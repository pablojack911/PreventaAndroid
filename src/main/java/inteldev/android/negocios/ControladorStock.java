package inteldev.android.negocios;

import android.database.Cursor;

import inteldev.android.accesoadatos.IDao;
import inteldev.android.presentation.MainActivity;

/**
 * Created by Operador on 25/08/2014.
 */
public class ControladorStock {

    IDao dao;
    public ControladorStock(IDao dao)
    {
        this.dao = dao;
    }

    public boolean hayStock(String articulo){
        boolean hayStock = false;

        Cursor cursor = dao.ejecutarConsultaSql("select bultos from Stock where" +
                " trim(idArticulo)='"+articulo+"'");

        if (cursor.moveToFirst() && cursor!=null){
            int bultos = cursor.getInt(0);
            if (bultos!=0){
                hayStock = true;
            }
        }
        return hayStock;
    }

}
