package com.uth.facturacion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import helpers.Clientes;
import helpers.FacturacionHelper;
import helpers.RecibosDet;
import helpers.RecibosEnc;
import modelos.ReciboDet;
import modelos.ReciboDetAdapter;

import static android.R.id.input;

public class ReciboDetalleActivity extends AppCompatActivity {

    String idCliente = "0";
    Long idRecibo = Long.valueOf(0);
    TextView tvNoRecibo;
    TextView tvCliente;
    FacturacionHelper helperFacturacion;
    Clientes cliente;
    RecibosEnc recEnc;
    RecibosDet recDet;
    ReciboDetAdapter adapter;
    ImageButton btnAdd;
    ImageButton btnAnular;
    ImageButton btnAplicar;
    ImageButton btnEnviar;
    Context context;
    String status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibo_detalle);
        context = this;
        helperFacturacion = new FacturacionHelper(this);
        cliente = new Clientes();
        recEnc = new RecibosEnc();
        recDet = new RecibosDet();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            idRecibo = bundle.getLong("idRecibo");
        }
        tvNoRecibo = (TextView) findViewById(R.id.tvNoRecibo);
        tvCliente = (TextView) findViewById(R.id.tvCliente);
        btnAdd = (ImageButton) findViewById(R.id.btnAdd);
        btnAnular = (ImageButton) findViewById(R.id.btnAnular);
        btnAplicar = (ImageButton) findViewById(R.id.btnAplicar);
        btnEnviar = (ImageButton) findViewById(R.id.btnEnviar);

        getRecibo();
        getCliente();

        ArrayList<ReciboDet> arrayOfRecibo = new ArrayList<ReciboDet>();
        // Create the adapter to convert the array to views
        adapter = new ReciboDetAdapter(this, arrayOfRecibo);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listRecibosDet);
        listView.setAdapter(adapter);
        getData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("1")) {
                    dlgDetalleRecibo dlg = new dlgDetalleRecibo(context, new dlgDetalleRecibo.DialogListener() {
                        @Override
                        public void ready(String idServicio, String precio, String impuesto, String cantidad) {
                            if (!idServicio.equals("0")) {
                                double imp = Double.parseDouble(impuesto) * Double.parseDouble(cantidad) * Double.parseDouble(precio);
                                double totalBruto = Double.parseDouble(precio) * Double.parseDouble(cantidad);
                                double total = imp + totalBruto;
                                helperFacturacion.insertReciboDet(idRecibo + "", idServicio, cantidad, precio, imp + "", total + "");
                                adapter.clear();
                                getData();
                            }
                        }

                        @Override
                        public void cancelled() {

                        }
                    });
                    dlg.setTitle("Seleccione un cliente");
                    dlg.setCancelable(false);
                    dlg.show();
                } else {
                    Toast.makeText(getApplicationContext(), "El estado del recibo no permite modificaciones", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAnular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!status.equals("3")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReciboDetalleActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Recibo");
                    // Setting Dialog Message
                    alertDialog.setMessage("¿Esta seguro que desea anular el recibo?");
                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.ic);
                    // Setting OK Button
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            String[] params = {idRecibo.toString()};
                            helperFacturacion.updateReciboEncEstado("3","_id=?",params);
                            Toast.makeText(getApplicationContext(), "Recibo anulado existosamente", Toast.LENGTH_SHORT).show();
                            status = "3";
                            adapter.clear();
                            getData();
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "El estado del recibo no permite modificaciones", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("1")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReciboDetalleActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Recibo");
                    // Setting Dialog Message
                    alertDialog.setMessage("¿Esta seguro que desea aplicar el recibo?");
                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.ic);
                    // Setting OK Button
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to execute after dialog closed
                            String[] params = {idRecibo.toString()};
                            helperFacturacion.updateReciboEncEstado("2","_id=?",params);
                            Toast.makeText(getApplicationContext(), "Recibo aplicado existosamente", Toast.LENGTH_SHORT).show();
                            status = "2";
                            adapter.clear();
                            getData();
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "El estado del recibo no permite modificaciones", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.equals("2")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReciboDetalleActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Recibo");
                    // Setting Dialog Message
                    alertDialog.setMessage("¿Esta seguro que desea enviar el recibo?");
                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.ic);
                    // Setting OK Button
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            Toast.makeText(getApplicationContext(), "Opcion en desarrollo.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "El recibo debe estar en estado aplicado para poder enviar.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(status.equals("1")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ReciboDetalleActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Servicio");
                    // Setting Dialog Message
                    alertDialog.setMessage("¿Desea eliminar el servicio seleccionado?");
                    // Setting Icon to Dialog
                    //alertDialog.setIcon(R.drawable.ic);
                    // Setting OK Button
                    alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String id = ((TextView)findViewById(R.id.idReciboDet)).getText().toString();
                            // Write your code here to execute after dialog closed
                            String[] params = {id};
                            helperFacturacion.deleteReciboDet("_id=?",params);
                            Toast.makeText(getApplicationContext(), "Servicio eliminado existosamente", Toast.LENGTH_SHORT).show();
                            adapter.clear();
                            getData();
                        }
                    });
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    Toast.makeText(getApplicationContext(), "El estado del recibo no permite modificaciones", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }

    private void getRecibo(){
        String[] params = {idRecibo.toString()};
        Cursor c = helperFacturacion.selectReciboEnc("_id=?",params);

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                idCliente = c.getString(c.getColumnIndex(recEnc.CLIENTE));
                tvNoRecibo.setText(c.getString(c.getColumnIndex(recEnc.NO_RECIBO)));
                status = c.getString(c.getColumnIndex(recEnc.ESTADO));
            }
        }
        finally {
            c.close();
        }
    }

    private void getCliente(){
        String[] params = {idCliente};
        Cursor c = helperFacturacion.selectCliente("_id=?",params);

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                tvCliente.setText(c.getString(c.getColumnIndex(cliente.NOMBRE)));
            }
        }
        finally {
            c.close();
        }
    }

    private void getData()
    {
        Cursor c = helperFacturacion.selectReciboDet(idRecibo+"");

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    adapter.add(new ReciboDet(
                            c.getString(c.getColumnIndex("Nombre")),
                            c.getString(c.getColumnIndex(recDet.CANTIDAD)),
                            c.getString(c.getColumnIndex(recDet.PRECIO)),
                            c.getString(c.getColumnIndex(recDet.IMPUESTO)),
                            c.getString(c.getColumnIndex(recDet.TOTAL)),
                            c.getString(c.getColumnIndex(recEnc.ID))));
                }
            }
        }
        finally {
            c.close();
        }
    }

}
