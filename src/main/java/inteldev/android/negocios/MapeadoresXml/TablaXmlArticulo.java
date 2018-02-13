package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by gabriel on 13/03/14.
 */
public class TablaXmlArticulo extends TablaXml{

    public TablaXmlArticulo(){

        super();
        this.NombreTabla = "Articulos";
        this.NombreTablaTagXml = "Articulos";

        this.claves.add("idArticulo");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idArticulo","idArticulo",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("tasaiva","tasaIva",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("descripcion","nombre",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("suspendido","suspendido",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("unidadVenta","unidadVenta",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("MinimoVenta","minimoVenta",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("MultiploVenta","multiploVenta",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idLinea","idLinea",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idRubro","idRubro",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate", Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("usacanal","usaCanal",Integer.class));
    }
}
