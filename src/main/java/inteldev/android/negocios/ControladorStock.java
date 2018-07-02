package inteldev.android.negocios;

import android.database.Cursor;

import inteldev.android.accesoadatos.IDao;

/**
 * Created by Operador on 25/08/2014.
 */
public class ControladorStock
{

    IDao dao;

    public ControladorStock(IDao dao)
    {
        this.dao = dao;
    }

    public boolean hayStock(String articulo)
    {
        boolean hayStock = false;

        Cursor cursor = dao.ejecutarConsultaSql("select bultos from Stock where" + " trim(idArticulo)='" + articulo + "'");

        if (cursor != null && cursor.moveToFirst())
        {
            int bultos = cursor.getInt(0);
            if (bultos != 0)
            {
                hayStock = true;
            }
        }
        return hayStock;
    }

    public boolean hayStock(String articulo, int bultosPedidos, int fraccionPedidos)
    {
        boolean hayStock = false;

        Cursor cursor = dao.ejecutarConsultaSql("select bultos, unidades from Stock where trim(idArticulo)='" + articulo + "'");
        if (cursor != null && cursor.moveToFirst())
        {
            int bultos = cursor.getInt(0);
            int fraccion = cursor.getInt(1);
            if (bultos >= 0 || fraccion >= 0)
            {
                hayStock = true;
            }
        }
        return hayStock;
    }

}
