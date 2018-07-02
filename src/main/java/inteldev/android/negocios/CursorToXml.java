package inteldev.android.negocios;

import android.database.Cursor;
import android.os.Environment;
import android.util.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Operador on 15/05/14.
 */
public class CursorToXml
{
    String nombreXml=null;
    DocumentBuilderFactory docFactory ;
    DocumentBuilder docBuilder;
    Document doc;
    Element elementoRaiz;
    int tipo;

    public CursorToXml(String nombreXml)
    {
        try {
            docFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docFactory.newDocumentBuilder();
        }
        catch (ParserConfigurationException p){}

        doc = docBuilder.newDocument();
        elementoRaiz = doc.createElement(nombreXml.toString());
        doc.appendChild(elementoRaiz);
    }

    public void AgregarTabla(Cursor cursor,String nombreTabla)
    {
        int i=0;

        if (cursor != null && cursor.moveToFirst()) {


            do
            {
                Element subElementoRaiz = doc.createElement(nombreTabla);
                elementoRaiz.appendChild(subElementoRaiz);

                for (i=0;i<cursor.getColumnCount();i++)
                {
                    Element campo = doc.createElement(cursor.getColumnName(i).toString());
                    int tipo = cursor.getType(i);
                    switch (tipo)
                    {
                        case 0:
                            campo.appendChild(doc.createTextNode(String.valueOf(cursor.getString(i))));
                            break;
                        case 1:
                            campo.appendChild(doc.createTextNode(String.valueOf(cursor.getInt(i))));
                            break;
                        case 2:
                            campo.appendChild(doc.createTextNode(String.valueOf(cursor.getFloat(i))));
                            break;
                        case 3:
                            if (cursor.getColumnName(i).toString().toLowerCase().contains("fecha"))
                            {
                                String fechaString = String.valueOf(cursor.getString(i));
//                                fechaString+="T00:00:00";
                                campo.appendChild(doc.createTextNode(fechaString));
                            }else
                                campo.appendChild(doc.createTextNode(String.valueOf(cursor.getString(i))));
                            {

                            }
                            break;
                    }

               //     campo.appendChild(doc.createTextNode(cursor.getString(i)));
                    subElementoRaiz.appendChild(campo);
                }

            } while(cursor.moveToNext());
        }
    }

    public String GenerarXml()
    {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();

        }catch (TransformerException t)
        {
            return null;
        }

    }







}
