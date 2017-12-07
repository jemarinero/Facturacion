package com.uth.facturacion;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Calendar;

import helpers.AsyncTaskPost;
import helpers.Clientes;
import helpers.Configuracion;
import helpers.Numeradores;
import helpers.RecibosEnc;
import modelos.Empresa;
import helpers.FacturacionHelper;
import io.github.skyhacker2.sqliteonweb.SQLiteOnWeb;
import modelos.Cliente;
import modelos.Numerador;
import modelos.Recibo;
import modelos.ReciboDet;
import modelos.Servicio;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FacturacionHelper helperFacturacion;
    Configuracion config;
    TextView tvFecha;
    TextView tvVenta;
    String urlLista;
    Clientes cliente;
    helpers.Empresa empresa;
    helpers.Servicios ser;
    helpers.Numeradores nums;
    helpers.RecibosEnc recEnc;
    helpers.RecibosDet recDet;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SQLiteOnWeb.init(this,9000).start();
        helperFacturacion = new FacturacionHelper(this);
        config = new Configuracion();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostDatos();

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
        tvVenta = (TextView) findViewById(R.id.tvVenta);

        getData();
        getVentaDia();

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
                urlLista = c.getString(c.getColumnIndex(config.URL_SERVIDOR));
            }
        }
        finally {
            c.close();
        }
    }
    private void getVentaDia()
    {
        Cursor c = helperFacturacion.selectSumRecibosEncByDate(tvFecha.getText().toString());

        try
        {
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                tvVenta.setText(c.getString(c.getColumnIndex("MONTO_TOTAL")));
            }
        }
        finally {
            c.close();
        }
    }
    private void PostDatos(){
        SyncClientes();
        SyncEmpresa();
        SyncServicio();
        SyncNumeradores();
        SyncRecibosEnc();
        SyncRecibosDet();

        Toast.makeText(context,"Proceso de sincronización finalizado", Toast.LENGTH_SHORT).show();
    }

    private void SyncClientes(){
        Cursor c = helperFacturacion.selectAllCliente();
        cliente = new Clientes();

        String newUrl = urlLista+"/clientes";

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    Cliente clie = new Cliente(
                            c.getString(c.getColumnIndex(cliente.ID)),
                            c.getString(c.getColumnIndex(cliente.NOMBRE)),
                            c.getString(c.getColumnIndex(cliente.DIRECCION)),
                            c.getString(c.getColumnIndex(cliente.TELEFONO)),
                            c.getString(c.getColumnIndex(cliente.CORREO)),
                            c.getString(c.getColumnIndex(cliente.IDENTIDAD)),
                            c.getString(c.getColumnIndex(cliente.RTN)),
                            c.getString(c.getColumnIndex(cliente.LATITUD)),
                            c.getString(c.getColumnIndex(cliente.LONGITUD)),
                            c.getString(c.getColumnIndex(cliente.EXENTO_IMPUESTO)).equals("1") ? "true" : "false"
                            );
                    ObjectMapper mapper = new ObjectMapper();
                    String Json = mapper.writeValueAsString(clie);
                    new AsyncTaskPost(newUrl,Json,context).execute();
                }


            }
        } catch (Exception ex){

        }
        finally {
            c.close();
        }
    }

    private void SyncEmpresa(){
        Cursor c = helperFacturacion.selectAllEmpresa();
        empresa = new helpers.Empresa();

        String newUrl = urlLista+"/empresas";

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    Empresa data = new Empresa(
                            c.getString(c.getColumnIndex(empresa.ID)),
                            c.getString(c.getColumnIndex(empresa.NOMBRE)),
                            c.getString(c.getColumnIndex(empresa.DIRECCION)),
                            c.getString(c.getColumnIndex(empresa.RTN)),
                            c.getString(c.getColumnIndex(empresa.TELEFONO)),
                            c.getString(c.getColumnIndex(empresa.CORREO))
                    );
                    ObjectMapper mapper = new ObjectMapper();
                    String Json = mapper.writeValueAsString(data);
                    new AsyncTaskPost(newUrl,Json,context).execute();
                }


            }
        } catch (Exception ex){

        }
        finally {
            c.close();
        }
    }

    private void SyncServicio(){
        Cursor c = helperFacturacion.selectAllServicios();
        ser = new helpers.Servicios();

        String newUrl = urlLista+"/servicios";

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    Servicio data = new Servicio(
                            c.getString(c.getColumnIndex(ser.NOMBRE)),
                            c.getString(c.getColumnIndex(ser.PRECIO)),
                            c.getString(c.getColumnIndex(ser.ID)),
                            c.getString(c.getColumnIndex(ser.IMPUESTO))
                    );
                    ObjectMapper mapper = new ObjectMapper();
                    String Json = mapper.writeValueAsString(data);
                    new AsyncTaskPost(newUrl,Json,context).execute();
                }


            }
        } catch (Exception ex){

        }
        finally {
            c.close();
        }
    }

    private void SyncNumeradores(){
        Cursor c = helperFacturacion.selectAllNumeradores();
        nums = new Numeradores();

        String newUrl = urlLista+"/numeradores";

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    Numerador clie = new Numerador(
                            c.getString(c.getColumnIndex(nums.ID)),
                            c.getString(c.getColumnIndex(nums.NUMERADOR)),
                            c.getString(c.getColumnIndex(nums.SERIE)),
                            c.getString(c.getColumnIndex(nums.NUMERO_INICIO)),
                            c.getString(c.getColumnIndex(nums.NUMERO_FIN)),
                            c.getString(c.getColumnIndex(nums.FECHA_INICIO)),
                            c.getString(c.getColumnIndex(nums.FECHA_FIN)),
                            c.getString(c.getColumnIndex(nums.CAI)),
                            c.getString(c.getColumnIndex(nums.ULTIMO_USADO)),
                            c.getString(c.getColumnIndex(nums.ESTADO)).equals("1") ? "true" : "false"
                    );
                    ObjectMapper mapper = new ObjectMapper();
                    String Json = mapper.writeValueAsString(clie);
                    new AsyncTaskPost(newUrl,Json,context).execute();
                }


            }
        } catch (Exception ex){

        }
        finally {
            c.close();
        }
    }

    private void SyncRecibosEnc(){
        Cursor c = helperFacturacion.selectAllRecibosEnc();
        recEnc = new RecibosEnc();

        String newUrl = urlLista+"/recibosencs";

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    Recibo clie = new Recibo(
                            c.getString(c.getColumnIndex(recEnc.ID)),
                            c.getString(c.getColumnIndex(recEnc.NO_RECIBO)),
                            c.getString(c.getColumnIndex(recEnc.FECHA)),
                            c.getString(c.getColumnIndex(recEnc.CAI)),
                            c.getString(c.getColumnIndex(recEnc.CLIENTE)),
                            c.getString(c.getColumnIndex(recEnc.ESTADO)),
                            c.getString(c.getColumnIndex(recEnc.LATITUD)),
                            c.getString(c.getColumnIndex(recEnc.LONGITUD))
                    );
                    ObjectMapper mapper = new ObjectMapper();
                    String Json = mapper.writeValueAsString(clie);
                    new AsyncTaskPost(newUrl,Json,context).execute();
                }


            }
        } catch (Exception ex){

        }
        finally {
            c.close();
        }
    }
    private void SyncRecibosDet(){
        Cursor c = helperFacturacion.selectAllRecibosDet();
        recDet = new helpers.RecibosDet();

        String newUrl = urlLista+"/recibosdets";

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    ReciboDet clie = new ReciboDet(
                            c.getString(c.getColumnIndex(recDet.ID)),
                            c.getString(c.getColumnIndex(recDet.ID_RECIBO)),
                            c.getString(c.getColumnIndex(recDet.ID_SERVICIO)),
                            c.getString(c.getColumnIndex(recDet.CANTIDAD)),
                            c.getString(c.getColumnIndex(recDet.PRECIO)),
                            c.getString(c.getColumnIndex(recDet.IMPUESTO)),
                            c.getString(c.getColumnIndex(recDet.TOTAL))
                    );
                    ObjectMapper mapper = new ObjectMapper();
                    String Json = mapper.writeValueAsString(clie);
                    new AsyncTaskPost(newUrl,Json,context).execute();
                }


            }
        } catch (Exception ex){

        }
        finally {
            c.close();
        }
    }

}
