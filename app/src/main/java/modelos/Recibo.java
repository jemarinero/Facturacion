package modelos;

/**
 * Created by jemarinero on 2/12/2017.
 */

public class Recibo {
    public String NO_RECIBO;
    public String CLIENTE;
    public String MONTO;
    public String ID;

    public Recibo(String noRecibo, String cliente, String monto,String id) {
        this.NO_RECIBO = noRecibo;
        this.CLIENTE = cliente;
        this.MONTO = monto;
        this.ID = id;
    }
}
