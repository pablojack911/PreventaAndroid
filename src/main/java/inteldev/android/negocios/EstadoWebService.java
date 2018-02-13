package inteldev.android.negocios;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Operador on 10/11/2014.
 */
public class EstadoWebService {

    ServiceRegistry serviceRegistry;
    ServiceRegistry serviceRegistryRemoto;

    public EstadoWebService(ServiceRegistry serviceRegistry, ServiceRegistry serviceRegistryRemoto)
    {
        this.serviceRegistry = serviceRegistry;
        this.serviceRegistryRemoto = serviceRegistryRemoto;
    }

    public Estado verificarConexionServicio(String nombreServicioLocal, String nombreServicioRemoto){

        Estado estadoWS;

        SoapObject respuestaLocal = this.serviceRegistry.getWebServiceHelper().executeService(nombreServicioLocal);

        if (!respuestaLocal.getName().isEmpty()) {
            estadoWS = Estado.ConexionLocal;
        } else {
            SoapObject respuestaRemoto = serviceRegistryRemoto.getWebServiceHelper().executeService(nombreServicioRemoto);

            if (!respuestaRemoto.getName().isEmpty()) {
                estadoWS = Estado.ConexionRemota;
            }
            else{
                estadoWS = Estado.NoDisponible;
            }
        }
        return estadoWS;
    }

    public enum Estado
    {
        NoDisponible,
        ConexionLocal,
        ConexionRemota
    }

}
