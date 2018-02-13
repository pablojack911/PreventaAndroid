package inteldev.android.negocios;

import android.app.Activity;
import android.location.LocationManager;

/**
 * Created by Operador on 23/12/2015.
 */
public class EstadoGPS extends Activity
{
    LocationManager locationManager;

    public EstadoGPS(LocationManager locationManager)
    {
        this.locationManager = locationManager;
    }

    public boolean GPSActivado()
    {
        boolean activado = false;
        if (this.locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            activado = true;
        }
        return activado;
    }
}
