package com.uth.facturacion;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;

import helpers.Clientes;
import helpers.Configuracion;
import helpers.FacturacionHelper;
import helpers.Numeradores;
import helpers.RecibosEnc;
import modelos.LocationTrack;
import modelos.Numerador;
import modelos.NumeradorAdapter;
import modelos.Recibo;
import modelos.ReciboAdapter;

public class ListFacturacionActivity extends AppCompatActivity {

    FacturacionHelper helperFacturacion;
    Configuracion config;
    Numeradores num;
    TextView txtFecha;
    Context context;
    RecibosEnc recEnc;
    Clientes cliente;
    ReciboAdapter adapter;
    String fecha = "";
    long idNumerador = 0;
    int ultimo = 0;
    String cai = "";
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_facturacion);

        context = this;
        helperFacturacion = new FacturacionHelper(this);
        recEnc = new RecibosEnc();
        cliente = new Clientes();
        num = new Numeradores();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(fecha.toString().isEmpty() || fecha.toString().equals("")){
                    Toast.makeText(ListFacturacionActivity.this,"No hay fecha de trabajo configurada.", Toast.LENGTH_LONG).show();
                } else {
                    dlgCliente dlg = new dlgCliente(context, new dlgCliente.DialogListener() {
                        @Override
                        public void ready(String idCliente) {
                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            LocationTrack locationListener = new LocationTrack(getBaseContext());
                            Long idRecibo = Long.valueOf(0);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(ListFacturacionActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
                                } else {
                                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);
                                    LatLng lat = new LatLng(locationListener.getLatitude(),locationListener.getLongitude());
                                    getUltimoNumero();
                                    String[] idnum = {idNumerador+""};
                                    helperFacturacion.updateNumeradorUltimo((ultimo+1)+"","_id=?",idnum);
                                    idRecibo = helperFacturacion.insertReciboEnc((ultimo+1)+"",fecha,cai,idCliente,"1",Double.toString(lat.latitude),Double.toString(lat.longitude));
                                }

                            } else {
                                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);
                                LatLng lat = new LatLng(locationListener.getLatitude(),locationListener.getLongitude());
                                getUltimoNumero();
                                String[] idnum = {idNumerador+""};
                                helperFacturacion.updateNumeradorUltimo((ultimo+1)+"","_id=?",idnum);
                                idRecibo = helperFacturacion.insertReciboEnc((ultimo+1)+"",fecha,cai,idCliente,"1",Double.toString(lat.latitude),Double.toString(lat.longitude));
                            }
                            Bundle bundle = new Bundle();
                            bundle.putLong("idRecibo", idRecibo);
                            Intent num = new Intent(context, ReciboDetalleActivity.class);
                            num.putExtras(bundle);
                            startActivityForResult(num, 1);

                        }

                        @Override
                        public void cancelled() {

                        }
                    });
                    dlg.setTitle("Seleccione un cliente");
                    dlg.setCancelable(false);
                    dlg.show();
                }
            }
        });

        txtFecha = (TextView) findViewById(R.id.txtFecha);

        getFecha();
        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FechaTrabajoDatePicker();
            }
        });

        ArrayList<Recibo> arrayOfRecibo = new ArrayList<Recibo>();
        // Create the adapter to convert the array to views
        adapter = new ReciboAdapter(this, arrayOfRecibo);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.listRecibos);
        listView.setAdapter(adapter);
        getData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Long idRecibo = Long.parseLong(((TextView)view.findViewById(R.id.idRecibo)).getText().toString());
                Bundle bundle = new Bundle();
                bundle.putLong("idRecibo",idRecibo);
                Intent num = new Intent(context,ReciboDetalleActivity.class);
                num.putExtras(bundle);
                startActivityForResult(num,1);
            }
        });

    }
    private void getFecha()
    {
        Cursor c = helperFacturacion.selectAllConfiguracion();

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                txtFecha.setText(c.getString(c.getColumnIndex(config.FECHA_TRABAJO)));
                fecha = c.getString(c.getColumnIndex(config.FECHA_TRABAJO));
            }
        }
        finally {
            c.close();
        }
    }

    private void getUltimoNumero()
    {
        Cursor c = helperFacturacion.selectAllNumeradores();

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                idNumerador = c.getLong(c.getColumnIndex(num.ID));
                ultimo = c.getInt(c.getColumnIndex(num.ULTIMO_USADO));
                cai = c.getString(c.getColumnIndex(num.CAI));
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
        final DatePickerDialog datepicture = new DatePickerDialog(ListFacturacionActivity.this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view , int year, int month, int dayOfMonth) {
                String _month = ("00"+(month + 1));
                String day = "00"+dayOfMonth;
                String fecha = day.substring(day.length()-2,day.length()) + "/" + _month.substring(_month.length()-2,_month.length()) + "/" + year;
                txtFecha.setText(fecha);
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

    private void getData()
    {
        Cursor c = helperFacturacion.selectAllRecibosEnc();

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    String estado = "NA";

                    if(c.getString(c.getColumnIndex("Estados")).equals("1")){
                        estado = "En Proceso";
                    } else if (c.getString(c.getColumnIndex("Estados")).equals("2")){
                        estado = "Aplicado";
                    } else if (c.getString(c.getColumnIndex("Estados")).equals("3")){
                        estado = "Anulado";
                    }

                    adapter.add(new Recibo(
                            "No. Recibo: "+c.getString(c.getColumnIndex(recEnc.NO_RECIBO)),
                            "Cliente: "+c.getString(c.getColumnIndex(cliente.NOMBRE)),
                            "Monto: "+c.getString(c.getColumnIndex("MONTO_TOTAL")),
                            c.getString(c.getColumnIndex(recEnc.ID)),
                            "Estado: "+estado));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(ListFacturacionActivity.this, "Se otorgaron los permisos", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(ListFacturacionActivity.this, "Se negaron los permisos", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
