package inteldev.android.negocios.MapeadoresXml;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabriel on 13/03/14.
 */
public class TablaXml {
    public String NombreTabla;
    public ArrayList<String> claves;
    public String NombreTablaTagXml;

    public List<TagXmlCampoTabla> tagXmlCampoTablas;

    public TablaXml()
    {
        this.tagXmlCampoTablas = new ArrayList<TagXmlCampoTabla>();
        this.claves = new ArrayList<String>();
    }
}
