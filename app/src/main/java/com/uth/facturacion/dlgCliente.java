package com.uth.facturacion;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import helpers.Clientes;
import helpers.Configuracion;
import helpers.FacturacionHelper;
import helpers.RecibosEnc;
import modelos.Cliente;
import modelos.ClienteAdapter;
import modelos.Recibo;

/**
 * Created by jemarinero on 4/12/2017.
 */

public class dlgCliente extends Dialog {

    private Context mContext;
    private Spinner mSpinner;
    private DialogListener mReadyListener;
    FacturacionHelper helperFacturacion;
    Clientes cliente;
    ArrayAdapter adapter;
    private Button btnOk, btnCancel;
    String idCliente = "0";

    public dlgCliente(Context context, DialogListener dl) {
        super(context);
        mReadyListener = dl;
        mContext = context;
    }

    public interface DialogListener {
        public void ready(String idCliente);
        public void cancelled();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_add_recibo);

        helperFacturacion = new FacturacionHelper(mContext);
        cliente = new Clientes();

        ArrayList<Cliente> arrayOfClientes = new ArrayList<Cliente>();
        // Attach the adapter to a ListView
        mSpinner = (Spinner) findViewById(R.id.cbxCliente);

        adapter = new ArrayAdapter(mContext,android.R.layout.simple_spinner_item, arrayOfClientes);

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
                if(!idCliente.equals("0")){
                    mReadyListener.ready(idCliente);
                }
                dismiss();
            }
        });

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cliente st = (Cliente)mSpinner.getSelectedItem();
                idCliente = st.ID;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getData()
    {
        Cursor c = helperFacturacion.selectAllCliente();

        try
        {
            if (c != null && c.getCount() > 0) {
                while (c.moveToNext()) {
                    adapter.add(new Cliente(
                            c.getString(c.getColumnIndex(cliente.NOMBRE)),
                            "Teléfono: "+c.getString(c.getColumnIndex(cliente.TELEFONO)),
                            c.getString(c.getColumnIndex(cliente.ID))));
                }
            }
        }
        finally {
            c.close();
        }
    }
}
