package inteldev.android.accesoadatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Operador on 9/05/14.
 */
public interface IDao
{
    long insert(String table, ContentValues contentValues);

    //    long update(String table, ContentValues contentValues, String where);
    long update(String table, ContentValues contentValues, String where, String[] args);

//    long delete(String table, String where);

    long delete(String table, String where, String[] args);

//    boolean codeExists(String table, String condicion);

    boolean codeExists(String table, String where, String[] args);

    Cursor ejecutarConsultaSql(String sentencia);

    void ejecutarSentenciaSql(String sentencia);

//    String getWhere(ContentValues contentValues, ArrayList<String> claves);

    String[] getArgs(ContentValues contentValues, ArrayList<String> claves);

    String getWhere(ArrayList<String> claves);

    SQLiteDatabase getSqLiteDatabase();

    Cursor queryByCode(String table, String where, String[] args);
}
