package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 11/04/14.
 */
public class Alcance extends BaseModel {

    public int idCabBonif;
    public Date lastUpdate;
    public int borrado;
    public String idCliente;

    public Alcance() {
        this.setKey("Alcance");
    }

}
