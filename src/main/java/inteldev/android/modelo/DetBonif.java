package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 11/04/14.
 */
public class DetBonif extends BaseModel {

    public int idCabBonif;
    public String idRubro;
    public String idLinea;
    public String idArticulo;
    public float porcentaje;
    public int cant_sc;
    public float importe;
    public Date lastUpdate;
    public int borrado;

    public DetBonif() {
        this.setKey("idCabBonif");
    }

}
