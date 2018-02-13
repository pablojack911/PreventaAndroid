package inteldev.android.negocios;

import inteldev.android.accesoadatos.IDao;

/**
 * Created by Operador on 20/05/14.
 */
public abstract class ControladorBaseNegocio
{
    IDao dao;

    public ControladorBaseNegocio(IDao dao)
    {
        this.dao = dao;
    }
}
