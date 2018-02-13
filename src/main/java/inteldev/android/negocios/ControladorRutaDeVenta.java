package inteldev.android.negocios;

import android.database.Cursor;

import java.util.ArrayList;

import inteldev.android.Enum.OrdenRutaDeVenta;
import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.Cliente;

/**
 * Created by Operador on 31/05/14.
 */
public class ControladorRutaDeVenta
{
    IDao dao;

    public ControladorRutaDeVenta(IDao dao)
    {
        this.dao = dao;
    }

    public ArrayList<Cliente> obtenerClientesRutaDeVenta(String loginUsuario, int diaAConsultarSegunArray, int seleccionVisitado, OrdenRutaDeVenta ordenRutaDeVenta)
    {


        String dia = "";

        switch (diaAConsultarSegunArray)
        {
            case 0:
                dia = "dom=1";
                break;

            case 1:
                dia = "lun=1";
                break;

            case 2:
                dia = "mar=1";
                break;

            case 3:
                dia = "mie=1";
                break;

            case 4:
                dia = "jue=1";
                break;

            case 5:
                dia = "vie=1";
                break;

            case 6:
                dia = "sab=1";
                break;

            case 7:
                dia = "1=1";
        }

        String whereVisitado;
        if (seleccionVisitado == 0)
        {
            whereVisitado = "and not exists ( select claveunica from caboper where " +
                    "caboper.idCliente = cl.idCliente and caboper.fecha = '" + Fecha.obtenerFechaActual().toString() + "')" +
                    "and not exists (select distinct idCliente from PosicionesGPS where PosicionesGPS.idCliente = cl.idCliente " +
                    "and PosicionesGPS.usuario='" + loginUsuario + "' " +
                    "and strftime('%Y-%m-%d', PosicionesGPS.Fecha)== '" + Fecha.obtenerFechaActual().toString() + "')";

        }
        else
        {
            if (seleccionVisitado == 1)
            {
                whereVisitado = "and (exists ( select claveunica from caboper where " +
                        "caboper.idCliente = cl.idCliente and caboper.fecha = '" + Fecha.obtenerFechaActual().toString() + "')" +
                        "or  exists (select distinct idCliente from PosicionesGPS where PosicionesGPS.idCliente = cl.idCliente " +
                        "and PosicionesGPS.usuario='" + loginUsuario + "' " +
                        "and strftime('%Y-%m-%d', PosicionesGPS.Fecha)== '" + Fecha.obtenerFechaActual().toString() + "'))";

            }
            else
            {
                whereVisitado = "";
            }
        }

        String stringOrdenLista = null;

        switch (ordenRutaDeVenta)
        {
            case Codigo:
                stringOrdenLista = "cl.idCliente";
                break;
            case Nombre:
                stringOrdenLista = "cl.nombre";
                break;
            case Domicilio:
                stringOrdenLista = "cl.domicilio";
                break;
            case Cuit:
                stringOrdenLista = "cl.cuit";
            case Recorrido:
                stringOrdenLista = "CR.idRutaVenta,CR.recorrido";
        }

        ArrayList<Cliente> listaClientes = new ArrayList<Cliente>();

        String query = "select distinct cl.idCliente,cl.nombre, cl.domicilio, cl.telefono, cl.cuit " +
                "from rutaventas AS RV " +
                "JOIN configrutas as CR ON CR.idRutaVenta = RV.IdRutaVenta " +
                "JOIN clientes as cl on cl.idCliente = cr.IdCliente " +
                "where " + dia + " and (vendedor = '" + loginUsuario + "' or suplente = '" + loginUsuario + "')" +
                " " + whereVisitado +
                " order by " + stringOrdenLista;

        Cursor cursor = dao.ejecutarConsultaSql(query);

        Mapeador<Cliente> clienteMapeador = new Mapeador<Cliente>(new Cliente());

        listaClientes = clienteMapeador.cursorToList(cursor);

        return listaClientes;

    }
}
