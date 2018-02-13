package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 9/04/14.
 */
public class TablaXmlRubro extends TablaXml {

    public TablaXmlRubro()
    {
        this.NombreTabla = "Rubros";
        this.NombreTablaTagXml = "rubros";

        this.claves.add("idRubro");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idRubro","idRubro",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("nombre","nombre",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));

    }
}
