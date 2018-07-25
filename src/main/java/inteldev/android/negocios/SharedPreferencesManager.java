package inteldev.android.negocios;

import android.content.Context;
import android.content.SharedPreferences;

import inteldev.android.CONSTANTES;

import static inteldev.android.CONSTANTES.IMEI_KEY;
import static inteldev.android.CONSTANTES.SESION_INICIADA;
import static inteldev.android.CONSTANTES.USUARIO_KEY;

public class SharedPreferencesManager
{
    private static SharedPreferences sharedPref;

    private static SharedPreferences getSharedPreferences(Context context)
    {
        if (sharedPref == null)
        {
            sharedPref = context.getSharedPreferences(CONSTANTES.PREFERENCIAS, Context.MODE_PRIVATE);
        }
        return sharedPref;
    }

    public static boolean isLogin(Context context)
    {
        return getSharedPreferences(context).getBoolean(SESION_INICIADA, false);
    }

    public static void setLogin(Context context, boolean login)
    {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(SESION_INICIADA, login);
        editor.apply();
    }

    public static void setLoginUsuario(Context context, String loginUsuario)
    {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(USUARIO_KEY, loginUsuario);
        editor.apply();
    }

    public static String getLoginUsuario(Context context)
    {
        return getSharedPreferences(context).getString(USUARIO_KEY, "");
    }

    public static void setImei(Context context, String imei)
    {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(IMEI_KEY, imei);
        editor.apply();
    }

    public static String getImei(Context context)
    {
        if (getSharedPreferences(context).getString(IMEI_KEY, "").equals(""))
        {
            setImei(context, FabricaNegocios.obtenerImei(context));
        }
        return getSharedPreferences(context).getString(IMEI_KEY, "");
    }

    public static String getString(Context context, String key)
    {
        return getSharedPreferences(context).getString(key, "");
    }

    public static void setString(Context context, String key, String value)
    {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(key, value);
        editor.apply();
    }
}
