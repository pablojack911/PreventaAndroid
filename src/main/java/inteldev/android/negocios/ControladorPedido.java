package inteldev.android.negocios;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.OferOper;

/**
 * Created by Operador on 20/05/14.
 */
public class ControladorPedido
{
    private IDao dao;

    public ControladorPedido(IDao dao)
    {
        this.dao = dao;
    }

    public String ObtenerClaveUnicaPedido(String idCliente, java.sql.Date fecha)
    {
        String claveUnica;

        Cursor cursor = this.dao.ejecutarConsultaSql("select claveUnica from CabOper" +
                " where idCliente = '" + idCliente + "'" +
                " and fecha = '"+fecha.toString()+"'");

        if (cursor != null && cursor.moveToFirst()) {
            claveUnica = cursor.getString(0);
        } else
        {
            claveUnica = null;
        }

        return claveUnica;
    }


    public ArrayList<OferOper> obtenerOfertaPedido(String claveUnica)
    {
        ArrayList<OferOper> oferOpers=null;

        Cursor cursor = this.dao.ejecutarConsultaSql("select * from oferOper" +
                " where claveunica = '"+claveUnica+"'");

        if (cursor != null && cursor.moveToFirst()) {

            Mapeador<OferOper> oferOperMapeador = new Mapeador<OferOper>(new OferOper());
            oferOpers =  oferOperMapeador.cursorToList(cursor);
        }

        return oferOpers;
    }
}
