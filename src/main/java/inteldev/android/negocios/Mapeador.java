package inteldev.android.negocios;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Mapper
 *
 * @author Pablo Vigolo
 * @version 1.0 6 february of 2014
 */
public class Mapeador<T> extends GenericBase<T>
{

    private final String DEBUG_TAG = "Mapper";

    private Map<String, String> map;
    private Field[] fields;

    public Mapeador(T entity)
    {
        super(entity);
        this.map = new HashMap<String, String>();
        this.fields = this.entityClass.getFields();
        try
        {
            for (int i = 0; i < fields.length; i++)
            {
                //por defecto asumo que los campos entidad = campos base de datos
                String campo = fields[i].getName();
                if (!campo.equals("serialVersionUID") && (!campo.equals("PARCELABLE_WRITE_RETURN_VALUE")) && (!campo.equals("CREATOR")) && (!campo.equals("CONTENTS_FILE_DESCRIPTOR")) && (!campo.equals("PARCELABLE_ELIDE_DUPLICATES")) && (!campo.equals("$change")))
                {
                    map.put(campo, campo);
                }
            }
        }
        catch (IndexOutOfBoundsException exception)
        {
            Log.w(DEBUG_TAG, "Fields out of bound exception.");
        }
    }

    public ContentValues entityToContentValues()
    {
        ContentValues contentValues = new ContentValues();
        Iterator it = this.map.entrySet().iterator();
        Object field = null;
        while (it.hasNext())
        {
            Map.Entry e = (Map.Entry) it.next();
            try
            {
                try
                {
                    field = entityClass.getField(e.getValue().toString()).get(entity);
                }
                catch (IllegalAccessException exception)
                {
                    Log.w(DEBUG_TAG, "Can't access field" + e.getValue().toString());
                }
                if (field != null)
                {
                    contentValues.put(e.getKey().toString(), field.toString());
                }
            }
            catch (NoSuchFieldException ex)
            {
                Log.w(DEBUG_TAG, "Can't access field" + e.getValue().toString());
            }
        }
        return contentValues;
    }

    /***
     * Gets a cursor and return the mapped entity.
     *
     * @param cursor cursor with entity data
     * @return mapped entity
     */
    public T cursorToEntity(Cursor cursor)
    {

        int i = 0;
        try
        {

            this.entity = (T) this.entityClass.newInstance();

            for (i = 0; i < fields.length; i++)
            {
                String nombreColumna = fields[i].getName();
                int columnIndex = cursor.getColumnIndex(nombreColumna);
                if (columnIndex != -1)//no existe una propiedad con
                {
                    switch (cursor.getType(columnIndex))
                    {
                        // FIELD_TYPE_NULL
                        case 0:
                            break;
                        //FIELD_TYPE_INTEGER
                        case 1:
                            fields[i].set(entity, cursor.getInt(columnIndex));
                            break;
                        //FIELD_TYPE_FLOAT
                        case 2:
                            fields[i].set(entity, cursor.getFloat(columnIndex));
                            break;
                        //FIELD_TYPE_STRING
                        case 3:
                            if (!(fields[i].getGenericType().toString().equals("class java.util.Date")))
                            {

                                fields[i].set(entity, cursor.getString(columnIndex));
                            }
                            break;
                        //FIELD_TYPE_BLOB
                        case 4:
                            fields[i].set(entity, cursor.getBlob(columnIndex));
                    }
                }
                else
                {
                    Log.i("Mapeador.cursorToEntity", fields[i].getName() + " no esta presente en el cursor.");
                }
            }
        }
        catch (IndexOutOfBoundsException ex)
        {
            Log.w(DEBUG_TAG, "Fields out of bound exception");
        }
        catch (IllegalAccessException ex)
        {
            Log.e(DEBUG_TAG, "Can't set field" + fields[i]);
        }
        catch (Exception e)
        {
            System.out.println("NO ANDAAAAA " + e.getMessage() + " " + fields[i].getName());
        }
        return this.entity;
    }

    public ArrayList<T> cursorToList(Cursor cursor)
    {
        this.arrayList = new ArrayList<T>();

        T objeto;

        if (cursor.moveToFirst())
        {
            //   cursor.moveToFirst();
            do
            {
//                this.cursorToEntity(cursor);

                objeto = cursorToEntity(cursor);
                arrayList.add(objeto);

            } while (cursor.moveToNext());

        }

        return arrayList;

    }


}


