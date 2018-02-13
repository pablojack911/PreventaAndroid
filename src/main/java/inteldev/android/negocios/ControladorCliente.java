package inteldev.android.negocios;

import android.database.Cursor;

import inteldev.android.accesoadatos.IDao;

/**
 * Created by Operador on 21/05/14.
 */
public class ControladorCliente implements IControladorCliente
{

    IDao dao;

    public ControladorCliente(IDao dao)
    {
        this.dao = dao;
    }

    public String obtenerLista(String cliente)
    {

        String idLista = null;

        Cursor cursor = dao.ejecutarConsultaSql("select lista from clientes where idCliente='" + cliente + "'");

        if (cursor.moveToFirst() && cursor != null)
        {
            idLista = cursor.getString(0);
        }

        return idLista;
    }

    public String[] obtenerListas(String cliente)
    {
        Cursor cursor = dao.ejecutarConsultaSql("select lista from clientes where idCliente='" + cliente + "'");
        if (cursor.moveToFirst() && cursor != null)
        {
            return cursor.getString(0).split("-");
        }
        return null;
    }


}
