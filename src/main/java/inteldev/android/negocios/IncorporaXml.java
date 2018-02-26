package inteldev.android.negocios;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import inteldev.android.accesoadatos.Dao;
import inteldev.android.negocios.MapeadoresXml.TablaXmlAlcance;
import inteldev.android.negocios.MapeadoresXml.TablaXmlArticulo;
import inteldev.android.negocios.MapeadoresXml.TablaXmlBarras;
import inteldev.android.negocios.MapeadoresXml.TablaXmlCabBonif;
import inteldev.android.negocios.MapeadoresXml.TablaXmlCabLista;
import inteldev.android.negocios.MapeadoresXml.TablaXmlCliente;
import inteldev.android.negocios.MapeadoresXml.TablaXmlCondVta;
import inteldev.android.negocios.MapeadoresXml.TablaXmlConfigRuta;
import inteldev.android.negocios.MapeadoresXml.TablaXmlDetBonif;
import inteldev.android.negocios.MapeadoresXml.TablaXmlDetLista;
import inteldev.android.negocios.MapeadoresXml.TablaXmlLinea;
import inteldev.android.negocios.MapeadoresXml.TablaXmlOferEsp;
import inteldev.android.negocios.MapeadoresXml.TablaXmlOfer_Det;
import inteldev.android.negocios.MapeadoresXml.TablaXmlRamo;
import inteldev.android.negocios.MapeadoresXml.TablaXmlRubro;
import inteldev.android.negocios.MapeadoresXml.TablaXmlRutaVenta;
import inteldev.android.negocios.MapeadoresXml.TablaXmlStock;
import inteldev.android.negocios.MapeadoresXml.TablaXmlVendedor;
import inteldev.android.negocios.MapeadoresXml.XmlReaderMobile;
//import inteldev.android.negocios.xmlParser.DatosMobileXMLParser;

/**
 * Created by Operador on 25/04/14.
 */
public class IncorporaXml
{
    public void incorporar(Context context)
    {
        try
        {

//            DatosMobileXMLParser datosMobileXMLParser = new DatosMobileXMLParser();

            String directory = Environment.getExternalStorageDirectory().toString();

            Dao controladorDb = new Dao(context);

            // datosMobile1
            String ubicacion = directory + "/" + Environment.DIRECTORY_DOWNLOADS + "/InteldevMobile.xml";

            File xml = new File(ubicacion);
            FileInputStream fin = new FileInputStream(xml);
            InputStreamReader in = new InputStreamReader(fin);
            BufferedReader bufferedReader = new BufferedReader(in);

            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(bufferedReader);

            XmlReaderMobile xmlReaderMobile = new XmlReaderMobile(xmlPullParser, controladorDb);

            // datosMobile2
            String ubicacion2 = directory + "/" + Environment.DIRECTORY_DOWNLOADS + "/InteldevMobile2.xml";

            File xml2 = new File(ubicacion2);
            FileInputStream fin2 = new FileInputStream(xml2);
            InputStreamReader in2 = new InputStreamReader(fin2);
            BufferedReader bufferedReader2 = new BufferedReader(in2);

            XmlPullParser xmlPullParser2 = Xml.newPullParser();
            xmlPullParser2.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser2.setInput(bufferedReader2);

            XmlReaderMobile xmlReaderMobile2 = new XmlReaderMobile(xmlPullParser2, controladorDb);

            TablaXmlArticulo tablaXmlArticulo = new TablaXmlArticulo();
            TablaXmlCabLista tablaXmlCabListas = new TablaXmlCabLista();
            TablaXmlCliente tablaXmlCliente = new TablaXmlCliente();
            TablaXmlCondVta tablaXmlCondVta = new TablaXmlCondVta();
            TablaXmlConfigRuta tablaXmlConfigRuta = new TablaXmlConfigRuta();
            TablaXmlDetLista tablaXmlDetLista = new TablaXmlDetLista();
            TablaXmlLinea tablaXmlLinea = new TablaXmlLinea();
            TablaXmlRamo tablaXmlRamo = new TablaXmlRamo();
            TablaXmlRubro tablaXmlRubro = new TablaXmlRubro();
            TablaXmlRutaVenta tablaXmlRutaVenta = new TablaXmlRutaVenta();
            TablaXmlVendedor tablaXmlVendedor = new TablaXmlVendedor();
            TablaXmlOferEsp tablaXmlOferEsp = new TablaXmlOferEsp();
            TablaXmlOfer_Det tablaXmlOfer_det = new TablaXmlOfer_Det();
            TablaXmlCabBonif tablaXmlCabBonif = new TablaXmlCabBonif();
            TablaXmlDetBonif tablaXmlDetBonif = new TablaXmlDetBonif();
            TablaXmlAlcance tablaXmlAlcance = new TablaXmlAlcance();
            TablaXmlStock tablaXmlStock = new TablaXmlStock();
            TablaXmlBarras tablaXmlBarras = new TablaXmlBarras();

            xmlReaderMobile.tablas.add(tablaXmlArticulo);
            xmlReaderMobile.tablas.add(tablaXmlCabListas);
            xmlReaderMobile.tablas.add(tablaXmlCliente);
            xmlReaderMobile.tablas.add(tablaXmlCondVta);
            xmlReaderMobile.tablas.add(tablaXmlConfigRuta);
            xmlReaderMobile.tablas.add(tablaXmlDetLista);
            xmlReaderMobile.tablas.add(tablaXmlLinea);
            xmlReaderMobile.tablas.add(tablaXmlRamo);
            xmlReaderMobile.tablas.add(tablaXmlRubro);
            xmlReaderMobile.tablas.add(tablaXmlRutaVenta);
            xmlReaderMobile.tablas.add(tablaXmlVendedor);
            xmlReaderMobile.tablas.add(tablaXmlBarras);


            xmlReaderMobile2.tablas.add(tablaXmlOferEsp);

            xmlReaderMobile2.tablas.add(tablaXmlOfer_det);
            xmlReaderMobile2.tablas.add(tablaXmlCabBonif);
            xmlReaderMobile2.tablas.add(tablaXmlDetBonif);
            xmlReaderMobile2.tablas.add(tablaXmlAlcance);
            xmlReaderMobile2.tablas.add(tablaXmlStock);

            xmlReaderMobile.Read();
            xmlReaderMobile2.Read();


        }
        catch (Exception e)
        {
            Log.d("IncorporaXml", e.getMessage());
        }


    }

}
