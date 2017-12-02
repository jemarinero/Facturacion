package com.uth.facturacion;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import helpers.Clientes;
import helpers.FacturacionHelper;
import modelos.Cliente;


public class ClienteActivity extends AppCompatActivity {
    ImageButton btnMapa;
    ImageButton btnSave;
    ImageButton btnDelete;
    EditText txtNombreCliente;
    EditText txtDireccion;
    EditText txtTelefono;
    EditText txtCorreo;
    EditText txtIdentidad;
    EditText txtRtn;
    CheckBox chkExento;
    Context context;
    FacturacionHelper helperFacturacion;
    Clientes cliente;
    long id = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);
        context = this;
        //data
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            id = bundle.getLong("idCliente");
        //helpers
        helperFacturacion = new FacturacionHelper(this);
        cliente = new Clientes();
        //botones
        btnMapa = (ImageButton) findViewById(R.id.btnMapa);
        btnSave = (ImageButton) findViewById(R.id.btnSave);
        btnDelete = (ImageButton) findViewById(R.id.btnDelete);
        //campos
        txtNombreCliente = (EditText) findViewById(R.id.txtNombreCliente);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtIdentidad = (EditText) findViewById(R.id.txtIdentidad);
        txtRtn = (EditText) findViewById(R.id.txtRtn);
        chkExento = (CheckBox) findViewById(R.id.chkExento);

        //listeners
        btnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("idCliente",id);
                Intent clie = new Intent(context,MapClienteActivity.class);
                clie.putExtras(bundle);
                startActivity(clie);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id > 0){
                    String[] params = {Long.toString(id)};
                    long rs = helperFacturacion.updateCliente(
                            txtNombreCliente.getText().toString(),
                            txtDireccion.getText().toString(),
                            txtTelefono.getText().toString(),
                            txtCorreo.getText().toString(),
                            txtIdentidad.getText().toString(),
                            txtRtn.getText().toString(),
                            (chkExento.isChecked() ? "1" : "0"),
                            "_id=?",
                            params
                    );

                    if(rs > 0){
                        Toast.makeText(context, "Datos guardados existosamente.", Toast.LENGTH_LONG).show();
                        id = rs;
                        btnMapa.setEnabled(true);
                    } else {
                        Toast.makeText(context, "Se produjo un error al guardar los datos.", Toast.LENGTH_LONG).show();
                    }

                } else {
                    long rs = helperFacturacion.insertCliente(
                            txtNombreCliente.getText().toString(),
                            txtDireccion.getText().toString(),
                            txtTelefono.getText().toString(),
                            txtCorreo.getText().toString(),
                            txtIdentidad.getText().toString(),
                            txtRtn.getText().toString(),
                            "",
                            "",
                            (chkExento.isChecked() ? "1" : "0")
                    );

                    if(rs > 0){
                        Toast.makeText(context, "Datos guardados existosamente.", Toast.LENGTH_LONG).show();
                        id = rs;
                        btnMapa.setEnabled(true);
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ClienteActivity.this);
                // Setting Dialog Title
                alertDialog.setTitle("Clientes");
                // Setting Dialog Message
                alertDialog.setMessage("¿Desea eliminar el cliente?");
                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.ic);
                // Setting OK Button
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        String[] params = {Long.toString(id)};
                        helperFacturacion.deleteCliente("_id=?",params);
                        Toast.makeText(getApplicationContext(), "Cliente eliminado existosamente", Toast.LENGTH_SHORT).show();
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
            btnMapa.setEnabled(true);
            btnDelete.setEnabled(true);
        } else {
            btnMapa.setEnabled(false);
            btnDelete.setEnabled(false);
        }


        getData();
    }


    private void getData()
    {
        String[] params = {Long.toString(id)};
        Cursor c = helperFacturacion.selectCliente("_id=?",params);

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                id = Long.parseLong(c.getString(c.getColumnIndex(cliente.ID)));
                txtNombreCliente.setText(c.getString(c.getColumnIndex(cliente.NOMBRE)));
                txtDireccion.setText(c.getString(c.getColumnIndex(cliente.DIRECCION)));
                txtTelefono.setText(c.getString(c.getColumnIndex(cliente.TELEFONO)));
                txtCorreo.setText(c.getString(c.getColumnIndex(cliente.CORREO)));
                txtIdentidad.setText(c.getString(c.getColumnIndex(cliente.IDENTIDAD)));
                txtRtn.setText(c.getString(c.getColumnIndex(cliente.RTN)));
                String chk = c.getString(c.getColumnIndex(cliente.EXENTO_IMPUESTO));
                chkExento.setChecked((chk.equals("1")?true:false));
            }
        }
        finally {
            c.close();
        }
    }
}
