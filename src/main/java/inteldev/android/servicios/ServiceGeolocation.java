package inteldev.android.servicios;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import inteldev.android.modelo.EstadoGPS;
import inteldev.android.modelo.PosicionesGPS;
import inteldev.android.negocios.ControladorPosicionesGPS;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.SharedPreferencesManager;

import static android.location.LocationManager.*;
import static inteldev.android.CONSTANTES.POSICION_GPS_KEY;
import static inteldev.android.modelo.MotivoNoCompra.NO_HAY_DATOS_REGISTRADOS;

/**
 * Created by Pocho on 31/01/2017.
 */

public class ServiceGeolocation extends Service implements LocationListener
{
    private static final String TAG = "ServiceGeolocation";
    private static final int LOCATION_INTERVAL = 1000 * 60 * 2;
    private static final float LOCATION_DISTANCE = 0;
    LocationManager mLocationManager;
    ControladorPosicionesGPS controladorPosicionesGPS;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        controladorPosicionesGPS = new ControladorPosicionesGPS(FabricaNegocios.obtenerDao(getApplicationContext()));

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        try
        {
            mLocationManager.requestLocationUpdates(GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, this);
        }
        catch (java.lang.SecurityException ex)
        {
            Log.i(TAG, "fail to request location update, ignore", ex);
        }
        catch (IllegalArgumentException ex)
        {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }


//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);

//        Criteria crit = new Criteria();
//        crit.setAccuracy(Criteria.ACCURACY_FINE);
//        String bestProvider = locationManager.getBestProvider(crit, false);
//        locationManager.requestLocationUpdates(bestProvider, UPDATE_INTERVAL_IN_MILLISECONDS, 1, locationListener);
    }

    @Override
    public void onDestroy()
    {
        if (mLocationManager != null)
        {
            mLocationManager.removeUpdates(this);
        }
    }

    public void procesaPosicion(Location location, int estadoGPS)
    {
        PosicionesGPS posicionesGPS = new PosicionesGPS();
        posicionesGPS.usuario = SharedPreferencesManager.getLoginUsuario(getApplicationContext());
        posicionesGPS.estado = estadoGPS;
        posicionesGPS.motivoNoCompra = NO_HAY_DATOS_REGISTRADOS;
        posicionesGPS.idCliente = "VIAJE";
        if (location != null)
        {
            posicionesGPS.latitud = (float) location.getLatitude();
            posicionesGPS.longitud = (float) location.getLongitude();
        }

        Intent mServiceIntent = new Intent(this, GPSIntentService.class);
        mServiceIntent.putExtra(POSICION_GPS_KEY, posicionesGPS);
        this.startService(mServiceIntent);
    }

    @Override
    public void onLocationChanged(Location location)
    {
        procesaPosicion(location, EstadoGPS.OK);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onProviderDisabled(String provider)
    {
        procesaPosicion(mLocationManager.getLastKnownLocation(GPS_PROVIDER), EstadoGPS.GPS_APAGADO);
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

