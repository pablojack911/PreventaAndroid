package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 9/04/14.
 */
public class TablaXmlCondVta extends TablaXml{

    public TablaXmlCondVta()
    {
        super();
        this.NombreTabla = "CondVtas";
        this.NombreTablaTagXml = "condvtas";

        this.claves.add("idCondVta");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idCondVta","idCondVta",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("nombre","nombre",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("contado","contado",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));
    }
}
