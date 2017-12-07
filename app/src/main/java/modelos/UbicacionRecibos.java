package modelos;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by jemarinero on 7/12/2017.
 */

public class UbicacionRecibos {
    public LatLng Ubicacion;
    public String NoRecibo;

    public UbicacionRecibos(LatLng ubicacion, String noRecibo) {
        Ubicacion = ubicacion;
        NoRecibo = noRecibo;
    }
}
