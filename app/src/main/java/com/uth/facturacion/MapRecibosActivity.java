package com.uth.facturacion;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import helpers.FacturacionHelper;
import modelos.UbicacionRecibos;


public class MapRecibosActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    FacturacionHelper helperFacturacion;
    String fecha;
    TextView txtFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_recibos);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng hn = new LatLng(14.079526, -87.180662);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hn));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(hn,7));
        helperFacturacion = new FacturacionHelper(MapRecibosActivity.this);

        txtFecha = (TextView) findViewById(R.id.txtFecha);


        getFecha();
        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FechaTrabajoDatePicker();

            }
        });

        addMarkers(fecha);
    }

    private ArrayList<UbicacionRecibos> getLatLog(String _fecha) {

        Cursor c = helperFacturacion.selectAllRecibosEncByDate(_fecha);

        ArrayList<UbicacionRecibos> ltLatLng = new ArrayList<UbicacionRecibos>();

            try {
                if (c != null && c.getCount() > 0) {
                    while (c.moveToNext()) {

                        double lat = Double.parseDouble(c.getString(c.getColumnIndex("Latitud")));
                        double log = Double.parseDouble(c.getString(c.getColumnIndex("Longitud")));
                        String noRecibo = c.getString(c.getColumnIndex("NoRecibo"));

                        ltLatLng.add(new UbicacionRecibos(new LatLng(lat,log),noRecibo));
                    }
                }
            }catch (Exception ex){

            }
            finally {
                c.close();
            }
        return ltLatLng;
    }

    private void getFecha()
    {
        Cursor c = helperFacturacion.selectAllConfiguracion();

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                fecha = c.getString(c.getColumnIndex("FechaTrabajo"));
                txtFecha.setText(fecha);
            }
        }
        finally {
            c.close();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(MapRecibosActivity.this, "Se otorgaron los permisos", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(MapRecibosActivity.this, "Se negaron los permisos", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void FechaTrabajoDatePicker(){

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datepicture = new DatePickerDialog(MapRecibosActivity.this , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view , int year, int month, int dayOfMonth) {
                String _month = ("00"+(month + 1));
                String day = "00"+dayOfMonth;
                String fecha = day.substring(day.length()-2,day.length()) + "/" + _month.substring(_month.length()-2,_month.length()) + "/" + year;
                txtFecha.setText(fecha);
                addMarkers(fecha);

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

    private void addMarkers(String fecha){
        mMap.clear();
        ArrayList<UbicacionRecibos> ltlgRecibos = getLatLog(fecha);

        if(ltlgRecibos != null && ltlgRecibos.size() > 0){
            for (UbicacionRecibos ub : ltlgRecibos) {
                mMap.addMarker(new MarkerOptions().position(ub.Ubicacion).title("No. Recibo: "+ub.NoRecibo));
            }
        }
    }
}
