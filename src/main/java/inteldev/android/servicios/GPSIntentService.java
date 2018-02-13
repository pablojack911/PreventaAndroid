package inteldev.android.servicios;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;

import inteldev.android.modelo.PosicionesGPS;
import inteldev.android.negocios.ControladorPosicionesGPS;
import inteldev.android.negocios.EnviarEstadosGeoLocalizacion;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.Fecha;

/**
 * Created by Pocho on 21/03/2017.
 */

public class GPSIntentService extends IntentService
{
    private static final String POSICION_GPS_KEY = "posicion-gps-key";
    ControladorPosicionesGPS controladorPosicionesGPS;

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
        Location location = controladorPosicionesGPS.obtenerUbicacion(GPSIntentService.this);
        PosicionesGPS posicionesGPS;
        if (intent != null && intent.hasExtra(POSICION_GPS_KEY))
        {
            posicionesGPS = intent.getParcelableExtra(POSICION_GPS_KEY);
        }
        else
        {
            posicionesGPS = new PosicionesGPS();
            posicionesGPS.estado = 99;
        }

        if (location != null)
        {
            posicionesGPS.longitud = (float) location.getLongitude();
            posicionesGPS.latitud = (float) location.getLatitude();
            posicionesGPS.fecha = Fecha.obtenerFechaHoraActual();
            posicionesGPS.imei = FabricaNegocios.obtenerImei(GPSIntentService.this);
            controladorPosicionesGPS.insertar(posicionesGPS);

            EnviarEstadosGeoLocalizacion enviarEstadosGeoLocalizacion = new EnviarEstadosGeoLocalizacion();
            enviarEstadosGeoLocalizacion.enviarEstadosGeoLocalizacion(GPSIntentService.this);
        }
    }
}
