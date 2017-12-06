package com.uth.facturacion;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import helpers.Clientes;
import helpers.FacturacionHelper;
import helpers.Servicios;

public class ServicioActivity extends AppCompatActivity {
    ImageButton btnSave;
    ImageButton btnDelete;
    EditText txtNombreServicio;
    EditText txtPrecio;
    EditText txtImpuesto;
    Context context;
    FacturacionHelper helperFacturacion;
    Servicios servicio;
    long id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio);

        context = this;
        //data
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            id = bundle.getLong("idServicio");
        //helpers
        helperFacturacion = new FacturacionHelper(this);
        servicio = new Servicios();
        //botones
        btnSave = (ImageButton) findViewById(R.id.btnSave);
        btnDelete = (ImageButton) findViewById(R.id.btnDelete);
        //campos
        txtNombreServicio = (EditText) findViewById(R.id.txtNombreServicio);
        txtPrecio = (EditText) findViewById(R.id.txtPrecio);
        txtImpuesto = (EditText) findViewById(R.id.txtImpuesto);

        //listeners
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id > 0){
                    String[] params = {Long.toString(id)};
                    long rs = helperFacturacion.updateServicios(
                            txtNombreServicio.getText().toString(),
                            txtPrecio.getText().toString(),
                            (txtImpuesto.getText().toString().isEmpty()) ? "0" : txtImpuesto.getText().toString(),
                            "_id=?",
                            params
                    );

                    if(rs > 0){
                        Toast.makeText(context, "Datos guardados existosamente.", Toast.LENGTH_LONG).show();
                        id = rs;
                    } else {
                        Toast.makeText(context, "Se produjo un error al guardar los datos.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    long rs = helperFacturacion.insertServicios(
                            txtNombreServicio.getText().toString(),
                            txtPrecio.getText().toString(),
                            (txtImpuesto.getText().toString().isEmpty()) ? "0" : txtImpuesto.getText().toString()
                    );

                    if(rs > 0){
                        Toast.makeText(context, "Datos guardados existosamente.", Toast.LENGTH_LONG).show();
                        id = rs;
                        btnDelete.setEnabled(true);
                    } else {
                        Toast.makeText(context, "Se produjo un error al guardar los datos.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ServicioActivity.this);
                // Setting Dialog Title
                alertDialog.setTitle("Servicios");
                // Setting Dialog Message
                alertDialog.setMessage("¿Desea eliminar el servicio?");
                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.ic);
                // Setting OK Button
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        String[] params = {Long.toString(id)};
                        helperFacturacion.deleteServicio("_id=?",params);
                        Toast.makeText(getApplicationContext(), "Servicio eliminado existosamente", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                // Showing Alert Message
                alertDialog.show();
            }
        });


        //propiedades
        if(id>0){
            btnDelete.setEnabled(true);
        } else {
            btnDelete.setEnabled(false);
        }


        getData();
    }

    private void getData()
    {
        String[] params = {Long.toString(id)};
        Cursor c = helperFacturacion.selectServicio("_id=?",params);

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                id = Long.parseLong(c.getString(c.getColumnIndex(servicio.ID)));
                txtNombreServicio.setText(c.getString(c.getColumnIndex(servicio.NOMBRE)));
                txtPrecio.setText(c.getString(c.getColumnIndex(servicio.PRECIO)));
                txtImpuesto.setText(c.getString(c.getColumnIndex(servicio.IMPUESTO)));
            }
        }
        finally {
            c.close();
        }
    }
}
