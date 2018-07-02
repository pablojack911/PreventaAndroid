package inteldev.android.negocios;

import android.database.Cursor;

import java.util.ArrayList;

import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.Articulo;
import inteldev.android.modelo.Linea;
import inteldev.android.modelo.Rubro;
import inteldev.android.presentation.vistaModelo.ArticuloDescuento;
import inteldev.android.presentation.vistaModelo.ArticuloSinCargo;

/**
 * Created by Operador on 23/05/14.
 */
public class ControladorArticulo extends ControladorBaseNegocio
{

    public ControladorArticulo(IDao dao)
    {
        super(dao);
    }

    public Articulo buscarArticulo(String codigo)
    {
        Articulo articulo = buscarArticuloPorBarras(codigo);
        if (articulo == null)
        {
            articulo = buscarArticuloPorCodigo(codigo);
        }
        return articulo;
    }

    public Articulo buscarArticuloPorCodigo(String codigo)
    {
        Articulo articulo = null;

        Cursor cursor = dao.ejecutarConsultaSql("select * from Articulos where idArticulo='" + codigo + "'");

        if (cursor.moveToFirst())
        {
            Mapeador<Articulo> articuloMapeador = new Mapeador<Articulo>(new Articulo());
            articulo = articuloMapeador.cursorToEntity(cursor);
        }

        return articulo;
    }

    public Articulo buscarArticuloPorBarras(String barra)
    {
        Articulo articulo = null;
        Cursor cursor = dao.ejecutarConsultaSql("select idArticulo from barras where barcode='" + barra + "'");
        if (cursor.moveToFirst())
        {
            String idarticulo = cursor.getString(0);
            articulo = buscarArticuloPorCodigo(idarticulo);
        }
        return articulo;
    }

    public int buscarRowIdArticuloPorCodigo(String idArticulo)
    {

        int rowid = 0;

        Cursor cursor = dao.ejecutarConsultaSql("select rowid from Articulos where idArticulo = '" + idArticulo + "'");

        if (cursor.moveToFirst())
        {
            rowid = cursor.getInt(0);
        }

        return rowid;
    }

    public int buscarRowIdLineaPorCodigo(String idLinea)
    {

        int rowid = 0;

        Cursor cursor = dao.ejecutarConsultaSql("select rowid from Lineas where idLinea = '" + idLinea + "'");

        if (cursor.moveToFirst())
        {
            rowid = cursor.getInt(0);
        }

        return rowid;
    }

    public int buscarRowIdRubroPorRowId(String idRubro)
    {

        int rowid = 0;

        Cursor cursor = dao.ejecutarConsultaSql("select rowid from Rubros where idRubro = '" + idRubro + "'");

        if (cursor.moveToFirst())
        {
            rowid = cursor.getInt(0);
        }
        return rowid;
    }

    public Articulo buscarArticuloPorRowId(Long rowid)
    {

        Articulo articulo = null;

        Cursor cursor = dao.ejecutarConsultaSql("select * from Articulos where rowid = '" + rowid + "'");

        if (cursor.moveToFirst())
        {
            Mapeador<Articulo> articuloMapeador = new Mapeador<Articulo>(new Articulo());
            articulo = articuloMapeador.cursorToEntity(cursor);
        }

        return articulo;
    }

    public Linea buscarLineaPorRowId(Long rowid)
    {

        Linea linea = null;

        Cursor cursor = dao.ejecutarConsultaSql("select * from Lineas where rowid = '" + rowid + "'");

        if (cursor.moveToFirst())
        {
            Mapeador<Linea> lineaMapeador = new Mapeador<Linea>(new Linea());
            linea = lineaMapeador.cursorToEntity(cursor);
        }

        return linea;
    }

    public Rubro buscarRubroPorRowId(Long rowid)
    {

        Rubro rubro = null;

        Cursor cursor = dao.ejecutarConsultaSql("select * from Rubros where rowid = '" + rowid + "'");

        if (cursor.moveToFirst())
        {
            Mapeador<Rubro> rubroMapeador = new Mapeador<Rubro>(new Rubro());
            rubro = rubroMapeador.cursorToEntity(cursor);
        }
        return rubro;
    }

    public ArrayList<ArticuloDescuento> obtenerDescuentos(String idClienteSeleccionado)
    {
        ArrayList<ArticuloDescuento> articuloDescuentos = new ArrayList<>();
        String query = "select art.idarticulo, art.nombre, db.porcentaje, dl.precio*db.porcentaje/100, cb.fhasta, alcance.idcabbonif " + "from detbonif db " + "inner join cabbonif cb on cb.idcabbonif=db.idcabbonif " + "inner join articulos art on (db.idlinea=art.idlinea and db.idrubro='' and db.idarticulo='') or (db.idarticulo=art.idarticulo and db.idlinea='' and db.idrubro='') or (db.idrubro=art.idrubro and db.idarticulo='' and db.idlinea='') " + "inner join clientes c on c.idCliente=alcance.idCliente " + "inner join detlistas dl on dl.articulo=art.idarticulo " + "inner join alcance on alcance.idcabbonif=cb.idcabbonif " + "where alcance.idcliente='" + idClienteSeleccionado + "' and " + "dl.idlista=c.lista and " + "date() between cb.fdesde and cb.fhasta " + "order by art.idarticulo";
        Cursor cursor = dao.ejecutarConsultaSql(query);
        while (cursor.moveToNext())
        {
            ArticuloDescuento articuloDescuento = new ArticuloDescuento();
            articuloDescuento.setIdArticulo(cursor.getString(0));
            articuloDescuento.setNombre(cursor.getString(1));
            articuloDescuento.setPorcentaje(cursor.getFloat(2));
            articuloDescuento.setPrecio(cursor.getFloat(3));
            String fecha = cursor.getString(4);
            articuloDescuento.setVencimiento(Fecha.convertir(fecha));
            articuloDescuento.setIdCabBonif(cursor.getString(5));
            articuloDescuentos.add(articuloDescuento);
        }

        return articuloDescuentos;
    }

    public ArrayList<ArticuloDescuento> obtenerDescuentosMayorista(String idClienteSeleccionado)
    {
        ArrayList<ArticuloDescuento> articuloDescuentos = new ArrayList<>();
        String query = "select art.idarticulo, art.nombre, db.porcentaje, dl.precio*db.porcentaje/100, cb.fhasta, alcance.idcabbonif  from detbonif db  inner join cabbonif cb on cb.idcabbonif=db.idcabbonif  inner join articulos art on (db.idlinea=art.idlinea and db.idrubro='' and db.idarticulo='') or (db.idarticulo=art.idarticulo and db.idlinea='' and db.idrubro='') or (db.idrubro=art.idrubro and db.idarticulo='' and db.idlinea='')  inner join clientes c on c.idCliente=alcance.idCliente  inner join detlistas dl on dl.articulo=art.idarticulo  inner join alcance on alcance.idcabbonif=cb.idcabbonif  where alcance.idcliente='" + idClienteSeleccionado + "' and  dl.idlista='02068' order by art.idarticulo";
        Cursor cursor = dao.ejecutarConsultaSql(query);
        while (cursor.moveToNext())
        {
            ArticuloDescuento articuloDescuento = new ArticuloDescuento();
            articuloDescuento.setIdArticulo(cursor.getString(0));
            articuloDescuento.setNombre(cursor.getString(1));
            articuloDescuento.setPorcentaje(cursor.getFloat(2));
            articuloDescuento.setPrecio(cursor.getFloat(3));
            String fecha = cursor.getString(4);
            articuloDescuento.setVencimiento(Fecha.convertir(fecha));
            articuloDescuento.setIdCabBonif(cursor.getString(5));
            articuloDescuentos.add(articuloDescuento);
        }

        return articuloDescuentos;
    }

    public ArrayList<ArticuloSinCargo> obtenerSinCargos(String idClienteSeleccionado)
    {
        ArrayList<ArticuloSinCargo> articuloSinCargos = new ArrayList<>();
        String query = "select art.idarticulo, art.nombre, db.cant_sc, cb.fhasta, alcance.idcabbonif " + "from detbonif db " + "inner join cabbonif cb on cb.idcabbonif=db.idcabbonif " + "inner join articulos art on db.idarticulo=art.idarticulo " + "inner join alcance on alcance.idcabbonif=cb.idcabbonif " + "where alcance.idcliente='" + idClienteSeleccionado + "' and " + "db.cant_sc > 0 and " + "date() between cb.fdesde and cb.fhasta";
        Cursor cursor = dao.ejecutarConsultaSql(query);
        while (cursor.moveToNext())
        {
            ArticuloSinCargo articuloSinCargo = new ArticuloSinCargo();
            articuloSinCargo.setIdArticulo(cursor.getString(0));
            articuloSinCargo.setNombre(cursor.getString(1));
            articuloSinCargo.setSinCargo(cursor.getInt(2));
            articuloSinCargo.setVencimiento(Fecha.convertir(cursor.getString(3)));
            articuloSinCargo.setIdCabBonif(cursor.getString(4));
            articuloSinCargos.add(articuloSinCargo);
        }
        return articuloSinCargos;
    }

    public ArrayList<Articulo> obtenerFolder()
    {
        ArrayList<Articulo> articulos = new ArrayList<>();
        String query = "select articulos.* from articulos inner join detlistas on Articulos.idArticulo = detListas.articulo where detListas.folder>0 order by Articulos.nombre";
        Cursor cursor = dao.ejecutarConsultaSql(query);
        Mapeador<Articulo> mapeador = new Mapeador<>(new Articulo());
        articulos = mapeador.cursorToList(cursor);
        return articulos;
    }
}
