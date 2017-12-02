package com.uth.facturacion;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import helpers.FacturacionHelper;
import helpers.Numeradores;
import helpers.Servicios;
import modelos.Numerador;
import modelos.NumeradorAdapter;
import modelos.Servicio;
import modelos.ServicioAdapter;

public class ListNumeradoresActivity extends AppCompatActivity {
    Context context;
    FacturacionHelper helperFacturacion;
    Numeradores nums;
    NumeradorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_numeradores);
        context = this;
        helperFacturacion = new FacturacionHelper(this);
        nums = new Numeradores();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent num = new Intent(context,NumeradorActivity.class);
                startActivity(num);
            }
        });

        ArrayList<Numerador> arrayOfNumerador = new ArrayList<Numerador>();
        // Create the adapter to convert the array to views
        adapter = new NumeradorAdapter(this, arrayOfNumerador);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listNumeradores);
        listView.setAdapter(adapter);
        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Long idNumerador = Long.parseLong(((TextView)view.findViewById(R.id.idNumerador)).getText().toString());
                Bundle bundle = new Bundle();
                bundle.putLong("idNumerador",idNumerador);
                Intent num = new Intent(context,NumeradorActivity.class);
                num.putExtras(bundle);
                startActivityForResult(num,1);
            }
        });
    }

    private void getData()
    {
        Cursor c = helperFacturacion.selectAllNumeradores();

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    adapter.add(new Numerador(
                            c.getString(c.getColumnIndex(nums.NUMERADOR)),
                            "Serie: "+c.getString(c.getColumnIndex(nums.SERIE)),
                            "Último numero usado: "+c.getString(c.getColumnIndex(nums.ULTIMO_USADO)),
                            c.getString(c.getColumnIndex(nums.ID))));
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
