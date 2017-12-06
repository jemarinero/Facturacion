package modelos;

/**
 * Created by jemarinero on 25/11/2017.
 */

public class Cliente {
    public String ID;
    public String NOMBRE;
    public String DIRECCION;
    public String TELEFONO;
    public String CORREO;
    public String IDENTIDAD;
    public String RTN;
    public String LATITUD;
    public String LONGITUD;
    public String EXENTO_IMPUESTO;


    public Cliente(String nombre, String telefono,String id) {
        this.NOMBRE = nombre;
        this.TELEFONO = telefono;
        this.ID = id;
    }

    public String toString()
    {
        return( this.NOMBRE);
    }

    public Cliente(String ID, String NOMBRE, String DIRECCION, String TELEFONO, String CORREO, String IDENTIDAD, String RTN, String LATITUD, String LONGITUD, String EXENTO_IMPUESTO) {
        this.ID = ID;
        this.NOMBRE = NOMBRE;
        this.DIRECCION = DIRECCION;
        this.TELEFONO = TELEFONO;
        this.CORREO = CORREO;
        this.IDENTIDAD = IDENTIDAD;
        this.RTN = RTN;
        this.LATITUD = LATITUD;
        this.LONGITUD = LONGITUD;
        this.EXENTO_IMPUESTO = EXENTO_IMPUESTO;
    }
}
