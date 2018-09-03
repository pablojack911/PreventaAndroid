package inteldev.android.negocios;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.PosicionesGPS;
import inteldev.android.servicios.GPSIntentService;

import static inteldev.android.CONSTANTES.POSICION_GPS_KEY;

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
        String query = "select motivoNoCompra from PosicionesGPS where PosicionesGPS.idCliente = '" + idCliente + "' and estado<>6 order by fecha desc limit 1";
        //        String query = "select motivoNoCompra from PosicionesGPS where PosicionesGPS.idCliente = '" + idCliente + "' and estado<>6 and strftime('%Y-%m-%d', PosicionesGPS.Fecha)=='" + fecha + "' order by fecha desc limit 1";
        Cursor cursor = this.dao.ejecutarConsultaSql(query);
        if (cursor != null && cursor.moveToFirst())
        {
            return cursor.getInt(0);
        }
        return -1;
    }

    //    public long actualizar(String idCliente, String fecha, PosicionesGPS posicionesGPS)
    //    {
    //        Mapeador<PosicionesGPS> posicionGPSMapeador = new Mapeador<PosicionesGPS>(posicionesGPS);
    //        ContentValues contentValues = posicionGPSMapeador.entityToContentValues();
    //        long res = -1;
    //        try
    //        {
    //            res = this.dao.update("PosicionesGPS", contentValues, "idCliente=?", new String[]{idCliente});
    ////            res = this.dao.update("PosicionesGPS", contentValues, "idCliente=? and strftime('%Y-%m-%d', PosicionesGPS.Fecha)=?", new String[]{idCliente, fecha});
    //        }
    //        catch (Exception ex)
    //        {
    //            Log.e("PosicionesGPS.act", ex.getLocalizedMessage());
    //        }
    //        return res;
    //    }

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
//
//    public Location obtenerUbicacion(Context context)
//    {
//        LocationManager mLocationManager;
//        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
//        List<String> providers = mLocationManager.getProviders(true);
//        Location bestLocation = null;
//        for (String provider : providers)
//        {
//            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
//            if (l == null)
//            {
//                continue;
//            }
//            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy())
//            {
//                // Found best last known location: %s", l);
//                bestLocation = l;
//            }
//        }
//        if (bestLocation == null)
//        {
//            bestLocation = new Location(LocationManager.GPS_PROVIDER);
//        }
//        return bestLocation;
//    }
//
//    public PosicionesGPS creaPosicion(Context context, String usuario, int estado, int motivoNoCompra, String idCliente, int bultos, float pesos)
//    {
//        String imei = FabricaNegocios.obtenerImei(context);
//        PosicionesGPS posicionesGPS = new PosicionesGPS();
//        Double lat = 0.00;
//        Double lng = 0.00;
//
//        Location location = this.obtenerUbicacion(context);
//        if (location != null)
//        {
//            lat = location.getLatitude();
//            lng = location.getLongitude();
//        }
//
//        posicionesGPS.latitud = lat.floatValue();
//        posicionesGPS.longitud = lng.floatValue();
//        posicionesGPS.fecha = Fecha.obtenerFechaHoraActual();
//        posicionesGPS.imei = imei;
//        posicionesGPS.estado = estado;
//        posicionesGPS.motivoNoCompra = motivoNoCompra;
//        posicionesGPS.idCliente = idCliente;
//        posicionesGPS.bultos = bultos;
//        posicionesGPS.pesos = pesos;
//
//        return posicionesGPS;
//    }

    public PosicionesGPS obtenerUltimaPosicion(String usuario)
    {
        PosicionesGPS posicionesGPS = null;
        Cursor cursor = dao.ejecutarConsultaSql("select * from PosicionesGPS where usuario='" + usuario + "' order by fecha desc");
        if (cursor.moveToFirst())
        {
            Mapeador<PosicionesGPS> mapper = new Mapeador<PosicionesGPS>(new PosicionesGPS());
            posicionesGPS = mapper.cursorToEntity(cursor);
        }
        return posicionesGPS;
    }

    public void enviarPosicion(Context context, PosicionesGPS posicionesGPS)
    {
        Intent mServiceIntent = new Intent(context, GPSIntentService.class);
        mServiceIntent.putExtra(POSICION_GPS_KEY, posicionesGPS);
        context.startService(mServiceIntent);
    }
}
