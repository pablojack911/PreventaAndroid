package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 9/04/14.
 */
public class TablaXmlLinea extends TablaXml {

    public TablaXmlLinea()
    {
        this.NombreTabla = "Lineas";
        this.NombreTablaTagXml = "lineas";

        this.claves.add("idLinea");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idLinea","idLinea",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("nombre","nombre",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("empresa","empresa",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));
    }
}
