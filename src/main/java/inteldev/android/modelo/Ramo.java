package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 9/04/14.
 */
public class Ramo extends BaseModel {

    public String idRamo;
    public String Nombre;
    public Date lastUpdate;
    public int Borrado;

    public Ramo() {
        this.setKey("Ramo");
    }

}
