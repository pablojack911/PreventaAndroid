package inteldev.android.negocios;

import android.database.Cursor;

import java.util.ArrayList;

import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.OferEsp;
import inteldev.android.modelo.Ofer_Det;

/**
 * Created by Operador on 20/05/14.
 */
public class ControladorOferta extends ControladorBaseNegocio
{
    IControladorCliente controladorCliente;

    public ControladorOferta(IDao dao, IControladorCliente controladorCliente)
    {
        super(dao);
        this.controladorCliente = controladorCliente;
    }

    public ArrayList<OferEsp> obtenerOfertas(String idCliente)
    {
        ArrayList<OferEsp> listaOferEsp = new ArrayList<OferEsp>();

        String idLista = controladorCliente.obtenerLista(idCliente);

        Cursor cursor = dao.ejecutarConsultaSql("select codigo as Codigo, nombre as Nombre from oferEsp" +
                " where lista = '"+idLista+"' or lista = ''");

        Mapeador<OferEsp> oferEspMapeador  = new Mapeador<OferEsp>(new OferEsp());

        listaOferEsp = oferEspMapeador.cursorToList(cursor);

        return listaOferEsp;
    }

    public ArrayList<String> obtenerMotivosNoCompra()
    {
        ArrayList<String> listaMotivosNoCompra = new ArrayList<String>();

        listaMotivosNoCompra.add("Cerrado");
        listaMotivosNoCompra.add("No estaba el responsable");
        listaMotivosNoCompra.add("Tiene Stock");
        listaMotivosNoCompra.add("No tiene plata");

        return listaMotivosNoCompra;
    }
    public boolean buscarOfertaPorCodigo(String codigo)
    {

        Cursor cursor = dao.ejecutarConsultaSql("select codigo as Codigo," +
                "nombre as Nombre" +
                " from oferEsp" +
                " where codigo='"+codigo+"'");

        return cursor != null && cursor.moveToFirst();
    }

    public OferEsp obtenerOfertaPorCodigo(String codigo){

        OferEsp oferEsp=null;

        Cursor cursor = dao.ejecutarConsultaSql("select codigo as Codigo, " +
                "nombre as Nombre," +
                "d_cant as D_Cant," +
                "h_cant as H_Cant," +
                "tipo," +
                "ctrlcant as CtrlCant," +
                "base_dcant as Base_Dcant," +
                "base_hcant as Base_Hcant," +
                "comb_oblig as Comb_Oblig," +
                "comb_cantb as Comb_CantC," +
                "bon_cada as Bon_Cada," +
                "bon_tipo as Bon_Tipo," +
                "bon_cant as Bon_Cant," +
                "tomaconv as TomaConv"+
                " from oferEsp" +
                " where codigo='"+codigo+"'");

        if (cursor != null && cursor.moveToFirst()) {

            Mapeador<OferEsp> oferEspMapeador = new Mapeador<OferEsp>(new OferEsp());

            oferEsp = oferEspMapeador.cursorToEntity(cursor);

        }

        return oferEsp;

    }

    public ArrayList<Ofer_Det> obtenerArticulosOferta(String codigo, String tipo)
    {
        Mapeador<Ofer_Det> ofer_detMapeador = new Mapeador<Ofer_Det>(new Ofer_Det());

        ArrayList<Ofer_Det> ofer_dets = new ArrayList<Ofer_Det>();

        Cursor cursor = consultarDetalleOferta(codigo,tipo);

        if (cursor.moveToFirst()) {
            do {

                if (!cursor.getString(5).trim().isEmpty() && !cursor.getString(5).trim().equals("0")) {


                    Cursor cursorArticulo = dao.ejecutarConsultaSql("select idArticulo from Articulos" +
                            " where idArticulo='" + cursor.getString(5).trim() + "'");

                    if (cursorArticulo.moveToFirst())
                        ofer_dets.add(ofer_detMapeador.cursorToEntity(cursor));

                }
                else {
                    if (!cursor.getString(4).trim().isEmpty()) {

                        Cursor cursorArticulosRubro = dao.ejecutarConsultaSql("select idArticulo from Articulos" +
                                " where idRubro='" + cursor.getString(4) + "'");
                        if (cursorArticulosRubro.moveToFirst()) {
                            do {
                                Ofer_Det ofer_det = new Ofer_Det();
                                ofer_det = ofer_detMapeador.cursorToEntity(cursor);
                                ofer_det.Articulo = cursorArticulosRubro.getString(0);

                                ofer_dets.add(ofer_det);

                            } while (cursorArticulosRubro.moveToNext());
                        }
                    } else {
                        if (!cursor.getString(3).trim().isEmpty()) {
                            Cursor cursorArticulosLinea = dao.ejecutarConsultaSql("select idArticulo from Articulos" +
                                    " where idLinea = '" + cursor.getString(3) + "'");

                            if (cursorArticulosLinea.moveToFirst()) {
                                do {
                                    Ofer_Det ofer_det = new Ofer_Det();
                                    ofer_det = ofer_detMapeador.cursorToEntity(cursor);
                                    ofer_det.Articulo = cursorArticulosLinea.getString(0);
                                    ofer_dets.add(ofer_det);
                                } while (cursorArticulosLinea.moveToNext());
                            }
                        }
                    }
                }
            } while (cursor.moveToNext());
        }
       return ofer_dets;
    }

       public Cursor consultarDetalleOferta(String codigo, String tipo){
        Cursor cursor = dao.ejecutarConsultaSql("select rowid," +
                "codigo as Codigo," +
                "tipo as Tipo," +
                "linea as Linea," +
                "rubro as Rubro," +
                "articulo as Articulo," +
                "descuento1 as Descuento1," +
                "descuento2 as Descuento2," +
                "descuento3 as Descuento3," +
                "descuento4 as Descuento4," +
                "cpra_oblig as Cpra_Oblig," +
                "descrip as Descrip," +
                "d_cant as D_Cant," +
                "h_cant as H_Cant," +
                "multiplo as Multiplo " +
                " from ofer_det" +
                " where codigo='"+codigo+"' and " +
                " trim(tipo) = '"+tipo+"'");

        return cursor;
    }
}
