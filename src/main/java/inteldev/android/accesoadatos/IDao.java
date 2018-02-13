package inteldev.android.accesoadatos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Operador on 9/05/14.
 */
public interface IDao {
    long insert(String table, ContentValues contentValues);

    long update(String table, ContentValues contentValues, String where);

    long delete(String table, String where);

    boolean codeExists(String table, String condicion);

    Cursor ejecutarConsultaSql(String sentencia);

    void ejecutarSentenciaSql(String sentencia);

    String getWhere(ContentValues contentValues, ArrayList<String> claves);

    SQLiteDatabase getSqLiteDatabase();
}
