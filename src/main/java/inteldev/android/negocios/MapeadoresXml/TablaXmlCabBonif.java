package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 11/04/14.
 */
public class TablaXmlCabBonif extends TablaXml {

    public TablaXmlCabBonif()
    {
        this.NombreTabla = "cabBonif";
        this.NombreTablaTagXml = "cabBonif";

        this.claves.add("idCabBonif");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idCabBonif","idCabBonif",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("tipo","tipo",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("fdesde","fdesde",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("fhasta","fhasta",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("suspendido","suspendido",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("sinDescBase","sindescbase",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("permiteoe","permiteoe",Integer.class));

    }
}
