package com.uth.facturacion;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

import helpers.Configuracion;
import helpers.FacturacionHelper;
import io.github.skyhacker2.sqliteonweb.SQLiteOnWeb;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FacturacionHelper helperFacturacion;
    Configuracion config;
    TextView tvFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteOnWeb.init(this,9000).start();
        helperFacturacion = new FacturacionHelper(this);
        config = new Configuracion();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvFecha = (TextView) findViewById(R.id.tvFechaTrabajo);

        getData();

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String _month = ("00"+(month + 1));
        String _day = "00"+day;
        String fecha = _day.substring(_day.length()-2,_day.length()) + "/" + _month.substring(_month.length()-2,_month.length()) + "/" + year;

        if(tvFecha.getText().toString().equals(fecha)){
            tvFecha.setTextColor(Color.GREEN);
        } else {
            tvFecha.setTextColor(Color.RED);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        switch (id){
            case R.id.nav_configuracion: Intent config = new Intent(this,ConfiguracionActivity.class);
                                         startActivity(config);
                                         break;
            case R.id.nav_empresa:       Intent empresa = new Intent(this,EmpresaActivity.class);
                                         startActivity(empresa);
                                         break;
            case R.id.nav_clientes:      Intent cliente = new Intent(this,ListClienteActivity.class);
                                         startActivity(cliente);
                                         break;
            case R.id.nav_servicios:     Intent servicio = new Intent(this,ListServiciosActivity.class);
                                         startActivity(servicio);
                                         break;
            case R.id.nav_numeradores:   Intent numerador = new Intent(this,ListNumeradoresActivity.class);
                                         startActivity(numerador);
                                         break;
            case R.id.nav_recibos:       Intent recibo = new Intent(this,ListFacturacionActivity.class);
                                         startActivity(recibo);
                                         break;
                default: break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getData()
    {
        Cursor c = helperFacturacion.selectAllConfiguracion();

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                tvFecha.setText(c.getString(c.getColumnIndex(config.FECHA_TRABAJO)));
            }
        }
        finally {
            c.close();
        }
    }
}
