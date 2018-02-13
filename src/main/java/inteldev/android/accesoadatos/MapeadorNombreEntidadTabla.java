package inteldev.android.accesoadatos;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Operador on 8/04/14.
 */
public class MapeadorNombreEntidadTabla {

    private static MapeadorNombreEntidadTabla instancia;

    public Map<String, String> map;

    private MapeadorNombreEntidadTabla() {
        this.map = new HashMap<String, String>();

        map.put("Articulo", "Articulos");
        map.put("Cliente", "Clientes");
        map.put("CabLista", "CabListas");
        map.put("ConfigRuta", "ConfigRutas");
        map.put("PosicionGPS", "PosicionesGPS");
    }

    public static MapeadorNombreEntidadTabla getInstancia() {
        if (instancia == null) {
            instancia = new MapeadorNombreEntidadTabla();
        }
        return instancia;
    }

    public String getTabla(String entidad) {
        String tabla = map.get(entidad);

        return tabla;
    }


}
