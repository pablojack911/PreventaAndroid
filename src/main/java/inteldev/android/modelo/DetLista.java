package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 8/04/14.
 */
public class DetLista extends BaseModel {

    public String idLista;
    public float precio;
    public String articulo;
    public Date lastUpdate;
    public int borrado;
    public float descuento;

    public DetLista() {
        this.setKey("idLista");
    }

}
