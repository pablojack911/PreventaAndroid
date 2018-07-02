package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 8/04/14.
 */
public class TablaXmlDetLista extends TablaXml
{

    public TablaXmlDetLista()
    {
        super();

        this.NombreTabla = "detListas";
        this.NombreTablaTagXml = "detListas";

        this.claves.add("idLista");
        this.claves.add("articulo");

        this.tagXmlCampoTablas.add(new TagXmlCampoTabla("IdLista", "idLista", String.class));
        this.tagXmlCampoTablas.add(new TagXmlCampoTabla("precio", "precio", String.class));
        this.tagXmlCampoTablas.add(new TagXmlCampoTabla("folder", "folder", String.class));
        this.tagXmlCampoTablas.add(new TagXmlCampoTabla("articulo", "articulo", String.class));
        this.tagXmlCampoTablas.add(new TagXmlCampoTabla("lastUpdate", "lastUpdate", Date.class));
        this.tagXmlCampoTablas.add(new TagXmlCampoTabla("borrado", "borrado", Integer.class));
        this.tagXmlCampoTablas.add(new TagXmlCampoTabla("descuento", "descuento", Float.class));
    }

}
