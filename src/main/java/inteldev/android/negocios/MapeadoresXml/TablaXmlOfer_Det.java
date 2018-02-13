package inteldev.android.negocios.MapeadoresXml;

/**
 * Created by Operador on 11/04/14.
 */
public class TablaXmlOfer_Det extends TablaXml {

    public TablaXmlOfer_Det()
    {
        this.NombreTabla = "ofer_Det";
        this.NombreTablaTagXml = "ofer_det";

        this.claves.add("codigo");
        this.claves.add("tipo");
        this.claves.add("linea");
        this.claves.add("rubro");
        this.claves.add("articulo");

        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("codigo","codigo",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("tipo","tipo",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("linea","linea",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("rubro","rubro",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("articulo","articulo",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("descuento1","descuento1",Float.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("descuento2","descuento2",Float.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("descuento3","descuento3",Float.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("descuento4","descuento4",Float.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("cpra_oblig","cpra_oblig",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("descrip","descrip",String.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("d_cant","d_cant",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("h_cant","h_cant",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("multiplo","multiplo",Integer.class));
        this.tagXmlCampoTablas.add( new TagXmlCampoTabla("borrado","borrado",Integer.class));

    }
}
