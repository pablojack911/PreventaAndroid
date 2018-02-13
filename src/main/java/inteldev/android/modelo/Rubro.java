package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 9/04/14.
 */
public class Rubro extends BaseModel {

    public String idRubro;
    public String nombre;
    public Date lastUpdate;
    public int borrado;

    public Rubro() {
        this.setKey("idRubro");
    }
}
