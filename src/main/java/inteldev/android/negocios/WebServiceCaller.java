package inteldev.android.negocios;

import android.os.AsyncTask;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * WebServiceCaller
 *
 * Used to called encapsulate SOAP web service calls.
 *
 * @version 1.0 06 march of 2014
 * @author Gabriel Verdi
 */
public class WebServiceCaller extends AsyncTask<Void,Void,SoapObject> {

    private String Namespace;
    private String MethodName;
    private String Url;
    private SoapObject request;
    private String SoapAction;
    private int TimeOut;

    public WebServiceCaller(String Namespace, String MethodName, String Url, String SoapAction, int Timeout) {
        this.Namespace = Namespace;
        this.MethodName = MethodName;
        this.Url = Url;
        request = new SoapObject(this.Namespace, this.MethodName);
        this.SoapAction = SoapAction;
        if (Timeout==0) {
            this.TimeOut = 20000;
        }else
        {
            this.TimeOut = Timeout;
        }

    }

    /***
     * Adds a parameter to the calling method.
     * @param parameter name of the parameter. Ex. Celcius
     * @param value value of the parameter. Ex. 20
     */
    protected void addParameter(String parameter, String value) {
        this.request.addProperty(parameter,value);
    }

    protected SoapObject doInBackground(Void... params) {
        try {
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(Url,this.TimeOut);

            //300000
            //Declare the version of the SOAP request
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            //this is the actual part that will call the webservice
            androidHttpTransport.call(this.SoapAction, envelope);
            // Get the SoapResult from the envelope body.

            return (SoapObject) envelope.bodyIn;
        }
        catch (Exception e)
        {
            //Log.e("WebService",e.getMessage());
            return new SoapObject();
        }
    }
}
