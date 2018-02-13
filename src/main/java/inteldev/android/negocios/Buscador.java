package inteldev.android.negocios;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import inteldev.android.accesoadatos.Dao;

/**
 * GenericSearcher
 *
 *
 * @author Pablo Vigolo
 * @version 1.0 28 february 2014
 */
public class Buscador<T> extends GenericBase<T>
{
    private Dao controladorDb;
    private Mapeador<T> mapeador;

    public Buscador(Context context, T entity)
    {
        super(entity);
        this.controladorDb = new Dao(context);
        this.mapeador = new Mapeador<T>(entity);
    }

    public T searchByCode(String value)
    {
        Cursor cursor = queryByCode(value);
        cursor.moveToFirst();
        return this.mapeador.cursorToEntity(cursor);
    }

    public boolean codeExists(String value)
    {
        Cursor cursor = queryByCode(value);
        return cursor.getCount() > 0;
    }

    private Cursor queryByCode(String value)
    {

        Cursor cursor = null;

        try {
            cursor = controladorDb.getSqLiteDatabase().rawQuery("select * from "+this.table+" where "+this.field+" = "+value, null);
        }
        catch (Exception e)
        {
            Log.w("GenericSearcher", e.getMessage());
        }
        return  cursor;
    }
}
