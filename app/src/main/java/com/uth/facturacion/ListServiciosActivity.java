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

import helpers.Clientes;
import helpers.FacturacionHelper;
import helpers.Servicios;
import modelos.Cliente;
import modelos.ClienteAdapter;
import modelos.Servicio;
import modelos.ServicioAdapter;

public class ListServiciosActivity extends AppCompatActivity {

    Context context;
    FacturacionHelper helperFacturacion;
    Servicios servicio;
    ServicioAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_servicios);
        context = this;
        helperFacturacion = new FacturacionHelper(this);
        servicio = new Servicios();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ser = new Intent(context,ServicioActivity.class);
                startActivity(ser);
            }
        });

        ArrayList<Servicio> arrayOfServicio = new ArrayList<Servicio>();
        // Create the adapter to convert the array to views
        adapter = new ServicioAdapter(this, arrayOfServicio);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listServicios);
        listView.setAdapter(adapter);
        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Long idServicio = Long.parseLong(((TextView)view.findViewById(R.id.idServicio)).getText().toString());
                Bundle bundle = new Bundle();
                bundle.putLong("idServicio",idServicio);
                Intent ser = new Intent(context,ServicioActivity.class);
                ser.putExtras(bundle);
                startActivityForResult(ser,1);
            }
        });
    }

    private void getData()
    {
        Cursor c = helperFacturacion.selectAllServicios();

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    adapter.add(new Servicio(
                            c.getString(c.getColumnIndex(servicio.NOMBRE)),
                            "Precio: "+c.getString(c.getColumnIndex(servicio.PRECIO)),
                            c.getString(c.getColumnIndex(servicio.ID))));
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
