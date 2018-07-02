package inteldev.android.negocios.MapeadoresXml;

import android.content.ContentValues;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import inteldev.android.accesoadatos.Dao;

/**
 * Created by Operador on 18/03/14.
 */
public class XmlReaderMobile extends XmlReader
{

    public List<TablaXml> tablas = new ArrayList<TablaXml>();
    private Dao controladorDb;
    private TablaXml tablaActual;
    private ContentValues contentValues = null;
    private Boolean leyendoRegistro;

    public XmlReaderMobile(XmlPullParser xmlPullParser, Dao controladorDb)
    {
        super(xmlPullParser);
        this.controladorDb = controladorDb;
    }

    @Override
    public void StartTag(String tag)
    {
        if (tablaActual == null || (!this.leyendoRegistro && tablaActual.NombreTablaTagXml != tag))
        {
            boolean ok = SetearTablaActual(tag);
            if(ok)
            {
                CrearContentValue();
                leyendoRegistro = true;
            }
        }
        else
        {
            SetearPropiedad(tag);
        }
    }

    private void CrearContentValue()
    {
        this.contentValues = new ContentValues();
    }

    private void SetearPropiedad(String tag)
    {
        Log.d("SetearPropiedad", tag);
        String campo = null;
        Type tipo = null;
        Object valor;

        for (TagXmlCampoTabla tagXmlCampoTabla : tablaActual.tagXmlCampoTablas)
        {
            if (tagXmlCampoTabla.Tag.equals(tag))
            {
                campo = tagXmlCampoTabla.Campo;
                tipo = tagXmlCampoTabla.Tipo;
                break;
            }
        }
        if (campo != null && tipo != null)
        {
            ObtenerValor(campo, tipo);
        }
        else
        {
            Log.e("SetearPropiedad", "Campo o Tipo es null. Tag=" + tag);
        }
    }


    private void ObtenerValor(String campo, Type tipo)
    {
        String valorText;
        Log.d("ObtenerValor", campo);
        try

        {
            valorText = xmlPullParser.nextText().trim();

            if (tipo.toString().equals(String.class.toString()))
            {
                this.contentValues.put(campo, valorText);
            }
            else
            {
                if (tipo.toString().equals(Integer.class.toString()))
                {
                    this.contentValues.put(campo, Integer.valueOf(valorText));
                }
                else
                {
                    if (tipo.toString().equals(Float.class.toString()))
                    {
                        this.contentValues.put(campo, Float.valueOf(valorText + "f"));
                    }
                    else
                    {
                        if (tipo.toString().equals(Date.class.toString()))
                        {
                            SimpleDateFormat parser;

                            if (valorText.length() == 8)
                            {
                                parser = new SimpleDateFormat("yyyyMMdd");
                            }
                            else
                            {
                                Character caracter = valorText.toString().charAt(19);

                                if (caracter.toString().equals("-"))
                                {
                                    parser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-SSS");
                                }
                                else
                                {
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
//        catch ()
//        {
//            Log.e("Error date", ex.getMessage().toString());
//
//        }
        catch (ParseException e)
        {
            Log.e("ObtenerValor", "Parser exception " + e.getLocalizedMessage());
        }
        catch (XmlPullParserException e)
        {
            Log.e("ObtenerValor", "XmlPullParserException " + e.getLocalizedMessage());
        }
        catch (IOException e)
        {
            Log.e("ObtenerValor", "IOException " + e.getLocalizedMessage());
        }
    }

    private boolean SetearTablaActual(String tag)
    {
        Log.d("XMLReaderMobile", tag);
        for (TablaXml tablaXml : tablas)
        {
            if (tablaXml.NombreTablaTagXml.equals(tag))
            {
                tablaActual = tablaXml;
                return true;
            }
        }
        return false;
    }

    @Override
    public void EndTag(String tag)
    {
        Log.d("EndTag", tag);
        if (this.tablaActual != null && this.tablaActual.NombreTablaTagXml.equals(tag))
        {
            this.leyendoRegistro = false;

            // Grabar

//            String condicion = controladorDb.getWhere(this.contentValues, this.tablaActual.claves);
            String where = controladorDb.getWhere(this.tablaActual.claves);
            String[] args = controladorDb.getArgs(this.contentValues, this.tablaActual.claves);

            String borrado = this.contentValues.getAsString("borrado");

            if (borrado != null && borrado.equals("1"))
            {
                long res = controladorDb.delete(this.tablaActual.NombreTabla, where, args);
                Log.d("Borrando", this.tablaActual + " -> " + res + " lineas." + args.toString());
            }
            else
            {
                if (controladorDb.codeExists(this.tablaActual.NombreTabla, where, args))
                {
                    controladorDb.update(this.tablaActual.NombreTabla, this.contentValues, where, args);
                }
                else
                {
                    controladorDb.insert(this.tablaActual.NombreTabla, this.contentValues);
                }
            }
            this.contentValues = null;
        }
    }

}
