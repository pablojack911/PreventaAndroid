package inteldev.android.negocios;

import android.content.Context;

/**
 * ServiceRegistry
 *
 * @author Gabriel Verdi 06 march 2014
 */
public class ServiceRegistry
{

    //    public static String direccionWSLocal = "http://192.168.1.99:8888/";
    public static String direccionWSLocal = "http://websrv:1138/";
    public static String direccionWSRemoto = "http://mhergo.ddns.net:1138/";
    public static String nombreWS = "preventa/Service.asmx?WSDL?date=today&timestamp=ddmmyyhhmmss";
    private WebServiceHelper webServiceHelper;

    public ServiceRegistry(Context context, String loginUsuario)
    {
        webServiceHelper = new WebServiceHelper();
        //bajar datos mobile Local
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "bajarDatosMobile4", direccionWSLocal + nombreWS, "http://www.inteldevmobile.com.ar/bajarDatosMobile4", "bajarDatosMobile", 0);
        webServiceHelper.addMethodParameter("bajarDatosMobile", "usuario", loginUsuario);
        webServiceHelper.addMethodParameter("bajarDatosMobile", "clave", "");
        webServiceHelper.addMethodParameter("bajarDatosMobile", "imei", SharedPreferencesManager.getImei(context));

        //bajar datos mobile Remoto
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "bajarDatosMobile4", direccionWSRemoto + nombreWS, "http://www.inteldevmobile.com.ar/bajarDatosMobile4", "bajarDatosMobileRemoto", 0);
        webServiceHelper.addMethodParameter("bajarDatosMobileRemoto", "usuario", loginUsuario);
        webServiceHelper.addMethodParameter("bajarDatosMobileRemoto", "clave", "");
        webServiceHelper.addMethodParameter("bajarDatosMobileRemoto", "imei", SharedPreferencesManager.getImei(context));

        //bajar datos mobile 2 Local
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "bajarDatosMobile3parte2", direccionWSLocal + nombreWS, "http://www.inteldevmobile.com.ar/bajarDatosMobile3parte2", "bajarDatosMobile2", 0);
        webServiceHelper.addMethodParameter("bajarDatosMobile2", "usuario", loginUsuario);
        webServiceHelper.addMethodParameter("bajarDatosMobile2", "clave", "");
        webServiceHelper.addMethodParameter("bajarDatosMobile2", "imei", SharedPreferencesManager.getImei(context));

        //bajar datos mobile 2 Remoto
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "bajarDatosMobile3parte2", direccionWSRemoto + nombreWS, "http://www.inteldevmobile.com.ar/bajarDatosMobile3parte2", "bajarDatosMobile2Remoto", 0);
        webServiceHelper.addMethodParameter("bajarDatosMobile2Remoto", "usuario", loginUsuario);
        webServiceHelper.addMethodParameter("bajarDatosMobile2Remoto", "clave", "");
        webServiceHelper.addMethodParameter("bajarDatosMobile2Remoto", "imei", SharedPreferencesManager.getImei(context));

        //actualizar fecha download Local
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "actualizaFechaDownLoad", direccionWSLocal + nombreWS, "http://www.inteldevmobile.com.ar/actualizaFechaDownLoad", "actualizaFechaDownload", 0);
        webServiceHelper.addMethodParameter("actualizaFechaDownload", "usuario", loginUsuario);
        webServiceHelper.addMethodParameter("actualizaFechaDownload", "clave", "");
        webServiceHelper.addMethodParameter("actualizaFechaDownload", "imei", SharedPreferencesManager.getImei(context));

        //actualizar fecha download Remoto
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "actualizaFechaDownLoad", direccionWSRemoto + nombreWS, "http://www.inteldevmobile.com.ar/actualizaFechaDownLoad", "actualizaFechaDownloadRemoto", 0);
        webServiceHelper.addMethodParameter("actualizaFechaDownloadRemoto", "usuario", loginUsuario);
        webServiceHelper.addMethodParameter("actualizaFechaDownloadRemoto", "clave", "");
        webServiceHelper.addMethodParameter("actualizaFechaDownloadRemoto", "imei", SharedPreferencesManager.getImei(context));

        //subir datos mobile 3 Local
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "subirDatosMobile3", direccionWSLocal + nombreWS, "http://www.inteldevmobile.com.ar/subirDatosMobile3", "subirDatosMobile3", 0);

        //subir datos mobile 3 Remoto
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "subirDatosMobile3", direccionWSRemoto + nombreWS, "http://www.inteldevmobile.com.ar/subirDatosMobile3", "subirDatosMobile3Remoto", 0);

        //Hola
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "hola", direccionWSLocal + nombreWS, "http://www.inteldevmobile.com.ar/hola", "hola", 5000);
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "hola", direccionWSRemoto + nombreWS, "http://www.inteldevmobile.com.ar/hola", "holaRemoto", 5000);
        //webServiceHelper.addService("http://www.inteldevmobile.com.ar","hola","http://hergo22.no-ip.org:8888/inteldevwebservice/Service.asmx?WSDL","http://www.inteldevmobile.com.ar/hola","holaCualquiera");

        //Login
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "IniciarSesion", direccionWSLocal + nombreWS, "http://www.inteldevmobile.com.ar/IniciarSesion", "IniciarSesion", 5000);
        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "IniciarSesion", direccionWSRemoto + nombreWS, "http://www.inteldevmobile.com.ar/IniciarSesion", "IniciarSesionRemoto", 5000);
    }

    public WebServiceHelper getWebServiceHelper()
    {
        return webServiceHelper;
    }
}
