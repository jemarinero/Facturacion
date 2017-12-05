package com.uth.facturacion;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import helpers.Clientes;
import helpers.FacturacionHelper;
import helpers.RecibosDet;
import helpers.Servicios;
import modelos.Cliente;
import modelos.ReciboDet;
import modelos.Servicio;

/**
 * Created by jemarinero on 4/12/2017.
 */

public class dlgDetalleRecibo extends Dialog {

    private Context mContext;
    private Spinner mSpinner;
    private dlgDetalleRecibo.DialogListener mReadyListener;
    FacturacionHelper helperFacturacion;
    RecibosDet recDet;
    Servicios ser;
    ArrayAdapter adapter;
    private Button btnOk, btnCancel;
    String idServicio = "0";
    String precio = "0";
    String impuesto = "0";
    EditText txtCant;

    public dlgDetalleRecibo(Context context, dlgDetalleRecibo.DialogListener dl) {
        super(context);
        mReadyListener = dl;
        mContext = context;
    }

    public interface DialogListener {
        public void ready(String idServicio,String precio, String impuesto,String cantidad);
        public void cancelled();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_detalle_pedido);

        helperFacturacion = new FacturacionHelper(mContext);
        recDet = new RecibosDet();
        ser = new Servicios();

        txtCant = (EditText) findViewById(R.id.txtCantidad);

        ArrayList<Servicio> array = new ArrayList<Servicio>();
        // Attach the adapter to a ListView
        mSpinner = findViewById(R.id.cbxServicio);

        adapter = new ArrayAdapter(mContext,android.R.layout.simple_spinner_item, array);

        mSpinner.setAdapter(adapter);
        getData();
        btnOk = (Button) findViewById(R.id.btn_yes);
        //find cancel button
        btnCancel = (Button) findViewById(R.id.btn_no);
        //set action
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send cancel action to the interface
                mReadyListener.cancelled();
                //close dialog
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!idServicio.equals("0")){
                    mReadyListener.ready(idServicio,precio,impuesto,txtCant.getText().toString());
                }
                dismiss();
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Servicio st = (Servicio) mSpinner.getSelectedItem();
                idServicio = st.ID;
                precio = st.PRECIO;
                impuesto = st.IMPUESTO;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getData()
    {
        Cursor c = helperFacturacion.selectAllServicios();

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    adapter.add(new Servicio(
                            c.getString(c.getColumnIndex(ser.NOMBRE)),
                            c.getString(c.getColumnIndex(ser.PRECIO)),
                            c.getString(c.getColumnIndex(ser.ID)),
                            c.getString(c.getColumnIndex(ser.IMPUESTO))
                            ));
                }
            }
        }
        finally {
            c.close();
        }
    }
}
