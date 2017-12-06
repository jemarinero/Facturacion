package modelos;

/**
 * Created by jemarinero on 2/12/2017.
 */

public class Recibo {
    public String ID;
    public String NO_RECIBO;
    public String FECHA;
    public String CAI;
    public String CLIENTE;
    public String ESTADO;
    public String LATITUD;
    public String LONGITUD;
    public String MONTO;

    public Recibo(String noRecibo, String cliente, String monto,String id,String estado) {
        this.NO_RECIBO = noRecibo;
        this.CLIENTE = cliente;
        this.MONTO = monto;
        this.ESTADO = estado;
        this.ID = id;
    }

    public Recibo(String ID, String NO_RECIBO, String FECHA, String CAI, String CLIENTE, String ESTADO, String LATITUD, String LONGITUD) {
        this.ID = ID;
        this.NO_RECIBO = NO_RECIBO;
        this.FECHA = FECHA;
        this.CAI = CAI;
        this.CLIENTE = CLIENTE;
        this.ESTADO = ESTADO;
        this.LATITUD = LATITUD;
        this.LONGITUD = LONGITUD;
    }
}
