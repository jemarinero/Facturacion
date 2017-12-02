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
 * Created by jemarinero on 1/12/2017.
 */

public class NumeradorAdapter extends ArrayAdapter<Numerador> {

    public NumeradorAdapter(Context context, ArrayList<Numerador> numerador) {
        super(context, 0, numerador);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Numerador num = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_numerador, parent, false);
        }
        TextView tvName = (TextView) convertView.findViewById(R.id.tvNumerador);
        tvName.setText(num.NUMERADOR);

        TextView tvSerie = (TextView) convertView.findViewById(R.id.tvSerie);
        tvSerie.setText(num.SERIE);

        TextView tvUltimo = (TextView) convertView.findViewById(R.id.tvUltimo);
        tvUltimo.setText(num.ULTIMO);

        TextView tvId = (TextView) convertView.findViewById(R.id.idNumerador);
        tvId.setText(num.ID);
        return convertView;
    }
}