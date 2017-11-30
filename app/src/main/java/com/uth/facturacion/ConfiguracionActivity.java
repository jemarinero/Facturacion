package com.uth.facturacion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import helpers.Configuracion;
import helpers.FacturacionHelper;

public class ConfiguracionActivity extends AppCompatActivity {

    Configuracion helperConfig;
    FacturacionHelper helperFacturacion;
    EditText txtUtrl;
    Button btnGuardar;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);
        context = this;
        helperFacturacion = new FacturacionHelper(this);
        helperConfig = new Configuracion();
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        txtUtrl = (EditText) findViewById(R.id.txtUrl);
        getUrl();
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helperFacturacion.deleteAllConfiguracion();
                long rs = helperFacturacion.insertConfiguracion("",txtUtrl.getText().toString());

                if(rs > 0){
                    Toast.makeText(context, "Datos guardados existosamente.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Se producjo un error al guardar los datos.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getUrl()
    {
        Cursor c = helperFacturacion.selectAllConfiguracion();

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                txtUtrl.setText(c.getString(c.getColumnIndex(helperConfig.URL_SERVIDOR)));
            }
        }
        finally {
            c.close();
        }
    }
}
