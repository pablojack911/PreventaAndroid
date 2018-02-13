package inteldev.android.negocios;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Operador on 25/07/2014.
 */
public class EstadoConexion extends Activity {

    ConnectivityManager connectivityManager;

    public EstadoConexion(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

    public boolean Conectado3G() {
        //  AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Recogemos el servicio ConnectivityManager
        //el cual se encarga de todas las conexiones del terminal

        boolean conectado = false;
        //Recogemos el estado del 3G
        //como vemos se recoge con el parámetro 0
        NetworkInfo.State internet_movil = connectivityManager.getNetworkInfo(0).getState();
        if (internet_movil == NetworkInfo.State.CONNECTED || internet_movil == NetworkInfo.State.CONNECTING)
            conectado = true;
        {

        }

        return conectado;
    }


    public boolean ConectadoWifi() {

        boolean conectado = false;

        NetworkInfo.State wifi = connectivityManager.getNetworkInfo(1).getState();

        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            conectado = true;
        }

        return conectado;
    }

    public boolean Conectado() {
        boolean conectado = false;
//        boolean conectado3G = Conectado3G();
//        boolean conectadoWifi = ConectadoWifi();
//        if (conectado3G || conectadoWifi)

        NetworkInfo activeNetwork = this.connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if ((activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) || (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)) {
                // connected to wifi
//                or
                // connected to the mobile provider's data plan
                conectado = true;
            }
        } else {
            // not connected to the internet
        }

        return conectado;
    }

}
