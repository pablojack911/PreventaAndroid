package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 9/04/14.
 */
public class CondVta extends BaseModel {

    public String idCondVta;
    public String nombre;
    public int contado;
    public Date lastUpdate;
    public int borrado;

    public CondVta() {
        this.setKey("idCondVta");
    }
}
