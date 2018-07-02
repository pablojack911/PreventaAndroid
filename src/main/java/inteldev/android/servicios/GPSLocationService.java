package inteldev.android.servicios;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Date;

import inteldev.android.CONSTANTES;
import inteldev.android.modelo.PosicionesGPS;
import inteldev.android.negocios.Fecha;
import inteldev.android.negocios.SharedPreferencesManager;

public class GPSLocationService extends Service
{
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(CONSTANTES.GPS_INTERVAL_REQUEST);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationCallback = new LocationCallback()
        {
            @Override
            public void onLocationResult(LocationResult locationResult)
            {
                for (Location location : locationResult.getLocations())
                {
                    procesaPosicion(location);
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null /* Looper */);
        }
    }

    @Override
    public void onDestroy()
    {
        stopLocationUpdates();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    private void stopLocationUpdates()
    {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void procesaPosicion(Location location)
    {
        PosicionesGPS pos = new PosicionesGPS();
        pos.idCliente = "VIAJE";
        pos.usuario = SharedPreferencesManager.getLoginUsuario(getApplicationContext());
        pos.imei = SharedPreferencesManager.getImei(getApplicationContext());
        if (location != null)
        {
            pos.latitud = Float.valueOf(String.valueOf(location.getLatitude()));
            pos.longitud = Float.valueOf(String.valueOf(location.getLongitude()));
            pos.fecha = Fecha.convertirFechaHora(new Date(location.getTime()));
            Intent mServiceIntent = new Intent(getApplicationContext(), GPSIntentService.class);
            mServiceIntent.putExtra(CONSTANTES.POSICION_GPS_VIAJE_KEY, pos);
            startService(mServiceIntent);
        }
    }
}

