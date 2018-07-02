package inteldev.android.presentation;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static inteldev.android.CONSTANTES.SESION_INICIADA;
import static inteldev.android.CONSTANTES.USUARIO_KEY;

/**
 * Created by Operador on 28/04/14.
 */
public class LoginObservable
{
    private static LoginObservable instancia;
    private OnLoginListener onLoginListener;

    public LoginObservable()
    {
    }

    public static LoginObservable getInstancia()
    {
        if (instancia == null)
        {
            instancia = new LoginObservable();
        }
        return instancia;
    }

    public void setOnLoginListener(OnLoginListener onLoginListener)
    {
        this.onLoginListener = onLoginListener;
    }

    private SharedPreferences getPreferences(Context context)
    {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
