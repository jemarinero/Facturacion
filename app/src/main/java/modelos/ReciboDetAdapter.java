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
 * Created by jemarinero on 4/12/2017.
 */

public class ReciboDetAdapter extends ArrayAdapter<ReciboDet> {

    public ReciboDetAdapter(Context context, ArrayList<ReciboDet> recibo) {
        super(context, 0, recibo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ReciboDet num = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_recibo_detalle, parent, false);
        }
        TextView tvServicio = (TextView) convertView.findViewById(R.id.tvServicio);
        tvServicio.setText(num.ID_SERVICIO);

        TextView tvCantidad = (TextView) convertView.findViewById(R.id.tvCantidad);
        tvCantidad.setText(num.CANTIDAD);

        TextView tvPrecio = (TextView) convertView.findViewById(R.id.tvPrecio);
        tvPrecio.setText(num.PRECIO);

        TextView tvImpuesto = (TextView) convertView.findViewById(R.id.tvImpuesto);
        tvImpuesto.setText(num.IMPUESTO);

        TextView tvMonto = (TextView) convertView.findViewById(R.id.tvMonto);
        tvMonto.setText(num.TOTAL);

        TextView tvId = (TextView) convertView.findViewById(R.id.idReciboDet);
        tvId.setText(num.ID);
        return convertView;
    }
}