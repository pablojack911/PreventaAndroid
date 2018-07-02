package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by pinsua on 16/02/2018.
 */

public class TablaXmlBarras extends TablaXml
{
    public TablaXmlBarras()
    {
        super();
        this.NombreTabla="Barras";
        this.NombreTablaTagXml="barras";

        this.claves.add("idArticulo");
        this.claves.add("barcode");
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("barcode","barcode",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastupdate","lastupdate", Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado", Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idArticulo","idArticulo", String.class));

    }
}
