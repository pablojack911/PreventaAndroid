package inteldev.android.negocios;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import inteldev.android.accesoadatos.IDao;

/**
 * Created by Operador on 01/12/2015.
 */
public class EnviarEstadosGeoLocalizacion
{

    WebServiceHelper webServiceHelper;
    IDao dao;

    public void enviarEstadosGeoLocalizacion(Context context)
    {
        try
        {


            this.dao = FabricaNegocios.obtenerDao(context);

            Cursor cursor = this.dao.ejecutarConsultaSql("select fecha,latitud,longitud,estado,bultos,pesos,idcliente,motivonocompra,imei,usuario,id from posicionesGPS " +
                    " where enviado=0");

            if (cursor != null && cursor.moveToFirst())
            {
                do
                {
                    String fechaHora = cursor.getString(0);
                    Double lat3 = cursor.getDouble(1);
                    Double lng3 = cursor.getDouble(2);
                    String estado = cursor.getString(3);

                    String lat4 = lat3.toString().replace(".", ",");
                    String lng4 = lng3.toString().replace(".", ",");

                    int bultos = cursor.getInt(4);
                    float pesos = cursor.getFloat(5);

                    String idCliente = cursor.getString(6);
                    String motivoNoCompra = cursor.getString(7);
                    String imei = cursor.getString(8);
                    String usuario = cursor.getString(9);
                    int id = cursor.getInt(10);

                    webServiceHelper = new WebServiceHelper();
//                    webServiceHelper.addService("http://www.inteldevmobile.com.ar", "ActualizarPosicionTest", ServiceRegistry.direccionWSRemoto + ServiceRegistry.nombreWS, "http://www.inteldevmobile.com.ar/ActualizarPosicionTest", "ActualizarPosicionTest", 0);
                    webServiceHelper.addService("http://www.inteldevmobile.com.ar", "ActualizarPosicionTestImei", ServiceRegistry.direccionWSRemoto + ServiceRegistry.nombreWS, "http://www.inteldevmobile.com.ar/ActualizarPosicionTestImei", "ActualizarPosicionTestImei", 0);
                    webServiceHelper.addMethodParameter("ActualizarPosicionTestImei", "fecha", fechaHora);
                    webServiceHelper.addMethodParameter("ActualizarPosicionTestImei", "usuario", usuario);
                    webServiceHelper.addMethodParameter("ActualizarPosicionTestImei", "lat", lat4);
                    webServiceHelper.addMethodParameter("ActualizarPosicionTestImei", "lng", lng4);
                    webServiceHelper.addMethodParameter("ActualizarPosicionTestImei", "bultos", String.valueOf(bultos));
                    webServiceHelper.addMethodParameter("ActualizarPosicionTestImei", "pesos", Float.toString(pesos).replace(".", ","));
                    webServiceHelper.addMethodParameter("ActualizarPosicionTestImei", "estado", estado);
                    webServiceHelper.addMethodParameter("ActualizarPosicionTestImei", "cliente", idCliente);
                    webServiceHelper.addMethodParameter("ActualizarPosicionTestImei", "motivonocompra", motivoNoCompra);
                    webServiceHelper.addMethodParameter("ActualizarPosicionTestImei", "imei", imei);

                    SoapObject respuesta = webServiceHelper.executeService("ActualizarPosicionTestImei");
                    String resp = "";
                    if (respuesta.getPropertyCount() > 0)
                    {
                        resp = respuesta.getProperty(0).toString();
                    }

                    if ("ok".equals(resp))
                    {
                        this.dao.ejecutarSentenciaSql("update posicionesGPS set enviado = 1 where id = '" + id + "'");
                    }
                } while (cursor.moveToNext());
            }
        }
        catch (Exception ex)
        {
            Log.e("EnviarEstadosGeoLoc", ex.getLocalizedMessage());
        }

    }
}
