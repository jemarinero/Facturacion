package modelos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uth.facturacion.R;

import java.util.ArrayList;


/**
 * Created by jemarinero on 25/11/2017.
 */

public class ClienteAdapter extends ArrayAdapter<Cliente>
{
    public ClienteAdapter(Context context, ArrayList<Cliente> clientes) {
        super(context, 0, clientes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Cliente cliente = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_cliente, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvNombreCliente);
        tvName.setText(cliente.NOMBRE);

        TextView tvTelefono = (TextView) convertView.findViewById(R.id.tvTelefono);
        tvTelefono.setText(cliente.TELEFONO);

        TextView tvId = (TextView) convertView.findViewById(R.id.idCliente);
        tvId.setText(cliente.ID);
        return convertView;
    }
}
