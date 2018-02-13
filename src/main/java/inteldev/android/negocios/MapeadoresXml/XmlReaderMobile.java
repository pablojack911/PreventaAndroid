package inteldev.android.negocios.MapeadoresXml;

import android.content.ContentValues;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.lang.reflect.*;
import java.text.SimpleDateFormat;

import inteldev.android.accesoadatos.Dao;

/**
 * Created by Operador on 18/03/14.
 */
public class XmlReaderMobile extends XmlReader {

    private Dao controladorDb;
    public List<TablaXml> tablas = new ArrayList<TablaXml>();
    private TablaXml tablaActual;
    private ContentValues contentValues = null;

    public XmlReaderMobile(XmlPullParser xmlPullParser,Dao controladorDb)
    {
        super(xmlPullParser);
        this.controladorDb = controladorDb;
    }

    private Boolean leyendoRegistro;
    @Override
    public void StartTag(String tag)
    {
        if(tablaActual == null || (!this.leyendoRegistro && tablaActual.NombreTablaTagXml !=tag) )
        {
            SetearTablaActual(tag);
            CrearContentValue();
            leyendoRegistro=true;
        }
        else
        {
        /*    if (tablaActual.Nombre.equals(tag))
            {
                CrearContentValue();
            }*/

            SetearPropiedad(tag);
        }
    }

    private void CrearContentValue()
    {
        this.contentValues  = new ContentValues();
    }

    private void SetearPropiedad(String tag)
    {
        String campo=null;
        Type tipo=null;
        Object valor;

        Iterator<TagXmlCampoTabla> iterator = tablaActual.tagXmlCampoTablas.iterator();

        while (iterator.hasNext())
        {
            TagXmlCampoTabla tagXmlCampoTabla = iterator.next();
            if (tagXmlCampoTabla.Tag.equals(tag))
            {
                campo = tagXmlCampoTabla.Campo;
                tipo = tagXmlCampoTabla.Tipo;
            }
        }
        if (campo!=null && tipo !=null)
            ObtenerValor(campo, tipo);
    }


    private void ObtenerValor(String campo, Type tipo)
    {
        String valorText;
        try

        {
            valorText = xmlPullParser.nextText().trim();

            if(tipo.toString().equals(String.class.toString()))
            {
                if (this.tablaActual.NombreTabla.toString() == "Stock" &&  campo=="idArticulo") {
                    String epa = "sss";
                    if (epa == "dssd"){

                    }
                }

                this.contentValues.put(campo,valorText);
            }
            else
            {
                if (tipo.toString().equals(Integer.class.toString()))
                {
                    this.contentValues.put(campo,Integer.valueOf(valorText));
                }
                else
                {
                    if (tipo.toString().equals(Float.class.toString()))
                    {
                        this.contentValues.put(campo,Float.valueOf(valorText+"f"));
                    }
                    else
                    {
                        if (tipo.toString().equals(Date.class.toString()))
                        {
                            SimpleDateFormat parser;

                            if(valorText.length() == 8)
                            {
                                parser = new SimpleDateFormat("yyyyMMdd");
                            }
                            else {
                                Character caracter = valorText.toString().charAt(19);

                                if (caracter.toString().equals("-")) {
                                    parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-SSS");
                                } else {
                                    parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                }
                            }

                            Date date = parser.parse(valorText);

                            this.contentValues.put(campo, parser.format(date));
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            Log.e("Eror date",ex.getMessage().toString());

        }
    }

    private void SetearTablaActual(String tag)
    {
        Iterator<TablaXml> iterator = tablas.iterator();
        while(iterator.hasNext())
        {
            TablaXml tablaXml = iterator.next();

            if (tablaXml.NombreTablaTagXml.equals(tag))
            {
                tablaActual = tablaXml;
                break;
            }
        }
    }

    @Override
    public void EndTag(String tag)
    {
        if (this.tablaActual != null &&  this.tablaActual.NombreTablaTagXml.equals(tag))
        {
            this.leyendoRegistro=false;

            // Grabar

            String condicion = controladorDb.getWhere(this.contentValues,this.tablaActual.claves);

            String borrado = this.contentValues.getAsString("borrado");

            if (borrado != null && borrado.equals("1"))
            {
                controladorDb.delete(this.tablaActual.NombreTabla, condicion);
            }
            else {
                if (controladorDb.codeExists(this.tablaActual.NombreTabla, condicion)) {
                    controladorDb.update(this.tablaActual.NombreTabla, this.contentValues, condicion);
                } else {
                    controladorDb.insert(this.tablaActual.NombreTabla, this.contentValues);
                }
            }

            this.contentValues=null;
        }
    }

}
