package inteldev.android.negocios;

/**
 * Repository
 *
 * @author Pablo Vigolo
 * @version 1.0 10 february of 2014
 */


public class Repository<T> {

    public Grabador<T> grabador;
    public Buscador<T> buscador;

    public Repository(android.content.Context context,T entity)
    {
        this.buscador = new Buscador<T>(context,entity);
        this.grabador = new Grabador<T>(context,this.buscador,entity);
    }


}