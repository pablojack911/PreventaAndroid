package inteldev.android.modelo;

import java.util.Date;

/**
 * Articulo
 *
 * @author Gabriel Verdi
 * @version 07 march of 2014
 */
public class Articulo extends BaseModel {

    public String idArticulo;
    public String idRubro;
    public String idLinea;
    public String nombre;
    public int tasaIva;
    public int suspendido;
    public int unidadVenta;
    public int minimoVenta;
    public int multiploVenta;
    public int tasaIIBB;
    public int borrado;
    public int usaCanal;
    public float impInternos;
    public Date lastUpdate;

    public Articulo() {
        this.setKey("idArticulo");
    }
}
