package modelos;

/**
 * Created by jemarinero on 6/12/2017.
 */

public class Empresa {
    public String ID;
    public String NOMBRE;
    public String DIRECCION ;
    public String RTN;
    public String TELEFONO;
    public String CORREO;

    public Empresa(String ID, String NOMBRE, String DIRECCION, String RTN, String TELEFONO, String CORREO) {
        this.ID = ID;
        this.NOMBRE = NOMBRE;
        this.DIRECCION = DIRECCION;
        this.RTN = RTN;
        this.TELEFONO = TELEFONO;
        this.CORREO = CORREO;
    }
}
