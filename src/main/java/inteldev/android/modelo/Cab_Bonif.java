package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 11/04/14.
 */
public class Cab_Bonif extends BaseModel {

    public int idCabBonif;
    
    public int tipo;
    public Date fDesde;
    public Date fHasta;
    public int suspendido;
    public Date lastUpdate;
    public int borrado;
    public int sinDescBase;
    public int permiteOe;

    public Cab_Bonif() {
        this.setKey("Cab_Bonif");
    }

}
