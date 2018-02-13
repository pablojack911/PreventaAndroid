package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 11/04/14.
 */
public class TablaXmlStock extends TablaXml {

    public TablaXmlStock()
    {
        this.NombreTabla = "Stock";
        this.NombreTablaTagXml = "stock";

      //  this.claves.add("fecha");
        this.claves.add("idArticulo");

      //  this.tagXmlCampoTablas.add( new TagXmlCampoTabla("fecha","fecha",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idvendedor","idVendedor",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idarticulo","idArticulo",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("bultos","bultos",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("unidades","unidades",Integer.class));
    }
}
