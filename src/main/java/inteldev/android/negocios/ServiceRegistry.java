package inteldev.android.negocios;

import android.app.Activity;
import android.content.Context;

import inteldev.android.presentation.MainActivity;

/**
 * ServiceRegistry
 *
 * @author Gabriel Verdi 06 march 2014
 */
public class ServiceRegistry
{

//    public static String direccionWSLocal = "http://192.168.1.99:8888/";
    public static String direccionWSLocal = "http://websrv:8888/";
    public static String direccionWSRemoto = "http://mhergo.ddns.net:8888/";
    public static String nombreWS = "inteldevwebservice/Service.asmx?WSDL?date=today&timestamp=ddmmyyhhmmss";
    private WebServiceHelper webServiceHelper;

    public ServiceRegistry(Context context, String loginUsuario)
    {
        webServiceHelper = new WebServiceHelper();


        //bajar datos mobile Local
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "bajarDatosMobile3parte1", direccionWSLocal + nombreWS, "http://www.inteldevmobile.com.ar/bajarDatosMobile3parte1", "bajarDatosMobile", 0);
        webServiceHelper.addMethodParameter("bajarDatosMobile", "usuario", loginUsuario);
        webServiceHelper.addMethodParameter("bajarDatosMobile", "clave", MainActivity.loginPass);
        webServiceHelper.addMethodParameter("bajarDatosMobile", "imei", FabricaNegocios.obtenerImei(context));

        //bajar datos mobile Remoto
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "bajarDatosMobile3parte1", direccionWSRemoto + nombreWS, "http://www.inteldevmobile.com.ar/bajarDatosMobile3parte1", "bajarDatosMobileRemoto", 0);
        webServiceHelper.addMethodParameter("bajarDatosMobileRemoto", "usuario", loginUsuario);
        webServiceHelper.addMethodParameter("bajarDatosMobileRemoto", "clave", MainActivity.loginPass);
        webServiceHelper.addMethodParameter("bajarDatosMobileRemoto", "imei", FabricaNegocios.obtenerImei(context));

        //bajar datos mobile 2 Local
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "bajarDatosMobile3parte2", direccionWSLocal + nombreWS, "http://www.inteldevmobile.com.ar/bajarDatosMobile3parte2", "bajarDatosMobile2", 0);
        webServiceHelper.addMethodParameter("bajarDatosMobile2", "usuario", loginUsuario);
        webServiceHelper.addMethodParameter("bajarDatosMobile2", "clave", MainActivity.loginPass);
        webServiceHelper.addMethodParameter("bajarDatosMobile2", "imei", FabricaNegocios.obtenerImei(context));

        //bajar datos mobile 2 Remoto
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "bajarDatosMobile3parte2", direccionWSRemoto + nombreWS, "http://www.inteldevmobile.com.ar/bajarDatosMobile3parte2", "bajarDatosMobile2Remoto", 0);
        webServiceHelper.addMethodParameter("bajarDatosMobile2Remoto", "usuario", loginUsuario);
        webServiceHelper.addMethodParameter("bajarDatosMobile2Remoto", "clave", MainActivity.loginPass);
        webServiceHelper.addMethodParameter("bajarDatosMobile2Remoto", "imei", FabricaNegocios.obtenerImei(context));

        //actualizar fecha download Local
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "actualizaFechaDownLoad", direccionWSLocal + nombreWS, "http://www.inteldevmobile.com.ar/actualizaFechaDownLoad", "actualizaFechaDownload", 0);
        webServiceHelper.addMethodParameter("actualizaFechaDownload", "usuario", loginUsuario);
        webServiceHelper.addMethodParameter("actualizaFechaDownload", "clave", MainActivity.loginPass);
        webServiceHelper.addMethodParameter("actualizaFechaDownload", "imei", FabricaNegocios.obtenerImei(context));

        //actualizar fecha download Remoto
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "actualizaFechaDownLoad", direccionWSRemoto + nombreWS, "http://www.inteldevmobile.com.ar/actualizaFechaDownLoad", "actualizaFechaDownloadRemoto", 0);
        webServiceHelper.addMethodParameter("actualizaFechaDownloadRemoto", "usuario", loginUsuario);
        webServiceHelper.addMethodParameter("actualizaFechaDownloadRemoto", "clave", MainActivity.loginPass);
        webServiceHelper.addMethodParameter("actualizaFechaDownloadRemoto", "imei", FabricaNegocios.obtenerImei(context));

        //subir datos mobile 3 Local
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "subirDatosMobile3", direccionWSLocal + nombreWS, "http://www.inteldevmobile.com.ar/subirDatosMobile3", "subirDatosMobile3", 0);

        //subir datos mobile 3 Remoto
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "subirDatosMobile3", direccionWSRemoto + nombreWS, "http://www.inteldevmobile.com.ar/subirDatosMobile3", "subirDatosMobile3Remoto", 0);

        //Hola
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "hola", direccionWSLocal + nombreWS, "http://www.inteldevmobile.com.ar/hola", "hola", 5000);
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "hola", direccionWSRemoto + nombreWS, "http://www.inteldevmobile.com.ar/hola", "holaRemoto", 5000);
        //webServiceHelper.addService("http://www.inteldevmobile.com.ar","hola","http://hergo22.no-ip.org:8888/inteldevwebservice/Service.asmx?WSDL","http://www.inteldevmobile.com.ar/hola","holaCualquiera");
    }

    public WebServiceHelper getWebServiceHelper()
    {
        return webServiceHelper;
    }
}
