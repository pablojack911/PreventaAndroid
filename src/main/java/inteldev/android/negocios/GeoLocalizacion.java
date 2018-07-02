package inteldev.android.negocios;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import java.util.Timer;
import java.util.TimerTask;

import inteldev.android.presentation.MainActivity;

/**
 * Created by Operador on 2/06/14.
 */
public class GeoLocalizacion extends Service {

    String epa = "SDS";

    private Timer timer = new Timer();
    private final IBinder binder = new MyBinder();
    private static final long UPDATE_INTERVAL = 5000;

    private Location location;
    String servicio;
    LocationManager locationManager;
    String proveedor;
    WebServiceHelper webServiceHelper;

/*    public GeoLocalizacion(){

        comenzar();
    }*/
    public void OnCreate(){
        super.onCreate();
        comenzar();
    }

/*    public GeoLocalizacion(Context context){

            webServiceHelper = new WebServiceHelper();

        comenzar(context);

    }*/

    private void comenzar() {

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

               String epa = "sdsd";

                Log.i(getClass().getSimpleName(),"SE EJECUTAAAAAAAA");

            }
        },0,UPDATE_INTERVAL);

  ///      Toast.makeText(context,"COMENZOOO",Toast.LENGTH_SHORT).show();

/*        servicio = Context.LOCATION_SERVICE;
        locationManager = (LocationManager)context.getSystemService(servicio);
        proveedor = LocationManager.GPS_PROVIDER;
        location  = locationManager.getLastKnownLocation(proveedor);
        posicion(context);
        locationManager.requestLocationUpdates(proveedor,5000,5,new LocationListener() {
            @Override
            public void onLocationChanged(Location Location) {

                location = Location;
                posicion(context);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        });

    }


    public void posicion(Context context)
    {
        if (location!=null)
        {
            Double lat = location.getLatitude()*1E6;
            Double lng = location.getLongitude()*1E6;


            String lat2 = new Double(lat).toString();
            String lng2 = new Double(lng).toString();

            webServiceHelper.addService("http://www.inteldevmobile.com.ar","ActualizarPosicion","http://192.168.1.99:8888/inteldevwebservice/Service.asmx?WSDL","http://www.inteldevmobile.com.ar/ActualizarPosicion","ActualizarPosicion");
            webServiceHelper.addMethodParameter("ActualizarPosicion","fecha",Fecha.obtenerFechaHoraActual());
            webServiceHelper.addMethodParameter("ActualizarPosicion","usuario", "81");
            webServiceHelper.addMethodParameter("ActualizarPosicion","lat",lat2);
            webServiceHelper.addMethodParameter("ActualizarPosicion","lng",lng2);


            webServiceHelper.executeService("ActualizarPosicion");

        }*/
    }

   @Override
    public void onDestroy(){
        super.onDestroy();
        if (timer != null){
            timer.cancel();
        }

    }

    @Override
    public IBinder onBind(Intent arg0){
        return binder;
    }

    public class MyBinder extends Binder {
        public GeoLocalizacion getService() {
            return GeoLocalizacion.this;
        }
    }

    public String dame(){
        return epa;
    }
}


