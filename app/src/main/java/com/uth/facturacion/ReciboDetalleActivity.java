package com.uth.facturacion;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import helpers.Clientes;
import helpers.Empresa;
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
    TextView tvEstado;
    FacturacionHelper helperFacturacion;
    Clientes cliente;
    RecibosEnc recEnc;
    RecibosDet recDet;
    Empresa emp;
    ReciboDetAdapter adapter;
    ImageButton btnAdd;
    ImageButton btnAnular;
    ImageButton btnAplicar;
    ImageButton btnEnviar;
    Context context;
    String status="";
    String correo = "";
    String[] datosRecibo = new String[2];
    ArrayList<ReciboDet> arrRecDet = new ArrayList<ReciboDet>();
    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibo_detalle);
        context = this;
        helperFacturacion = new FacturacionHelper(this);
        cliente = new Clientes();
        recEnc = new RecibosEnc();
        recDet = new RecibosDet();
        emp = new Empresa();
        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            idRecibo = bundle.getLong("idRecibo");
        }
        tvNoRecibo = (TextView) findViewById(R.id.tvNoRecibo);
        tvCliente = (TextView) findViewById(R.id.tvCliente);
        tvEstado = (TextView) findViewById(R.id.tvEstado);
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
                            setEstado();
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
                            setEstado();
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

                            String recibo = "";

                            String[] datosEmpresa = getDataEmpresa();
                            String[] datosCliente = getDatosCliente();

                            StringBuilder sbRecibo = new StringBuilder()
                                    .append("Empresa:   "+datosEmpresa[0]+"\n")
                                    .append("Dirección: "+datosEmpresa[1]+"\n")
                                    .append("Teléfono:  "+datosEmpresa[2]+"\n")
                                    .append("Correo:    "+datosEmpresa[3]+"\n")
                                    .append("RTN:       "+datosEmpresa[4]+"\n")
                                    .append("\n")
                                    .append("Cliente:   "+datosCliente[0]+"\n")
                                    .append("Dirección: "+datosCliente[1]+"\n")
                                    .append("Teléfono:  "+datosCliente[2]+"\n")
                                    .append("Correo:    "+datosCliente[3]+"\n")
                                    .append("RTN:       "+datosCliente[4]+"\n")
                                    .append("\n")
                                    .append("No. Recibo:       "+tvNoRecibo.getText().toString()+"\n")
                                    .append("CAI:              "+datosRecibo[1]+"\n")
                                    .append("Fecha de Emisión: "+datosRecibo[0]+"\n")
                                    .append("\n")
                                    .append("Servicio                      Cant.      Precio     Imp.       Total      \n")
                                    .append("--------------------------------------------------------------------------\n");
                            double total = 0.0;
                            for (ReciboDet rd : arrRecDet) {
                                String servicio = (rd.ID_SERVICIO+"                              ").substring(0,30);
                                String cant = "        "+String.format("%,.2f",Double.parseDouble(rd.CANTIDAD))+"  ";
                                cant = cant.substring(cant.length()-11, cant.length());
                                String prec = "        "+String.format("%,.2f",Double.parseDouble(rd.PRECIO))+"  ";
                                prec = prec.substring(prec.length()-11, prec.length());
                                String imp = "        "+String.format("%,.2f",Double.parseDouble(rd.IMPUESTO))+"  ";
                                imp = imp.substring(imp.length()-11, imp.length());
                                String ttl = "        "+String.format("%,.2f",Double.parseDouble(rd.TOTAL))+"  ";
                                ttl = ttl.substring(ttl.length()-11, ttl.length());
                                total+= Double.parseDouble(rd.TOTAL);
                                sbRecibo.append(servicio+cant+prec+imp+ttl+"\n");

                            }

                            sbRecibo.append("\n")
                                    .append("\n")
                                    .append("\n")
                                    .append("Total Monto: L."+String.format("%,.2f",total)+"\n")
                                    .append("\n")
                                    .append("\n")
                                    .append("\n")
                                    .append("\n")
                                    .append("\n")
                                    .append("\n")
                                    .append("__________________________\n")
                                    .append("    Firma del Cliente\n")
                                    .append("\n")
                                    .append("\n");

                            recibo = sbRecibo.toString();
                            generateRecibo("recibo"+tvNoRecibo.getText().toString()+".txt",recibo);

                            Intent i = new Intent(Intent.ACTION_SENDTO );
                            i.setType("message/rfc822");
                            i.setData(Uri.parse("mailto:"));
                            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{correo});
                            i.putExtra(Intent.EXTRA_SUBJECT, "Recibo por Servicios No: "+tvNoRecibo.getText().toString());
                            i.putExtra(Intent.EXTRA_TEXT   , new StringBuilder()
                                    .append("Buen día Sr(a).: "+tvCliente.getText().toString()+"\n")
                                    .append("Adjunto recibo por servicios No: "+tvNoRecibo.getText().toString()+"\n")
                                    .append("Saludos,\n")
                                    .toString());
                            try {
                                startActivity(Intent.createChooser(i, "Send mail..."));
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(ReciboDetalleActivity.this, "No hay cliente de correo instalado.", Toast.LENGTH_SHORT).show();
                            }
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

        setEstado();
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
                datosRecibo[0] = c.getString(c.getColumnIndex(recEnc.FECHA));
                datosRecibo[1] = c.getString(c.getColumnIndex(recEnc.CAI));
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
                correo = c.getString(c.getColumnIndex(cliente.CORREO));
            }
        }
        finally {
            c.close();
        }
    }

    private String [] getDatosCliente(){
        String[] params = {idCliente};
        Cursor c = helperFacturacion.selectCliente("_id=?",params);
        String[] cli = new String[5];
        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                cli[0] = c.getString(c.getColumnIndex(cliente.NOMBRE));
                cli[1] = c.getString(c.getColumnIndex(cliente.DIRECCION));
                cli[2] = c.getString(c.getColumnIndex(cliente.TELEFONO));
                cli[3] = c.getString(c.getColumnIndex(cliente.CORREO));
                cli[4] = c.getString(c.getColumnIndex(cliente.RTN));
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG);
        }
        finally {
            c.close();
        }

        return cli;
    }

    private void getData()
    {
        Cursor c = helperFacturacion.selectReciboDet(idRecibo+"");

        arrRecDet.clear();

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

                    arrRecDet.add(new ReciboDet(
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

    private void setEstado(){
        String estado = "NA";

        if(status.equals("1")){
            estado = "En Proceso";
        } else if (status.equals("2")){
            estado = "Aplicado";
        } else if (status.equals("3")){
            estado = "Anulado";
        }

        tvEstado.setText(estado);
    }
    public void generateRecibo(String sFileName, String sBody) {
        try {
            File root = getRecibosStorageDir("Recibos");
            File gpxfile = new File(root, sFileName);
            //gpxfile.createNewFile();
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Se creo el recibo en el almacenamiento interno.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getRecibosStorageDir(String recibo) {
        // Get the directory for the user's public pictures directory.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            Toast.makeText(context, "Error: external storage is unavailable",Toast.LENGTH_SHORT).show();
            return null;
        }
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Toast.makeText(context, "Error: external storage is read only.",Toast.LENGTH_SHORT).show();
            return null;
        }

        if (ContextCompat.checkSelfPermission(context, // request permission when it is not granted.
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "permission:WRITE_EXTERNAL_STORAGE: NOT granted!",Toast.LENGTH_SHORT).show();
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),recibo);
        if (file.exists()) {
            Log.d("PVHMovil","folder exist:"+file.toString());
        }else{
            try {
                if (file.mkdir()) {
                    Log.d("PVHMovil", "folder created:" + file.toString());
                } else {
                    Log.d("PVHMovil", "creat folder fails:" + file.toString());
                }
            }catch (Exception ecp){
                ecp.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private String[] getDataEmpresa()
    {
        Cursor c = helperFacturacion.selectAllEmpresa();

        String[] empresa = new String[5];
        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                empresa[0] = c.getString(c.getColumnIndex(emp.NOMBRE));
                empresa[1] = c.getString(c.getColumnIndex(emp.DIRECCION));
                empresa[2] = c.getString(c.getColumnIndex(emp.TELEFONO));
                empresa[3] = c.getString(c.getColumnIndex(emp.CORREO));
                empresa[4] = c.getString(c.getColumnIndex(emp.RTN));
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG);
        }
        finally {
            c.close();
        }

        return empresa;
    }
}
