package com.uth.facturacion;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import helpers.Clientes;
import helpers.FacturacionHelper;
import modelos.Cliente;
import modelos.ClienteAdapter;


public class ListClienteActivity extends AppCompatActivity {
    Context context;
    FacturacionHelper helperFacturacion;
    Clientes cliente;
    ClienteAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_cliente);
        context = this;
        helperFacturacion = new FacturacionHelper(this);
        cliente = new Clientes();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAddCliente);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent clie = new Intent(context,ClienteActivity.class);
                startActivity(clie);
            }
        });

        ArrayList<Cliente> arrayOfClientes = new ArrayList<Cliente>();
        // Create the adapter to convert the array to views
        adapter = new ClienteAdapter(this, arrayOfClientes);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listClientes);
        listView.setAdapter(adapter);
        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Long idCliente = Long.parseLong(((TextView)view.findViewById(R.id.idCliente)).getText().toString());
                Bundle bundle = new Bundle();
                bundle.putLong("idCliente",idCliente);
                Intent clie = new Intent(context,ClienteActivity.class);
                clie.putExtras(bundle);
                startActivityForResult(clie,1);
            }
        });
    }

    private void getData()
    {
        Cursor c = helperFacturacion.selectAllCliente();

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    adapter.add(new Cliente(
                            c.getString(c.getColumnIndex(cliente.NOMBRE)),
                            "Teléfono: "+c.getString(c.getColumnIndex(cliente.TELEFONO)),
                            c.getString(c.getColumnIndex(cliente.ID))));
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
