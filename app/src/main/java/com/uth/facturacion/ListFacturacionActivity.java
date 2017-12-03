package com.uth.facturacion;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import helpers.Clientes;
import helpers.Configuracion;
import helpers.FacturacionHelper;
import helpers.Numeradores;
import helpers.RecibosEnc;
import modelos.Numerador;
import modelos.NumeradorAdapter;
import modelos.Recibo;
import modelos.ReciboAdapter;

public class ListFacturacionActivity extends AppCompatActivity {

    FacturacionHelper helperFacturacion;
    Configuracion config;
    TextView txtFecha;
    Context context;
    RecibosEnc recEnc;
    Clientes cliente;
    ReciboAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_facturacion);

        context = this;
        helperFacturacion = new FacturacionHelper(this);
        recEnc = new RecibosEnc();
        cliente = new Clientes();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        txtFecha = (TextView) findViewById(R.id.txtFecha);

        getFecha();
        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FechaTrabajoDatePicker();
            }
        });

        ArrayList<Recibo> arrayOfRecibo = new ArrayList<Recibo>();
        // Create the adapter to convert the array to views
        adapter = new ReciboAdapter(this, arrayOfRecibo);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listRecibos);
        listView.setAdapter(adapter);
        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Long idNumerador = Long.parseLong(((TextView)view.findViewById(R.id.idRecibo)).getText().toString());
                Bundle bundle = new Bundle();
                bundle.putLong("idRecibo",idNumerador);
//                Intent num = new Intent(context,NumeradorActivity.class);
//                num.putExtras(bundle);
//                startActivityForResult(num,1);
            }
        });

    }
    private void getFecha()
    {
        Cursor c = helperFacturacion.selectAllConfiguracion();

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                txtFecha.setText(c.getString(c.getColumnIndex(config.FECHA_TRABAJO)));
            }
        }
        finally {
            c.close();
        }
    }

    private void FechaTrabajoDatePicker(){

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datepicture = new DatePickerDialog(ListFacturacionActivity.this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view , int year, int month, int dayOfMonth) {
                String _month = ("00"+(month + 1));
                String day = "00"+dayOfMonth;
                String fecha = day.substring(day.length()-2,day.length()) + "/" + _month.substring(_month.length()-2,_month.length()) + "/" + year;
                txtFecha.setText(fecha);
            }
        }, year, month, day);
        datepicture.setTitle("Seleccione una fecha");
        datepicture.setButton(DialogInterface.BUTTON_POSITIVE, "Aceptar",datepicture);
        datepicture.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                datepicture.dismiss();
            }
        });
        datepicture.show();

    }

    private void getData()
    {
        Cursor c = helperFacturacion.selectAllRecibosEnc();

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    adapter.add(new Recibo(
                            c.getString(c.getColumnIndex(recEnc.NO_RECIBO)),
                            "Cliente: "+c.getString(c.getColumnIndex(cliente.NOMBRE)),
                            "Monto: "+c.getString(c.getColumnIndex("TOTAL_MONTO")),
                            c.getString(c.getColumnIndex(recEnc.ID))));
                }
            }
        }
        finally {
            c.close();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if(requestCode == 1 && resultCode==RESULT_OK)
        {
            adapter.clear();
            getData();
        }
    }
}
