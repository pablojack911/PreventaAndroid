package inteldev.android.negocios.MapeadoresXml;

import org.xmlpull.v1.XmlPullParser;

/**
 * Created by Operador on 18/03/14.
 */
public class XmlReader {

    XmlPullParser xmlPullParser;

    public XmlReader(XmlPullParser xmlPullParser)
    {
        this.xmlPullParser = xmlPullParser;
    }

    public void StartTag(String tag)
    {

    }

    public void EndTag(String tag)
    {

    }

    public void Read()
    {
        try
        {
            String etiqueta = null;
            String prueba = null;

            int evento = xmlPullParser.getEventType();

            while (evento != XmlPullParser.END_DOCUMENT)
            {
                switch (evento)
                    {
                        case XmlPullParser.START_DOCUMENT:

                            break;

                        case XmlPullParser.START_TAG:

                            etiqueta = xmlPullParser.getName();

                            StartTag(etiqueta);

                            break;

                        case XmlPullParser.END_TAG:

                            etiqueta = xmlPullParser.getName();

                            EndTag(etiqueta);

                            break;
                    }

                evento = xmlPullParser.next();
            }
         }
        catch (Exception e)
        {
            System.err.println("Caught exeption: " + e.getMessage());

        }
    }
}
