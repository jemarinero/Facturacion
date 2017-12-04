package com.uth.facturacion;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import helpers.Clientes;
import helpers.FacturacionHelper;
import helpers.RecibosDet;
import helpers.RecibosEnc;
import modelos.ReciboDet;
import modelos.ReciboDetAdapter;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibo_detalle);
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

        getRecibo();
        getCliente();

        ArrayList<ReciboDet> arrayOfRecibo = new ArrayList<ReciboDet>();
        // Create the adapter to convert the array to views
        adapter = new ReciboDetAdapter(this, arrayOfRecibo);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listRecibosDet);
        listView.setAdapter(adapter);
        getData();
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
                            c.getString(c.getColumnIndex("NOMBRE")),
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
