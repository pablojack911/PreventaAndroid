package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 9/04/14.
 */
public class RutaVenta extends BaseModel {

    public String idRutaVenta;
    public int lun;
    public int mar;
    public int mie;
    public int jue;
    public int vie;
    public int sab;
    public int dom;
    public Date lastUpdate;
    public int borrado;
    public String vendedor;
    public String suplente;

    public RutaVenta() {
        this.setKey("RutaVenta");
    }
}
