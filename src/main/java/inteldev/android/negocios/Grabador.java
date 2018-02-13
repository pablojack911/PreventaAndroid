package inteldev.android.negocios;

import android.content.ContentValues;
import android.util.Log;

import inteldev.android.accesoadatos.Dao;

/**
 * GenericRecorder
 *
 * @author Gabriel verdi
 * @version 1.0 02 february of 2014
 */
public class Grabador<T> extends GenericBase<T>
{
    private Dao controladorDb;
    private Buscador<T> buscador;

    public Grabador(android.content.Context context, Buscador<T> buscador, T entity)
    {
        super(entity);
        this.controladorDb = new Dao(context);
        this.buscador = buscador;
    }

    /**
     * Saves a entity
     * @return number of affected rows
     */
    public long save()
    {
        try {
            Mapeador mapeador = new Mapeador(this.entity);
            ContentValues cv = mapeador.entityToContentValues();
            boolean codeExists = buscador.codeExists(value);
            long result = 0;

            //String tabla = MapeadorNombreEntidadTabla.getInstancia().getTabla(this.entityClass.getSimpleName()) ;

            if (codeExists == false) {

                //result = this.dbManager.insert(this.entityClass.getSimpleName(), cv);
                result = this.controladorDb.insert(this.table, cv);
            }
            else {
                //result = this.dbManager.update(this.entityClass.getSimpleName(), cv, field + " = " + value);
                result = this.controladorDb.update(this.table, cv, field + " = " + value);
            }
            return result;
        }
        catch (Exception e)
        {
            Log.w("GenericRecorder",e.getMessage());
            return 0;
        }
    }
}
