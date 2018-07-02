package inteldev.android.negocios;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.telephony.TelephonyManager;

import inteldev.android.accesoadatos.Dao;
import inteldev.android.accesoadatos.IDao;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by Operador on 9/05/14.
 */
public class FabricaNegocios
{

    @SuppressLint("MissingPermission")
    public static String obtenerImei(Context context)
    {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public static IDao obtenerDao(Context context)
    {
        IDao dao = new Dao(context);
        return dao;
    }

    public static ControladorConvenio obtenerControladorConvenio(Context context, String cliente)
    {
        ControladorConvenio controladorConvenio = new ControladorConvenio(FabricaNegocios.obtenerDao(context), cliente);
        return controladorConvenio;
    }

    public static ControladorPedido obtenerControladorPedido(Context context)
    {
        ControladorPedido controladorPedido = new ControladorPedido(FabricaNegocios.obtenerDao(context));
        return controladorPedido;
    }

    public static ControladorOferta obtenerControladorOferta(Context context)
    {
        ControladorOferta controladorOferta = new ControladorOferta(FabricaNegocios.obtenerDao(context), FabricaNegocios.obtenerControladorCliente(context));
        return controladorOferta;
    }

    public static ControladorCliente obtenerControladorCliente(Context context)
    {
        ControladorCliente controladorCliente = new ControladorCliente(FabricaNegocios.obtenerDao(context));
        return controladorCliente;
    }

    public static ControladorArticulo obtenerControladorArticulo(Context context)
    {

        ControladorArticulo controladorArticulo = new ControladorArticulo(FabricaNegocios.obtenerDao(context));
        return controladorArticulo;
    }

    public static ControladorPrecio obtenerControladorPrecio(Context context)
    {
        ControladorPrecio controladorPrecio = new ControladorPrecio(FabricaNegocios.obtenerDao(context), FabricaNegocios.obtenerControladorCliente(context));
        return controladorPrecio;
    }

    public static ControladorRutaDeVenta obtenerControladorVendedor(Context context)
    {
        ControladorRutaDeVenta controladorRutaDeVenta = new ControladorRutaDeVenta(FabricaNegocios.obtenerDao(context));
        return controladorRutaDeVenta;
    }

    public static EstadoConexion obtenerEstadoConexion(ConnectivityManager connectivityManager)
    {
        EstadoConexion estadoConexion = new EstadoConexion(connectivityManager);
        return estadoConexion;
    }

    public static EstadoGPS obtenerEstadoGPS(LocationManager locationManager)
    {
        EstadoGPS estadoGPS = new EstadoGPS(locationManager);
        return estadoGPS;
    }

    //    public static boolean leerEstadoParaUsoApp(ConnectivityManager connectivityManager, LocationManager locationManager)
//    {
//
//        boolean activado = false;
//
//        //if (FabricaNegocios.obtenerEstadoGPS(locationManager).GPSActivado() && FabricaNegocios.obtenerEstadoConexion(connectivityManager).Conectado() )
//        if (FabricaNegocios.obtenerEstadoGPS(locationManager).GPSActivado())
//        {
//            activado = true;
//        }
//
//        return activado;
//    }
    public static boolean leerEstadoParaUsoApp(Context context)
    {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean activado = false;

        //if (FabricaNegocios.obtenerEstadoGPS(locationManager).GPSActivado() && FabricaNegocios.obtenerEstadoConexion(connectivityManager).Conectado() )
        if (FabricaNegocios.obtenerEstadoGPS(locationManager).GPSActivado())
        {
            activado = true;
        }

        return activado;
    }

    public static ControladorStock obtenerControladorStock(Context context)
    {
        ControladorStock controladorStock = new ControladorStock(FabricaNegocios.obtenerDao(context));

        return controladorStock;
    }

    public static EstadoWebService obtenerEstadoWebService(Context context, String loginUsuario)
    {
        ServiceRegistry serviceRegistry = new ServiceRegistry(context, loginUsuario);
        ServiceRegistry serviceRegistryRemoto = new ServiceRegistry(context, loginUsuario);

        EstadoWebService estadoWebService = new EstadoWebService(serviceRegistry, serviceRegistryRemoto);

        return estadoWebService;
    }

    public static ControladorPosicionesGPS obtenerControladorPosicionesGPS(Context context)
    {
        ControladorPosicionesGPS controladorPosicionesGPS = new ControladorPosicionesGPS(FabricaNegocios.obtenerDao(context));
        return controladorPosicionesGPS;
    }

}
