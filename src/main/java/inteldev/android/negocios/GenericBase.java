package inteldev.android.negocios;

import android.util.Log;

import java.util.ArrayList;

import inteldev.android.accesoadatos.MapeadorNombreEntidadTabla;

/**
 * GenericBase
 * <p>
 * Provides common methods for generic classes.
 *
 * @author Gabriel Verdi
 * @version 1.0 06 march of 2014
 */
public abstract class GenericBase<T> {

    protected Class<?> entityClass;
    /* Nombre de la Tabla */
    protected String table;
    /**
     * name of the key field
     */
    protected String field;
    /**
     * value of the key field
     */
    protected String value;
    protected T entity;

    protected ArrayList<T> arrayList;

    public GenericBase(T entity) {
        try {
            this.entity = entity;
            this.entityClass = entity.getClass();
            this.table = MapeadorNombreEntidadTabla.getInstancia().getTabla(this.entityClass.getSimpleName());
            this.field = this.entityClass.getMethod("getKey").invoke(entity).toString();
            this.value = this.entityClass.getField(this.field).get(entity).toString();
        } catch (NullPointerException ex) {
            Log.e("GenericBase", "Null PointerException - " + ex.getMessage());
            this.value = "0";
        } catch (NoSuchMethodException ex) {
            Log.e("GenericBase", "Method getKey does not exist - " + ex.getMessage());
        } catch (IllegalAccessException ex) {
            Log.e("GenericBase", "Can't access getKey method - " + ex.getMessage());
        } catch (NoSuchFieldException ex) {
            Log.e("GenericBase", "Field Key does not exist - " + ex.getMessage());
        } catch (Exception ex) {
            Log.e("GenericBase", ex.getMessage());
        }
    }

}
