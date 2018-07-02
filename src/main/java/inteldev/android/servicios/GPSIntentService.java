package inteldev.android.servicios;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Date;

import inteldev.android.modelo.PosicionesGPS;
import inteldev.android.negocios.ControladorPosicionesGPS;
import inteldev.android.negocios.EnviarEstadosGeoLocalizacion;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.Fecha;
import inteldev.android.negocios.SharedPreferencesManager;

import static inteldev.android.CONSTANTES.POSICION_GPS_VIAJE_KEY;

/**
 * Created by Pocho on 21/03/2017.
 */

public class GPSIntentService extends IntentService
{
    private static final String POSICION_GPS_KEY = "posicion-gps-key";
    ControladorPosicionesGPS controladorPosicionesGPS;
    private FusedLocationProviderClient mFusedLocationClient;

    public GPSIntentService()
    {
        this("GPSIntentService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GPSIntentService(String name)
    {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        controladorPosicionesGPS = FabricaNegocios.obtenerControladorPosicionesGPS(GPSIntentService.this);
//        Location location = controladorPosicionesGPS.obtenerUbicacion(GPSIntentService.this);
        final PosicionesGPS posicionesGPS;
        if (intent != null)
        {
            if (intent.hasExtra(POSICION_GPS_VIAJE_KEY))
            {
                //todo: GPSLocationService está enviando una actualizacion de posicion. Tiene todos los datos, solo necesita grabar en sqlite y enviar.
                posicionesGPS = intent.getParcelableExtra(POSICION_GPS_VIAJE_KEY);
                controladorPosicionesGPS.insertar(posicionesGPS);
            }
            else if (intent.hasExtra(POSICION_GPS_KEY))
            {
                posicionesGPS = intent.getParcelableExtra(POSICION_GPS_KEY);
                posicionesGPS.imei = SharedPreferencesManager.getImei(GPSIntentService.this);
                if (ActivityCompat.checkSelfPermission(GPSIntentService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GPSIntentService.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                {
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>()
                    {
                        @Override
                        public void onSuccess(Location location)
                        {
                            if (location != null && location.getLatitude() != 0)
                            {
                                posicionesGPS.fecha = Fecha.convertirFechaHora(new Date(location.getTime()));
                                posicionesGPS.latitud = Float.valueOf(String.valueOf(location.getLatitude()));
                                posicionesGPS.longitud = Float.valueOf(String.valueOf(location.getLongitude()));
                            }
                            else
                            {
                                PosicionesGPS ultimaGrabada = controladorPosicionesGPS.obtenerUltimaPosicion(posicionesGPS.usuario);
                                if (ultimaGrabada != null)
                                {
                                    posicionesGPS.fecha = ultimaGrabada.fecha;
                                    posicionesGPS.longitud = ultimaGrabada.longitud;
                                    posicionesGPS.latitud = ultimaGrabada.latitud;
                                }
                                else
                                {
                                    posicionesGPS.fecha = Fecha.obtenerFechaHoraActual();
                                }
                            }
                            controladorPosicionesGPS.insertar(posicionesGPS);
                        }
                    });
                }
            }
            try
            {
                EnviarEstadosGeoLocalizacion enviarEstadosGeoLocalizacion = new EnviarEstadosGeoLocalizacion();
                enviarEstadosGeoLocalizacion.enviarEstadosGeoLocalizacion(GPSIntentService.this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
