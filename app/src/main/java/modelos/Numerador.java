package modelos;

/**
 * Created by jemarinero on 1/12/2017.
 */

public class Numerador {
    public String ID;
    public String NUMERADOR;
    public String SERIE;
    public String NUMERO_INICIO;
    public String NUMERO_FIN;
    public String FECHA_INICIO;
    public String FECHA_FIN;
    public String CAI;
    public String ULTIMO_USADO;
    public String ESTADO;

    public Numerador(String numerador, String serie, String ultimo,String id) {
        this.NUMERADOR = numerador;
        this.SERIE = serie;
        this.ULTIMO_USADO = ultimo;
        this.ID = id;
    }

    public Numerador(String ID, String NUMERADOR, String SERIE, String NUMERO_INICIO, String NUMERO_FIN, String FECHA_INICIO, String FECHA_FIN, String CAI, String ULTIMO_USADO, String ESTADO) {
        this.ID = ID;
        this.NUMERADOR = NUMERADOR;
        this.SERIE = SERIE;
        this.NUMERO_INICIO = NUMERO_INICIO;
        this.NUMERO_FIN = NUMERO_FIN;
        this.FECHA_INICIO = FECHA_INICIO;
        this.FECHA_FIN = FECHA_FIN;
        this.CAI = CAI;
        this.ULTIMO_USADO = ULTIMO_USADO;
        this.ESTADO = ESTADO;
    }
}
