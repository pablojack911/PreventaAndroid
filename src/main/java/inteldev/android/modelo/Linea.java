package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 9/04/14.
 */
public class Linea extends BaseModel {

    public String idLinea;
    public String nombre;
    public int linea;
    public Date lastUpdate;
    public int borrado;

    public Linea() {
        this.setKey("idLinea");
    }
}
