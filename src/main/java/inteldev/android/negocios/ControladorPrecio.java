package inteldev.android.negocios;

import android.content.Context;
import android.database.Cursor;

import inteldev.android.accesoadatos.IDao;

/**
 * Created by Operador on 8/05/14.
 */
public class ControladorPrecio
{
    IDao dao;
    ControladorCliente controladorCliente;

    public ControladorPrecio(IDao dao, ControladorCliente controladorCliente)
    {
        this.dao = dao;
        this.controladorCliente = controladorCliente;

    }

    public Float obtener(Context context, String cliente, String articulo)
    {

        Float precio = 0f;

        String[] listas = controladorCliente.obtenerListas(cliente);
        if (listas != null)
        {
            for (String lista : listas)
            {
                Cursor cursor = dao.ejecutarConsultaSql("select precio from detlistas" +
                        " where idLista ='" + lista + "' and articulo = '" + articulo + "'");

                if (cursor.moveToFirst())
                {
                    return cursor.getFloat(0);
                }
            }
        }
        return precio;
    }

//    public Float obtener(Context context, String cliente, String articulo)
//    {
//
//        Float precio = 0f;
//
//        String lista = controladorCliente.obtenerLista(cliente);
//        if (lista != null) {
//            Cursor cursor = dao.ejecutarConsultaSql("select precio from detlistas" +
//                    " where idLista ='" + lista + "' and articulo = '" + articulo + "'");
//
//            if (cursor.moveToFirst())
//                precio = cursor.getFloat(0);
//        }
//
//        return precio;
//    }
}
