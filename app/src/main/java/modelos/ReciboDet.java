package modelos;

/**
 * Created by jemarinero on 4/12/2017.
 */

public class ReciboDet {
    public String ID;
    public String ID_RECIBO;
    public String ID_SERVICIO;
    public String CANTIDAD;
    public String PRECIO;
    public String IMPUESTO;
    public String TOTAL;

    public ReciboDet(String servicio, String cantidad,String precio,String impuesto,String monto,String id) {
        this.ID_SERVICIO = servicio;
        this.CANTIDAD = cantidad;
        this.PRECIO = precio;
        this.IMPUESTO = impuesto;
        this.TOTAL = monto;
        this.ID = id;
    }

    public ReciboDet(String ID, String ID_RECIBO, String ID_SERVICIO, String CANTIDAD, String PRECIO, String IMPUESTO, String TOTAL) {
        this.ID = ID;
        this.ID_RECIBO = ID_RECIBO;
        this.ID_SERVICIO = ID_SERVICIO;
        this.CANTIDAD = CANTIDAD;
        this.PRECIO = PRECIO;
        this.IMPUESTO = IMPUESTO;
        this.TOTAL = TOTAL;
    }
}
