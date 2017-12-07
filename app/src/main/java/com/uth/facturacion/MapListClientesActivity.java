package com.uth.facturacion;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Calendar;

import helpers.FacturacionHelper;
import modelos.UbicacionRecibos;

public class MapListClientesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    FacturacionHelper helperFacturacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_list_clientes);
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
        helperFacturacion = new FacturacionHelper(MapListClientesActivity.this);


        addMarkers();
    }

    private ArrayList<UbicacionRecibos> getLatLog() {

        Cursor c = helperFacturacion.selectAllCliente();

        ArrayList<UbicacionRecibos> ltLatLng = new ArrayList<UbicacionRecibos>();

        try {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {

                    double lat = Double.parseDouble(c.getString(c.getColumnIndex("Latitud")));
                    double log = Double.parseDouble(c.getString(c.getColumnIndex("Longitud")));
                    String cliente = c.getString(c.getColumnIndex("Nombre"));

                    ltLatLng.add(new UbicacionRecibos(new LatLng(lat,log),cliente));
                }
            }
        }catch (Exception ex){

        }
        finally {
            c.close();
        }
        return ltLatLng;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(MapListClientesActivity.this, "Se otorgaron los permisos", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(MapListClientesActivity.this, "Se negaron los permisos", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



    private void addMarkers(){
        mMap.clear();
        ArrayList<UbicacionRecibos> ltlgRecibos = getLatLog();

        if(ltlgRecibos != null && ltlgRecibos.size() > 0){
            for (UbicacionRecibos ub : ltlgRecibos) {
                mMap.addMarker(new MarkerOptions().position(ub.Ubicacion).title("Cliente: "+ub.NoRecibo));
            }
        }
    }
}
