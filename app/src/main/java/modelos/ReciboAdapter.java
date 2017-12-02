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
 * Created by jemarinero on 2/12/2017.
 */

public class ReciboAdapter extends ArrayAdapter<Recibo> {

    public ReciboAdapter(Context context, ArrayList<Recibo> recibo) {
        super(context, 0, recibo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Recibo num = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_recibo, parent, false);
        }
        TextView tvNoRecibo = (TextView) convertView.findViewById(R.id.tvNoRecibo);
        tvNoRecibo.setText(num.NO_RECIBO);

        TextView tvCliente = (TextView) convertView.findViewById(R.id.tvCliente);
        tvCliente.setText(num.CLIENTE);

        TextView tvMonto = (TextView) convertView.findViewById(R.id.tvMonto);
        tvMonto.setText(num.MONTO);

        TextView tvId = (TextView) convertView.findViewById(R.id.idRecibo);
        tvId.setText(num.ID);
        return convertView;
    }
}