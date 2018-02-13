package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 8/04/14.
 */
public class TablaXmlConfigRuta extends TablaXml {

    public TablaXmlConfigRuta()
    {
        this.NombreTabla = "configRutas";
        this.NombreTablaTagXml = "configRuta";

        this.claves.add("idRutaVenta");
        this.claves.add("idCliente");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idRutaVenta","idRutaVenta",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("IdCliente","idCliente",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("recorrido","recorrido",String.class));

    }
}
