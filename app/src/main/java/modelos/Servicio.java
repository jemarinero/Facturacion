package modelos;

/**
 * Created by jemarinero on 30/11/2017.
 */

public class Servicio {
    public String ID;
    public String NOMBRE;
    public String PRECIO;
    public String IMPUESTO;


    public Servicio(String nombre, String precio,String id,String impuesto) {
        this.NOMBRE = nombre;
        this.PRECIO = precio;
        this.ID = id;
        this.IMPUESTO = impuesto;
    }

    public String toString()
    {
        return( this.NOMBRE);
    }
}
