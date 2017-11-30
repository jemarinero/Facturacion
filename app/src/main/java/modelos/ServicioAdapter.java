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
 * Created by jemarinero on 30/11/2017.
 */

public class ServicioAdapter extends ArrayAdapter<Servicio> {

    public ServicioAdapter(Context context, ArrayList<Servicio> servicios) {
        super(context, 0, servicios);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Servicio cliente = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_servicio, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvNombreServicio);
        tvName.setText(cliente.NOMBRE);

        TextView tvPrecio = (TextView) convertView.findViewById(R.id.tvPrecio);
        tvPrecio.setText(cliente.PRECIO);

        TextView tvId = (TextView) convertView.findViewById(R.id.idServicio);
        tvId.setText(cliente.ID);
        return convertView;
    }
}
