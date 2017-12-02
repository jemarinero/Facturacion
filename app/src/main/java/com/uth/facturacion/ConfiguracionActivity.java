package com.uth.facturacion;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import helpers.Configuracion;
import helpers.FacturacionHelper;

public class ConfiguracionActivity extends AppCompatActivity {

    Configuracion helperConfig;
    FacturacionHelper helperFacturacion;
    EditText txtUtrl;
    EditText txtFechaTrabajo;
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
        txtFechaTrabajo = (EditText) findViewById(R.id.txtFechaTrabajo);
        getData();
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helperFacturacion.deleteAllConfiguracion();
                long rs = helperFacturacion.insertConfiguracion(txtFechaTrabajo.getText().toString(),txtUtrl.getText().toString());

                if(rs > 0){
                    Toast.makeText(context, "Datos guardados existosamente.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Se producjo un error al guardar los datos.", Toast.LENGTH_LONG).show();
                }
            }
        });
        txtFechaTrabajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FechaTrabajoDatePicker();
            }
        });
    }

    private void getData()
    {
        Cursor c = helperFacturacion.selectAllConfiguracion();

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                txtUtrl.setText(c.getString(c.getColumnIndex(helperConfig.URL_SERVIDOR)));
                txtFechaTrabajo.setText(c.getString(c.getColumnIndex(helperConfig.FECHA_TRABAJO)));
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
        final DatePickerDialog datepicture = new DatePickerDialog(ConfiguracionActivity.this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view , int year, int month, int dayOfMonth) {
                String _month = ("00"+(month + 1));
                String day = "00"+dayOfMonth;
                String fecha = day.substring(day.length()-2,day.length()) + "/" + _month.substring(_month.length()-2,_month.length()) + "/" + year;
                txtFechaTrabajo.setText(fecha);
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
}
