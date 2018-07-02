package inteldev.android.negocios;

import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;

import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.DetBonif;

/**
 * Created by Operador on 9/05/14.
 */
public class ControladorConvenio
{
    IDao dao;
    String cliente;

    public ControladorConvenio(IDao dao, String cliente)
    {
        this.dao = dao;
        this.cliente = cliente;
    }

    public DetBonif obtenerPorcentaje(String articulo, boolean sinCargos)
    {

        DetBonif detBonif = new DetBonif();

        float porcentaje = 0f;

        int idCabBonifPorLinea = 0;
        float porcentajePorLinea = 0f;
        int idCabBonifPorRubro = 0;
        float porcentajePorRubro = 0f;
        int idCabBonifPorArticulo = 0;
        float porcentajePorArticulo = 0f;
        int cant_sc = 0;
        String condicionCant_sc;

        if (sinCargos)
        {
            condicionCant_sc = "cant_sc<>0";
        }
        else
        {
            condicionCant_sc = "cant_sc=0";
        }

        String fechaActual = Fecha.obtenerFechaHoraActual();
        Cursor cursor = dao.ejecutarConsultaSql("select Alcance.idCabBonif," + "DetBonif.idLinea," + "DetBonif.idRubro," + "DetBonif.idArticulo," + "DetBonif.porcentaje," + "DetBonif.cant_sc," + "CabBonif.fDesde," + "CabBonif.fHasta," + "CabBonif.tipo," + "CabBonif.sinDescBase," + "CabBonif.permiteOe " + "from Alcance,Articulos " + "join CabBonif on CabBonif.idCabBonif = Alcance.idCabBonif " + "join DetBonif on DetBonif.idCabBonif = Alcance.idCabBonif " + "where Alcance.idcliente = '" + this.cliente + "' and " + "'" + fechaActual + "' >= CabBonif.fDesde and " + "'" + fechaActual + "' <= CabBonif.fHasta and " + "Articulos.idArticulo = '" + articulo + "' and (" + "DetBonif.idArticulo = '" + articulo + "' or " + "DetBonif.idRubro = Articulos.idRubro or " + "DetBonif.idLinea = Articulos.idLinea) and " + condicionCant_sc);

        if (cursor.moveToFirst())
        {

            do
            {
                docursor:
                if (! cursor.getString(1).isEmpty())
                {
                    idCabBonifPorLinea = cursor.getInt(0);
                    porcentajePorLinea = cursor.getFloat(4);
                    break docursor;
                }
                else
                {
                    if (! cursor.getString(2).isEmpty())
                    {
                        idCabBonifPorRubro = cursor.getInt(0);
                        porcentajePorRubro = cursor.getFloat(4);
                        break docursor;
                    }
                    else
                    {
                        if (! cursor.getString(3).isEmpty())
                        {
                            idCabBonifPorArticulo = cursor.getInt(0);
                            porcentajePorArticulo = cursor.getFloat(4);
                            cant_sc = cursor.getInt(5);
                            break docursor;
                        }
                    }
                }
            } while (cursor.moveToNext());
        }

        if (porcentajePorArticulo != 0 || cant_sc != 0)
        {
            detBonif.idCabBonif = idCabBonifPorArticulo;
            detBonif.porcentaje = porcentajePorArticulo;
            detBonif.cant_sc = cant_sc;
        }
        else
        {
            if (porcentajePorRubro != 0)
            {
                detBonif.idCabBonif = idCabBonifPorRubro;
                detBonif.porcentaje = porcentajePorRubro;
            }
            else
            {
                if (porcentajePorLinea != 0)
                {
                    detBonif.idCabBonif = idCabBonifPorLinea;
                    detBonif.porcentaje = porcentajePorLinea;
                }
            }
        }
        return detBonif;
    }

    public int obtenerSinCargosUtilizados(Date fecha, String articulo)
    {

        int sinCargos = 0;

        Cursor cursor = dao.ejecutarConsultaSql("SELECT SUM(entero) FROM detoper JOIN CabOper on caboper.Claveunica = detoper.claveunica where sincargo = 1 and idarticulo = " + articulo + " and CabOper.fecha = '" + fecha.toString() + "' and caboper.idcliente='" + cliente + "'");


        if (cursor.moveToFirst())
        {
            sinCargos = cursor.getInt(0);
        }

        return sinCargos;
    }
}
