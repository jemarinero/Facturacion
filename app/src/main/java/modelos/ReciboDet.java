package modelos;

/**
 * Created by jemarinero on 4/12/2017.
 */

public class ReciboDet {
    public String SERVICIO;
    public String CANTIDAD;
    public String PRECIO;
    public String IMPUESTO;
    public String MONTO;
    public String ID;

    public ReciboDet(String servicio, String cantidad,String precio,String impuesto,String monto,String id) {
        this.SERVICIO = servicio;
        this.CANTIDAD = cantidad;
        this.PRECIO = precio;
        this.IMPUESTO = impuesto;
        this.MONTO = monto;
        this.ID = id;
    }
}
