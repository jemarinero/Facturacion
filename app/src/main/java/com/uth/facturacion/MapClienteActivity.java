package com.uth.facturacion;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import helpers.Clientes;
import helpers.FacturacionHelper;
import modelos.LocationTrack;


public class MapClienteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btnUbicar;
    Context context;
    long idCliente;
    FacturacionHelper helperFacturacion;
    Clientes cliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_cliente);
        Bundle bundle = getIntent().getExtras();
        idCliente = bundle.getLong("idCliente");
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
        context = this;
        // Add a marker in Sydney and move the camera
        LatLng hn = new LatLng(14.079526, -87.180662);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hn));


        helperFacturacion = new FacturacionHelper(MapClienteActivity.this);
        cliente = new Clientes();

        LatLng ll = getLatLog();

        mMap.addMarker(new MarkerOptions().position(ll));

        btnUbicar = (Button) findViewById(R.id.btnCoordenadas);
        btnUbicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.clear();
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                LocationTrack locationListener = new LocationTrack(getBaseContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    } else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);
                        LatLng lat = new LatLng(locationListener.getLatitude(),locationListener.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(lat));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat,10));
                        String[] id = {Long.toString(idCliente)};
                        helperFacturacion.updateClienteGPS(Double.toString(lat.latitude),Double.toString(lat.longitude),"_id=?",id);
                    }

                } else {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, locationListener);
                    LatLng lat = new LatLng(locationListener.getLatitude(),locationListener.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(lat));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(lat));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat,10));
                    String[] id = {Long.toString(idCliente)};
                    helperFacturacion.updateClienteGPS(Double.toString(lat.latitude),Double.toString(lat.longitude),"_id=?",id);
                }

            }
        });

    }

    private LatLng getLatLog() {

        if (idCliente > 0) {
            String[] id = {Long.toString(idCliente)};
            Cursor c = helperFacturacion.selectCliente("_id=?", id);

            try {
                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();

                    double lat = Double.parseDouble(c.getString(c.getColumnIndex(cliente.LATITUD)));
                    double log = Double.parseDouble(c.getString(c.getColumnIndex(cliente.LONGITUD)));

                    return new LatLng(lat,log);
                }
            } catch (Exception ex){
                return new LatLng(0,0);
            }
            finally {
                c.close();
            }
        }
        return new LatLng(0,0);
    }
}
