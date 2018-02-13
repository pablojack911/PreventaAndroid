package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 9/04/14.
 */
public class TablaXmlRutaVenta extends TablaXml {

    public TablaXmlRutaVenta()
    {
        this.NombreTabla = "RutaVentas";
        this.NombreTablaTagXml = "rutaVentas";

        this.claves.add("idRutaVenta");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idRutaVenta","idRutaVenta",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lun","lun",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("mar","mar",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("mie","mie",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("jue","jue",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("vie","vie",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("sab","sab",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("dom","dom",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idVendedor","vendedor",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idSuplente","suplente",String.class));
    }

}
