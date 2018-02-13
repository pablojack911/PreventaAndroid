package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 9/04/14.
 */
public class TablaXmlRamo extends TablaXml {

    public TablaXmlRamo()
    {
        this.NombreTabla = "Ramos";
        this.NombreTablaTagXml = "ramos";

        this.claves.add("idRamo");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idRamo","idRamo",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("nombre","nombre",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));
    }
}
