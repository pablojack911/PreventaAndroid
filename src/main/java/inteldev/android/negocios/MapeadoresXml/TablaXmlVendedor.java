package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 9/04/14.
 */
public class TablaXmlVendedor extends TablaXml {

    public TablaXmlVendedor()
    {
        this.NombreTabla = "Vendedores";
        this.NombreTablaTagXml = "vendedores";

        this.claves.add("idVendedor");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idVendedor","idVendedor",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("usuario","usuario",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("pass","pass",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("autoventa","autoventa",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("sucursal","sucursal",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));
    }

}
