package inteldev.android.negocios.MapeadoresXml;

import java.lang.reflect.Type;

/**
 * Created by gabriel on 13/03/14.
 */
public class TagXmlCampoTabla {

    public String Tag, Campo ;
    public Type  Tipo;

    public TagXmlCampoTabla(String tag, String campo, Type tipo){
        this.Tag = tag;
        this.Campo = campo;
        this.Tipo = tipo;
    }
}
