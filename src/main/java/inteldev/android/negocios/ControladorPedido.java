package inteldev.android.negocios;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;

import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.CabOper;
import inteldev.android.modelo.DetOper;
import inteldev.android.modelo.OferOper;
import inteldev.android.presentation.Mayorista.PrecargaManager.Precarga;
import inteldev.android.presentation.vistaModelo.DetallePedido;

/**
 * Created by Operador on 20/05/14.
 */
public class ControladorPedido
{
    private IDao dao;

    public ControladorPedido(IDao dao)
    {
        this.dao = dao;
    }

    public String ObtenerClaveUnicaPedido(String idCliente, java.sql.Date fecha)
    {
        String claveUnica;

        Cursor cursor = this.dao.ejecutarConsultaSql("select claveUnica from CabOper" + " where idCliente = '" + idCliente + "'" + " and fecha = '" + fecha.toString() + "'");

        if (cursor != null && cursor.moveToFirst())
        {
            claveUnica = cursor.getString(0);
        }
        else
        {
            claveUnica = null;
        }

        return claveUnica;
    }


    public ArrayList<OferOper> obtenerOfertaPedido(String claveUnica)
    {
        ArrayList<OferOper> oferOpers = null;

        Cursor cursor = this.dao.ejecutarConsultaSql("select * from oferOper" + " where claveunica = '" + claveUnica + "'");

        if (cursor != null && cursor.moveToFirst())
        {
            Mapeador<OferOper> oferOperMapeador = new Mapeador<OferOper>(new OferOper());
            oferOpers = oferOperMapeador.cursorToList(cursor);
        }

        return oferOpers;
    }

    public ArrayList<DetallePedido> consultarPedidos(String idCliente)
    {
        ArrayList<DetallePedido> listadetallePedido = new ArrayList<>();
        Cursor cursor = dao.ejecutarConsultaSql("select cabOper.idCliente, cabOper.fecha, detOper.idArticulo, detOper.entero, detOper.fraccion, detOper.precio, detOper.descuento, detOper.idFila, detOper.oferta, detOper.claveUnica, detOper.unidadVenta from cabOper inner join detOper on detOper.claveUnica = cabOper.claveUnica where cabOper.idCliente = '" + idCliente + "' and cabOper.fecha = '" + Fecha.obtenerFechaActual().toString() + "'");
        if (cursor != null && cursor.moveToFirst())
        {
            Mapeador<DetallePedido> detallePedidoMapeador = new Mapeador<DetallePedido>(new DetallePedido());
            listadetallePedido = detallePedidoMapeador.cursorToList(cursor);
        }
        return listadetallePedido;
    }

    public ArrayList<DetallePedido> consultarPedidos(String idCliente, String claveUnicaCabOper)
    {
        ArrayList<DetallePedido> listadetallePedido = new ArrayList<>();
        Cursor cursor = dao.ejecutarConsultaSql("select cabOper.idCliente, cabOper.fecha, detOper.idArticulo, detOper.entero, detOper.fraccion, detOper.precio, detOper.descuento, detOper.idFila, detOper.oferta, detOper.claveUnica, detOper.unidadVenta from cabOper inner join detOper on detOper.claveUnica = cabOper.claveUnica where cabOper.idCliente = '" + idCliente + "' and cabOper.fecha = '" + Fecha.obtenerFechaActual().toString() + "' and cabOper.claveUnica='" + claveUnicaCabOper + "'");
        if (cursor != null && cursor.moveToFirst())
        {
            Mapeador<DetallePedido> detallePedidoMapeador = new Mapeador<DetallePedido>(new DetallePedido());
            listadetallePedido = detallePedidoMapeador.cursorToList(cursor);
        }
        return listadetallePedido;
    }

    public ArrayList<Precarga> consultarPrecargas(String idCliente)
    {
        Cursor cursor = dao.ejecutarConsultaSql("select caboper.rowid, detOper.claveUnica, sum((precio - (precio*(descuento/100)))*((entero * unidadVenta)+ fraccion))  as importeTotal from cabOper inner join detOper on detOper.claveUnica = cabOper.claveUnica where cabOper.idCliente = '" + idCliente + "' and cabOper.fecha = '" + Fecha.obtenerFechaActual() + "' group by detOper.claveUnica order by caboper.rowid");
        ArrayList<Precarga> precargaArrayList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst())
        {
            int i = 1;
            do
            {
                Precarga precarga = new Precarga();
                precarga.nombre = "PRECARGA " + i;
                precarga.idCabOper = cursor.getString(1);
                precarga.totalPrecarga = cursor.getFloat(2);
                precargaArrayList.add(precarga);
                i++;
            } while (cursor.moveToNext());
        }
        return precargaArrayList;
    }

    public String obtenerArticuloPedido(String claveUnica)
    {
        Cursor cursorOferOper = dao.ejecutarConsultaSql("select idArticulo from oferoper where claveUnica = '" + claveUnica + "'");
        if (cursorOferOper != null && cursorOferOper.moveToFirst())
        {
            return cursorOferOper.getString(0);
        }
        return "";
    }

    public void borrarDetOper(String idFila)
    {
        dao.delete("detOper", "idFila=?", new String[]{idFila});
    }

    public void borrarOferOper(String idFila)
    {
        dao.delete("detOper", "idFila=?", new String[]{idFila});
        dao.delete("oferOper", "claveUnica=?", new String[]{idFila});
    }

    public int calcularBultos(String idCliente)
    {
        int bultos = 0;
        Cursor cursor = dao.ejecutarConsultaSql("select sum(detOper.entero) from cabOper inner join detOper on detOper.claveUnica = cabOper.claveUnica where cabOper.idCliente = '" + idCliente + "' and cabOper.fecha = '" + Fecha.obtenerFechaActual().toString() + "'");
        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                int bultosPedidos = cursor.getInt(0);
                bultos = bultos + bultosPedidos;
            } while (cursor.moveToNext());
        }
        return bultos;
    }

    public void grabarCabOper(CabOper cabOper)
    {
        Mapeador<CabOper> cabOperMapeador = new Mapeador<CabOper>(cabOper);
        ContentValues contentValues = cabOperMapeador.entityToContentValues();
        dao.insert("cabOper", contentValues);
    }

    public void grabarDetOper(DetOper detOper)
    {
        Mapeador<DetOper> detOperMapeador = new Mapeador<DetOper>(detOper);
        ContentValues contentValuesdetOper = detOperMapeador.entityToContentValues();
        dao.insert("detOper", contentValuesdetOper);
    }

    public void grabarOferOper(OferOper oferOper)
    {
        Mapeador<OferOper> oferOperMapeador = new Mapeador<OferOper>(oferOper);
        ContentValues contentValuesOferOper = oferOperMapeador.entityToContentValues();
        dao.insert("oferOper", contentValuesOferOper);
    }

    public float calcularTotal(String idClienteSeleccionado)
    {
        ArrayList<DetallePedido> detallePedidos = consultarPedidos(idClienteSeleccionado);
        float total = 0f;
        for (DetallePedido pedido : detallePedidos)
        {
            total = calcularIntermedio(total, pedido);
            //            if (pedido.getOferta() != null && pedido.getOferta().length() > 0)
            //            {
            //                total += pedido.precio;
            //            }
            //            else
            //            {
            //                float precio = pedido.precio;
            //                int bultos = pedido.entero;
            //                int fracciones = pedido.fraccion;
            //                int unidadVenta = pedido.unidadVenta;
            //                int cantidad = bultos * unidadVenta + fracciones;
            //                float descuento = pedido.descuento;
            //                float importeDescuento = precio * (descuento / 100);
            //                total += ((precio - importeDescuento) * cantidad);
            //            }
        }
        return total;
    }

    public float calcularTotal(String idClienteSeleccionado, String claveUnicaCabOper)
    {
        ArrayList<DetallePedido> detallePedidos = consultarPedidos(idClienteSeleccionado, claveUnicaCabOper);
        float total = 0f;
        for (DetallePedido pedido : detallePedidos)
        {
            total = calcularIntermedio(total, pedido);
        }
        return total;
    }

    private float calcularIntermedio(float total, DetallePedido pedido)
    {
        if (pedido.getOferta() != null && pedido.getOferta().length() > 0)
        {
            total += pedido.precio;
        }
        else
        {
            float precio = pedido.precio;
            int bultos = pedido.entero;
            int fracciones = pedido.fraccion;
            int unidadVenta = pedido.unidadVenta;
            int cantidad = bultos * unidadVenta + fracciones;
            float descuento = pedido.descuento;
            float importeDescuento = precio * (descuento / 100);
            total += ((precio - importeDescuento) * cantidad);
        }
        return total;
    }

    public String crearNuevaPrecarga(String idCliente, String loginUsuario)
    {
        CabOper cabOper = new CabOper();
        cabOper.claveUnica = java.util.UUID.randomUUID().toString();
        cabOper.idOperacion = 1;
        cabOper.idCliente = idCliente;
        cabOper.fecha = Fecha.obtenerFechaActual();
        cabOper.fechaEntrega = Fecha.obtenerFechaActual();
        cabOper.idVendedor = loginUsuario;
        cabOper.enviado = 0;
        cabOper.domicilio = "";
        grabarCabOper(cabOper);
        return cabOper.claveUnica;
    }

    public boolean existeEnPrecarga(String claveUnicaCabOper, String idArticulo)
    {
        String query = "select detOper.idArticulo from detoper where detOper.claveUnica='" + claveUnicaCabOper + "' and detOper.idArticulo='" + idArticulo + "' union select oferOper.idArticulo from oferOper inner join detOper on detOper.idFila=oferOper.claveunica where detOper.claveunica='" + claveUnicaCabOper + "' and oferOper.idArticulo='" + idArticulo + "'";
        Cursor cursor = this.dao.ejecutarConsultaSql(query);
        return cursor != null && cursor.moveToNext();
    }
}
