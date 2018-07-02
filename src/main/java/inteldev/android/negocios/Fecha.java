package inteldev.android.negocios;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import inteldev.android.CONSTANTES;

import static inteldev.android.CONSTANTES.FORMATO_DIA_MES_AÑO;
import static inteldev.android.CONSTANTES.FORMATO_FECHA_COMPLETA;

/**
 * Created by Operador on 20/05/14.
 */
public class Fecha
{
    public static java.sql.Date obtenerFechaActual()
    {
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        System.out.println("Fecha.obtenerFechaActual - " + sqlDate);
        return sqlDate;
    }

    public static String dameUsuario(String loginUsuario)
    {
        String lavar = loginUsuario;
        System.out.println("Fecha.dameUsuario - " + lavar);
        return lavar;
    }

    public static String obtenerFechaHoraActual()
    {
        Date date = new Date();
        DateFormat fechaHoraFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        System.out.println("Fecha.obtenerFechaHoraActual - " + fechaHoraFormat.format(date));
        return fechaHoraFormat.format(date);
    }


    public static java.sql.Date sumarFechasDias(java.sql.Date fch, int dias)
    {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(fch.getTime());
        cal.add(Calendar.DATE, dias);
        System.out.println("Fecha.sumarFechasDias - " + cal.getTimeInMillis());
        return new java.sql.Date(cal.getTimeInMillis());
    }

    public static java.util.Date convertir(String fecha)
    {
        SimpleDateFormat format = new SimpleDateFormat(FORMATO_FECHA_COMPLETA);
        Date sqlFecha = null;
//        System.out.println("Fecha.convertirFechaHora - " + fechaHoraFormat.parse(fecha));
        try
        {
            sqlFecha = format.parse(fecha);
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return sqlFecha;
    }

    public static String convertir(Date fecha)
    {
        SimpleDateFormat format = new SimpleDateFormat(FORMATO_DIA_MES_AÑO);
        return format.format(fecha);
    }
    public static String convertirFechaHora(Date fecha)
    {
        SimpleDateFormat format = new SimpleDateFormat(FORMATO_FECHA_COMPLETA);
        return format.format(fecha);
    }
}
