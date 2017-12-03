package com.uth.facturacion;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import helpers.Empresa;
import helpers.FacturacionHelper;

public class EmpresaActivity extends AppCompatActivity  {

    Empresa helper;
    FacturacionHelper helperFacturacion;
    EditText txtEmpresa;
    EditText txtDireccion;
    EditText txtTelefono;
    EditText txtCorreo;
    EditText txtRtn;
    Button btnGuardar;
    Context context;
    long id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);

        context = this;
        helperFacturacion = new FacturacionHelper(this);
        helper = new Empresa();
        btnGuardar = (Button) findViewById(R.id.btnSave);
        txtEmpresa = (EditText) findViewById(R.id.txtEmpresa);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtTelefono = (EditText) findViewById(R.id.txtTelefono);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtRtn = (EditText) findViewById(R.id.txtRtn);
        getDataEmpresa();
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long rs = 0;
                if(id == 0){
                    rs = helperFacturacion.insertEmpresa(
                            txtEmpresa.getText().toString(),
                            txtDireccion.getText().toString(),
                            txtTelefono.getText().toString(),
                            txtCorreo.getText().toString(),
                            txtRtn.getText().toString()
                    );
                } else {
                    rs = helperFacturacion.updateEmpresa(
                            txtEmpresa.getText().toString(),
                            txtDireccion.getText().toString(),
                            txtTelefono.getText().toString(),
                            txtCorreo.getText().toString(),
                            txtRtn.getText().toString(),
                            helper.ID + "=?",
                            new String[]{Long.toString(id)}
                    );
                }

                if(rs > 0){
                    Toast.makeText(context, "Datos guardados existosamente.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Se producjo un error al guardar los datos.", Toast.LENGTH_LONG).show();
                }
            }
        });



    }



    private void getDataEmpresa()
    {
        Cursor c = helperFacturacion.selectAllEmpresa();

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                id = c.getLong(c.getColumnIndex(helper.ID));
                txtEmpresa.setText(c.getString(c.getColumnIndex(helper.NOMBRE)));
                txtDireccion.setText(c.getString(c.getColumnIndex(helper.DIRECCION)));
                txtTelefono.setText(c.getString(c.getColumnIndex(helper.TELEFONO)));
                txtCorreo.setText(c.getString(c.getColumnIndex(helper.CORREO)));
                txtRtn.setText(c.getString(c.getColumnIndex(helper.RTN)));
            }
        }
        catch(Exception ex)
        {
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG);
        }
        finally {
            c.close();
        }
    }
}
