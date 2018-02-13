package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 7/04/14.
 */
public class TablaXmlCabLista extends TablaXml{

    public TablaXmlCabLista()
    {
        super();

        this.NombreTabla = "cabListas";
        this.NombreTablaTagXml = "cabListas";

        this.claves.add("IdLista");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("IdLista","IdLista",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("nombre","nombre",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("fvigencia","fvigencia",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));

    }
}
