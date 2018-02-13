package inteldev.android.accesoadatos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * DbManager
 *
 * @author Pablo Vigolo
 * @version 1.0 06 february of 2014
 */
public class Dao implements IDao
{

    //    private DbHelper dbHelper;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public Dao(Context context)
    {
//        dbHelper = new DbHelper(context);
        dbHelper = DatabaseHelper.getInstance(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
    }

    @Override
    public long insert(String table, ContentValues contentValues)
    {
        long res = sqLiteDatabase.insert(table, null, contentValues);
        return res;
    }

    @Override
    public long update(String table, ContentValues contentValues, String where)
    {
        long res = sqLiteDatabase.update(table, contentValues, where, null);
        return res;
    }

    @Override
    public long delete(String table, String where)
    {
        return sqLiteDatabase.delete(table, where, null);
    }

    @Override
    public boolean codeExists(String table, String condicion)
    {
        Cursor cursor = queryByCode(table, condicion);
        return cursor.getCount() > 0;
    }

    @Override
    public Cursor ejecutarConsultaSql(String sentencia)
    {
        Cursor cursor = null;

        try
        {
            cursor = this.getSqLiteDatabase().rawQuery(sentencia, null);
        }
        catch (Exception e)
        {
            Log.w("GenericSearcher", e.getMessage());
        }
        return cursor;
    }

    private Cursor queryByCode(String table, String condicion)
    {

        Cursor cursor = null;

        try
        {
            cursor = this.getSqLiteDatabase().rawQuery("select * from " + table + " where " + condicion, null);
        }
        catch (Exception e)
        {
            Log.w("GenericSearcher", e.getMessage());
        }
        return cursor;
    }

    @Override
    public String getWhere(ContentValues contentValues, ArrayList<String> claves)
    {
        String condicion = null;
        String campo;
        String valor;

        Iterator<String> i = claves.iterator();
        while (i.hasNext())
        {
            campo = i.next();
            valor = contentValues.getAsString(campo);

            if (condicion == null)
            {
                condicion = campo + " = " + "'" + valor + "'";
            }
            else
            {
                condicion = condicion + "and " + campo + " = " + "'" + valor + "'";
            }
        }

        return condicion;

    }

    public void ejecutarSentenciaSql(String sentencia)
    {

        sqLiteDatabase.execSQL(sentencia);
    }

    @Override
    public SQLiteDatabase getSqLiteDatabase()
    {
        return sqLiteDatabase;
    }

}