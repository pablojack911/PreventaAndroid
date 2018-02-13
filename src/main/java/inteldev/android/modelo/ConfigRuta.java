package inteldev.android.modelo;

import java.util.Date;

/**
 * Created by Operador on 8/04/14.
 */
public class ConfigRuta extends BaseModel {

    public String idRutaVenta;
    public String idCliente;
    public Date lastUpdate;
    public int borrado;
    public int recorrido;

    public ConfigRuta() {
        this.setKey("idRutaVenta");
    }

}
