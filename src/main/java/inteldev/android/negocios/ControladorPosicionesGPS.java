package inteldev.android.negocios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.util.List;

import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.PosicionesGPS;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Pocho on 25/01/2017.
 */

public class ControladorPosicionesGPS
{
    private IDao dao;

    public ControladorPosicionesGPS(IDao dao)
    {
        this.dao = dao;
    }

    public int obtenerMotivoNoCompra(String idCliente, String fecha)
    {
        String query = "select motivoNoCompra from PosicionesGPS where PosicionesGPS.idCliente = '" + idCliente + "' and estado<>6 and strftime('%Y-%m-%d', PosicionesGPS.Fecha)=='" + fecha + "' order by fecha desc limit 1";
        Cursor cursor = this.dao.ejecutarConsultaSql(query);
        if (cursor != null && cursor.moveToFirst())
        {
            return cursor.getInt(0);
        }
        return -1;
    }

    public long actualizar(String idCliente, String fecha, PosicionesGPS posicionesGPS)
    {
        Mapeador<PosicionesGPS> posicionGPSMapeador = new Mapeador<PosicionesGPS>(posicionesGPS);
        ContentValues contentValues = posicionGPSMapeador.entityToContentValues();
        long res = -1;
        try
        {
            res = this.dao.update("PosicionesGPS", contentValues, "idCliente='" + idCliente + "'" + "and strftime('%Y-%m-%d', PosicionesGPS.Fecha)== '" + fecha + "'");
        }
        catch (Exception ex)
        {
            Log.e("PosicionesGPS.actualizar", ex.getLocalizedMessage());
        }
        return res;
    }

    public long insertar(PosicionesGPS posicionesGPS)
    {
        Mapeador<PosicionesGPS> posicionGPSMapeador = new Mapeador<PosicionesGPS>(posicionesGPS);
        ContentValues contentValues = posicionGPSMapeador.entityToContentValues();
        long res = -1;
        try
        {
            res = this.dao.insert("PosicionesGPS", contentValues);
        }
        catch (Exception ex)
        {
            Log.e("PosicionesGPS.insertar", ex.getLocalizedMessage());
        }
        return res;
    }

    public Location obtenerUbicacion(Context context)
    {
        LocationManager mLocationManager;
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers)
        {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null)
            {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy())
            {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        if (bestLocation == null)
        {
            bestLocation = new Location(LocationManager.GPS_PROVIDER);
        }
        return bestLocation;
    }

    public PosicionesGPS creaPosicion(Context context, String usuario, int estado, int motivoNoCompra, String idCliente, int bultos, float pesos)
    {
        String imei = FabricaNegocios.obtenerImei(context);
        PosicionesGPS posicionesGPS = new PosicionesGPS();
        Double lat = 0.00;
        Double lng = 0.00;

        Location location = this.obtenerUbicacion(context);
        if (location != null)
        {
            lat = location.getLatitude();
            lng = location.getLongitude();
        }

        posicionesGPS.latitud = lat.floatValue();
        posicionesGPS.longitud = lng.floatValue();
        posicionesGPS.fecha = Fecha.obtenerFechaHoraActual();
        posicionesGPS.imei = imei;
        posicionesGPS.estado = estado;
        posicionesGPS.motivoNoCompra = motivoNoCompra;
        posicionesGPS.idCliente = idCliente;
        posicionesGPS.bultos = bultos;
        posicionesGPS.pesos = pesos;

        return posicionesGPS;
    }
}
