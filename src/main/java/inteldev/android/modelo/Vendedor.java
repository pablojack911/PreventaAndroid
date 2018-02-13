package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 9/04/14.
 */
public class Vendedor extends BaseModel {

    public String idVendedor;
    public String usuario;
    public String pass;
    public int autoventa;
    public String sucursal;
    public Date lastUpdate;
    public int borrado;

    public Vendedor() {
        this.setKey("Vendedor");
    }
}
