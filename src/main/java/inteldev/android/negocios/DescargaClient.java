package inteldev.android.negocios;

import android.content.Context;
import android.os.Environment;

import org.ksoap2.serialization.SoapObject;

import inteldev.android.presentation.DescargaListener;
import inteldev.android.presentation.DescargaManager;

/**
 * Created by Operador on 23/04/14.
 */
public class DescargaClient
{

    public EstadoWebService.Estado estadoWebService;
    private String url; //= "http://192.168.1.99:8888/";
    private DescargaManager descargaManager;
    private ServiceRegistry serviceRegistry;
    private DescargaListener descargaListener;

    public DescargaClient(final Context context, String loginUsuario)
    {
        String directory = Environment.getExternalStorageDirectory().toString();
        String ubicacion = directory + "/" + Environment.DIRECTORY_DOWNLOADS;

        BorrarArchivos borrarArchivos = new BorrarArchivos();
        borrarArchivos.BorrarPorContenido(ubicacion, ".xml");

        descargaManager = new DescargaManager(context);

        serviceRegistry = new ServiceRegistry(context, loginUsuario);

        descargaManager.setDescargaListener(new DescargaListener()
        {
            @Override
            public void Notificar(boolean estado)
            {
                descargaListener.Notificar(estado);

            }
        });

        String servicioBajarDatosMobile = "";
        String servicioBajarDatosMobile2 = "";

        EstadoWebService estadoWebService = FabricaNegocios.obtenerEstadoWebService(context, loginUsuario);

        this.estadoWebService = estadoWebService.verificarConexionServicio("hola", "holaRemoto");

        if (this.estadoWebService == EstadoWebService.Estado.NoDisponible)
        {
            return;
        }
        else
        {
            if (this.estadoWebService == EstadoWebService.Estado.ConexionLocal)
            {
                servicioBajarDatosMobile = "bajarDatosMobile";
                servicioBajarDatosMobile2 = "bajarDatosMobile2";
                url = ServiceRegistry.direccionWSLocal;
            }
            else
            {
                servicioBajarDatosMobile = "bajarDatosMobileRemoto";
                servicioBajarDatosMobile2 = "bajarDatosMobile2Remoto";
                url = ServiceRegistry.direccionWSRemoto;
            }
        }
        this.bajarDatosMobile1(servicioBajarDatosMobile);
        this.bajarDatosMobile2(servicioBajarDatosMobile2);
    }

    public void setDescargaListener(DescargaListener descargaListener)
    {
        this.descargaListener = descargaListener;
    }

    public void bajarDatosMobile1(String nombreServicio)
    {
        SoapObject resultService = serviceRegistry.getWebServiceHelper().executeService(nombreServicio);
        String url = this.url + resultService.getProperty(0);
        descargaManager.setTitle("Inteldev Mobile");
        descargaManager.setFileName("InteldevMobile.xml");
        descargaManager.downloadFile(url);
    }

    public void bajarDatosMobile2(String nombreServicio)
    {
        SoapObject resultService = serviceRegistry.getWebServiceHelper().executeService(nombreServicio);
        String url = this.url + resultService.getProperty(0);
        descargaManager.setTitle("Inteldev Mobile 2");
        descargaManager.setFileName("InteldevMobile2.xml");
        descargaManager.downloadFile(url);
    }
}
