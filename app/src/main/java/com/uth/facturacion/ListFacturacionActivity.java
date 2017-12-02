package com.uth.facturacion;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import helpers.Configuracion;
import helpers.FacturacionHelper;
import helpers.Numeradores;
import helpers.RecibosEnc;
import modelos.NumeradorAdapter;
import modelos.ReciboAdapter;

public class ListFacturacionActivity extends AppCompatActivity {

    FacturacionHelper helperFacturacion;
    Configuracion config;
    TextView tvFecha;
    Context context;
    RecibosEnc recEnc;
    ReciboAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_facturacion);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        tvFecha = (TextView) findViewById(R.id.tvFechaTrabajo);

        getFecha();

    }
    private void getFecha()
    {
        Cursor c = helperFacturacion.selectAllConfiguracion();

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                tvFecha.setText(c.getString(c.getColumnIndex(config.FECHA_TRABAJO)));
            }
        }
        finally {
            c.close();
        }
    }
}
