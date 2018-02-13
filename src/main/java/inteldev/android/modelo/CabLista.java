package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 7/04/14.
 */
public class CabLista extends BaseModel {

    public String idLista;
    public String Nombre;
    public Date fvigencia;
    public Date lastUpdate;
    public int borrado;

    public CabLista() {
        this.setKey("idLista");
    }
}
