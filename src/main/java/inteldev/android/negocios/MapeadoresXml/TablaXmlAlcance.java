package inteldev.android.negocios.MapeadoresXml;

import java.util.Date;

/**
 * Created by Operador on 11/04/14.
 */
public class TablaXmlAlcance extends TablaXml {

    public TablaXmlAlcance()
    {
        this.NombreTabla  = "Alcance";
        this.NombreTablaTagXml = "alcance";

        this.claves.add("idCabBonif");
        this.claves.add("idCliente");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idCabBonif","idCabBonif",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lastUpdate","lastUpdate",Date.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("idcliente","idCliente",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));

    }

}
