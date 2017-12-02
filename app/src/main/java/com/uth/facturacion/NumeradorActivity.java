package com.uth.facturacion;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import helpers.Clientes;
import helpers.FacturacionHelper;
import helpers.Numeradores;

public class NumeradorActivity extends AppCompatActivity {

    ImageButton btnSave;
    ImageButton btnDelete;
    EditText txtNumerador;
    EditText txtSerie;
    EditText txtNumInicio;
    EditText txtNumFin;
    EditText txtFechaInicio;
    EditText txtFechaFin;
    EditText txtCai;
    EditText txtUltimo;
    CheckBox chkEstado;
    Context context;
    FacturacionHelper helperFacturacion;
    Numeradores num;
    long id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numerador);

        context = this;
        //data
        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
            id = bundle.getLong("idNumerador");
        //helpers
        helperFacturacion = new FacturacionHelper(this);
        num = new Numeradores();
        //botones
        btnSave = (ImageButton) findViewById(R.id.btnSave);
        btnDelete = (ImageButton) findViewById(R.id.btnDelete);
        //campos
        txtNumerador = (EditText) findViewById(R.id.txtNumerador);
        txtSerie = (EditText) findViewById(R.id.txtSerie);
        txtNumInicio = (EditText) findViewById(R.id.txtNumInicio);
        txtNumFin = (EditText) findViewById(R.id.txtNumFin);
        txtFechaInicio = (EditText) findViewById(R.id.txtFechaInicio);
        txtFechaFin = (EditText) findViewById(R.id.txtFechaFin);
        txtCai = (EditText) findViewById(R.id.txtCai);
        txtUltimo = (EditText) findViewById(R.id.txtUltimo);
        chkEstado = (CheckBox) findViewById(R.id.chkEstado);

        //listeners
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id > 0){
                    String[] params = {Long.toString(id)};
                    long rs = helperFacturacion.updateNumerador(
                            txtNumerador.getText().toString(),
                            txtSerie.getText().toString(),
                            txtNumInicio.getText().toString(),
                            txtNumFin.getText().toString(),
                            txtFechaInicio.getText().toString(),
                            txtFechaFin.getText().toString(),
                            txtCai.getText().toString(),
                            txtUltimo.getText().toString(),
                            (chkEstado.isChecked() ? "1" : "0"),
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
                    long rs = helperFacturacion.insertNumerador(
                            txtNumerador.getText().toString(),
                            txtSerie.getText().toString(),
                            txtNumInicio.getText().toString(),
                            txtNumFin.getText().toString(),
                            txtFechaInicio.getText().toString(),
                            txtFechaFin.getText().toString(),
                            txtCai.getText().toString(),
                            txtUltimo.getText().toString(),
                            (chkEstado.isChecked() ? "1" : "0")
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
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(NumeradorActivity.this);
                // Setting Dialog Title
                alertDialog.setTitle("Numerador");
                // Setting Dialog Message
                alertDialog.setMessage("¿Desea eliminar el numerador?");
                // Setting Icon to Dialog
                //alertDialog.setIcon(R.drawable.ic);
                // Setting OK Button
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog closed
                        String[] params = {Long.toString(id)};
                        helperFacturacion.deleteNumerador("_id=?",params);
                        Toast.makeText(getApplicationContext(), "Numerador eliminado existosamente", Toast.LENGTH_SHORT).show();
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
        txtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FechaInicioDatePicker();
            }
        });

        txtFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FechaFinDatePicker();
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
        Cursor c = helperFacturacion.selectNumerador("_id=?",params);

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                id = Long.parseLong(c.getString(c.getColumnIndex(num.ID)));
                txtNumerador.setText(c.getString(c.getColumnIndex(num.NUMERADOR)));
                txtSerie.setText(c.getString(c.getColumnIndex(num.SERIE)));
                txtNumInicio.setText(c.getString(c.getColumnIndex(num.NUMERO_INICIO)));
                txtNumFin.setText(c.getString(c.getColumnIndex(num.NUMERO_FIN)));
                txtFechaInicio.setText(c.getString(c.getColumnIndex(num.FECHA_INICIO)));
                txtFechaFin.setText(c.getString(c.getColumnIndex(num.FECHA_FIN)));
                txtCai.setText(c.getString(c.getColumnIndex(num.CAI)));
                txtUltimo.setText(c.getString(c.getColumnIndex(num.ULTIMO_USADO)));
                String chk = c.getString(c.getColumnIndex(num.ESTADO));
                chkEstado.setChecked((chk.equals("1")?true:false));
            }
        }
        finally {
            c.close();
        }
    }
    private void FechaInicioDatePicker(){

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datepicture = new DatePickerDialog(NumeradorActivity.this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view , int year, int month, int dayOfMonth) {
                String _month = ("00"+(month + 1));
                String day = "00"+dayOfMonth;
                String fecha = day.substring(day.length()-2,day.length()) + "/" + _month.substring(_month.length()-2,_month.length()) + "/" + year;
                txtFechaInicio.setText(fecha);
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

    private void FechaFinDatePicker(){

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datepicture = new DatePickerDialog(NumeradorActivity.this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view , int year, int month, int dayOfMonth) {
                String _month = ("00"+(month + 1));
                String day = "00"+dayOfMonth;
                String fecha = day.substring(day.length()-2,day.length()) + "/" + _month.substring(_month.length()-2,_month.length()) + "/" + year;
                txtFechaFin.setText(fecha);
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
