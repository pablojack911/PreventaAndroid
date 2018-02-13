package inteldev.android.negocios.MapeadoresXml;

/**
 * Created by Operador on 11/04/14.
 */
public class TablaXmlOferEsp extends TablaXml {

    public TablaXmlOferEsp()
    {
        this.NombreTabla = "oferEsp";
        this.NombreTablaTagXml = "oferesp";

        this.claves.add("codigo");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("codigo","codigo",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("nombre","nombre",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("d_cant","d_cant",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("h_cant","h_cant",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("tipo","tipo",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("ctrlcant","ctrlcant",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("base_dcant","base_dcant",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("base_hcant","base_hcant",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("comb_oblig","comb_oblig",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("comb_cantb","comb_cantb",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("comb_cantc","comb_cantc",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("bon_cada","bon_cada",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("bon_tipo","bon_tipo",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("bon_cant","bon_cant",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("pfinal","pfinal",float.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("tomaconv","tomaconv",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("lista","lista",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));
    }
}
