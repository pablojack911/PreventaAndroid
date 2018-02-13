package inteldev.android.negocios;

import android.content.Context;
import android.util.Log;

import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.Map;

/**
 * WebServiceHelper
 *
 * Used to make calling SOAP web services easier and faster. It has the ability to
 * keep references to many services.
 *
 * @author Gabriel Verdi
 * @version 1.0 06 march of 2014
 */
public class WebServiceHelper {


    private Map<String,WebServiceCaller> serviceCallerMap;

    public WebServiceHelper() {
        this.serviceCallerMap = new HashMap<String, WebServiceCaller>();
    }

    /***
     * Adds a webservicecaller to the map of services for later use.
     * @param namespace
     * @param methodName short method name
     * @param url index url where all service are
     * @param soapAction
     * @param serviceName name to identify the service
     */
    public void addService(String namespace, String methodName, String url, String soapAction, String serviceName, int timeOut) {
        WebServiceCaller webServiceCaller = new WebServiceCaller(namespace,methodName,url,soapAction,timeOut);
        this.serviceCallerMap.put(serviceName,webServiceCaller);
    }

    /***
     * Executes a given service and returns it's result. Logs if error.
     * @param serviceName
     * @return SoapObject result. Or null if no results.
     */
    public SoapObject executeService(String serviceName) {
        WebServiceCaller serviceCaller = this.serviceCallerMap.get(serviceName);
        SoapObject result;
        try {
             result = serviceCaller.execute().get();
        }
        catch (Exception e)
        {
            Log.e(serviceName,e.getMessage());
            result = null;
        }
        return result;
    }

    /***
     * Adds parameter to method of service.
     * @param serviceName
     * @param parameterName
     * @param value
     */
    public void addMethodParameter(String serviceName, String parameterName, String value) {
        WebServiceCaller serviceCaller = this.serviceCallerMap.get(serviceName);
        serviceCaller.addParameter(parameterName,value);
    }
}
