package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 11/04/14.
 */
public class TablaXmlDetBonif extends TablaXml {

    public TablaXmlDetBonif()
    {
        this.NombreTabla = "detBonif";
        this.NombreTablaTagXml = "detBonif";

        this.claves.add("idCabBonif");
        this.claves.add("idRubro");
        this.claves.add("idLinea");
        this.claves.add("idArticulo");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idCabBonif","idCabBonif",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idRubro","idRubro",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idLinea","idLinea",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idRubro","idRubro",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idArticulo","idArticulo",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("porcentaje","porcentaje",Float.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("importe","importe",Float.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("cant_sc","cant_sc",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));
    }
}
