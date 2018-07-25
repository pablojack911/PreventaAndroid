package inteldev.android.presentation.Mayorista.PedidoManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import inteldev.android.R;
import inteldev.android.modelo.Articulo;
import inteldev.android.modelo.OferEsp;
import inteldev.android.negocios.ControladorArticulo;
import inteldev.android.negocios.ControladorOferta;
import inteldev.android.negocios.ControladorPedido;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.SharedPreferencesManager;
import inteldev.android.presentation.AdaptadorListaGenerico;
import inteldev.android.presentation.Detalle_Oferta;
import inteldev.android.presentation.DialogoAlertaSiNo;
import inteldev.android.presentation.FabricaMensaje;
import inteldev.android.presentation.vistaModelo.DetallePedido;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static inteldev.android.CONSTANTES.CLAVE_UNICA_CABOPER;
import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;

/**
 * Created by Pocho on 31/03/2017.
 */

public class FragmentDetallePedido extends Fragment
{
    String idCliente;
    String claveUnicaCabOper;
    private ControladorPedido controladorPedido;
    private ControladorArticulo controladorArticulo;
    private ControladorOferta controladorOferta;
    private View rootView;

    private void lvPedidos()
    {
        idCliente = SharedPreferencesManager.getString(getContext(), ID_CLIENTE_SELECCIONADO);
        claveUnicaCabOper = SharedPreferencesManager.getString(getContext(), CLAVE_UNICA_CABOPER);
        ArrayList<DetallePedido> listadetallePedido = controladorPedido.consultarPedidos(idCliente, claveUnicaCabOper);
        final ListView lvPedidos = rootView.findViewById(R.id.lvPedidos);
        lvPedidos.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lvPedidos.setAdapter(new AdaptadorListaGenerico(getContext(), R.layout.activity_lista_pedido_mayorista, listadetallePedido)
        {
            @Override
            public void onEntrada(Object entrada, View view)
            {
                TextView tvCodigoArticulo = view.findViewById(R.id.tvCodigoArticulo);
                TextView tvArticulo = view.findViewById(R.id.tvArticulo);
                TextView tvCantidadBultos = view.findViewById(R.id.tvCantidadBultos);
                TextView tvCantidadFraccion = view.findViewById(R.id.tvCantidadFraccion);
                TextView tvDescuento = view.findViewById(R.id.tvDescuento);
                TextView tvFinal = view.findViewById(R.id.tvFinal);
                TextView tvEtiquetaDescuento = view.findViewById(R.id.tvEtiquetaDescuento);
                TextView tvEtiquetaFraccion = view.findViewById(R.id.tvEtiquetaFraccion);

                Float precio;
                int cantidad;
                int bultos;
                int fraccion;
                if (((DetallePedido) entrada).getOferta() == null)
                {
                    String codigoArticulo = ((DetallePedido) entrada).getIdArticulo();
                    String claveUnica = ((DetallePedido) entrada).getIdFila();

                    if (codigoArticulo.substring(0, 3).equals("CON"))
                    {
                        codigoArticulo = controladorPedido.obtenerArticuloPedido(claveUnica);
                        tvCodigoArticulo.setText(codigoArticulo);
                    }
                    else
                    {
                        tvCodigoArticulo.setText(codigoArticulo);
                    }

                    Articulo articulo = controladorArticulo.buscarArticuloPorCodigo(codigoArticulo);
                    tvArticulo.setText(articulo.nombre); //CATCH EXCEPCION POR NULL O VUELA!!

                    bultos = ((DetallePedido) entrada).getEntero();
                    fraccion = ((DetallePedido) entrada).getFraccion();
                    cantidad = bultos * articulo.unidadVenta + fraccion;
                    precio = ((DetallePedido) entrada).getPrecio();

                    tvCantidadBultos.setText(String.valueOf(bultos));

                    if (fraccion > 0)
                    {
                        tvCantidadFraccion.setVisibility(VISIBLE);
                        tvEtiquetaFraccion.setVisibility(VISIBLE);
                        tvCantidadFraccion.setText(String.valueOf(fraccion));
                    }
                    else
                    {
                        tvEtiquetaFraccion.setVisibility(GONE);
                        tvCantidadFraccion.setVisibility(GONE);
                    }
                }
                else
                {
                    String codigoOferta = ((DetallePedido) entrada).getOferta();
                    tvCodigoArticulo.setText(String.format("OFERTA %s", codigoOferta));

                    OferEsp oferEsp = controladorOferta.obtenerOfertaPorCodigo(codigoOferta);
                    tvArticulo.setText(oferEsp.Nombre);

                    tvEtiquetaDescuento.setVisibility(GONE);
                    tvDescuento.setVisibility(GONE);
                    tvEtiquetaFraccion.setVisibility(GONE);
                    tvCantidadFraccion.setVisibility(GONE);

                    precio = ((DetallePedido) entrada).getPrecio() / ((DetallePedido) entrada).getEntero();
                    cantidad = ((DetallePedido) entrada).getEntero();

                    tvCantidadBultos.setText(String.valueOf(cantidad));
                }

                float total = 0;
                float descuento = ((DetallePedido) entrada).getDescuento();
                if (descuento > 0)
                {
                    tvEtiquetaDescuento.setVisibility(VISIBLE);
                    tvDescuento.setVisibility(VISIBLE);
                    tvDescuento.setText(String.format("%.2f", descuento));
                }
                else
                {
                    tvEtiquetaDescuento.setVisibility(GONE);
                    tvDescuento.setVisibility(GONE);
                }
                if (descuento < 100)
                {
                    total = (precio - ((precio * descuento) / 100)) * cantidad;
                }
                tvFinal.setText(String.format("%.2f", total));
            }
        });


        lvPedidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                final DetallePedido elegido = (DetallePedido) adapterView.getItemAtPosition(i);

                FabricaMensaje.dialogoAlertaSiNo(getContext(), "", "¿Seguro que borra el pedido?", new DialogoAlertaSiNo()
                {
                    @Override
                    public void Positivo()
                    {

                        if (elegido.getOferta() == null)
                        {
                            controladorPedido.borrarDetOper(elegido.getIdFila());
                        }
                        else
                        {
                            controladorPedido.borrarOferOper(elegido.getIdFila());
                        }
                        ((ClienteCollection) Objects.requireNonNull(getActivity())).updateTotalPedido();
                        lvPedidos();
                    }

                    @Override
                    public void Negativo()
                    {

                    }
                }).show();

                return false;
            }
        });

        lvPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                final DetallePedido elegido = (DetallePedido) adapterView.getItemAtPosition(i);
                if (elegido.getOferta() != null)
                {
                    Intent intentDetalle_Oferta = new Intent(getContext(), Detalle_Oferta.class);
                    intentDetalle_Oferta.putExtra("claveUnica", elegido.getIdFila());
                    startActivity(intentDetalle_Oferta);
                }
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            lvPedidos();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_detalle_pedido, container, false);
        controladorPedido = FabricaNegocios.obtenerControladorPedido(getContext());
        controladorArticulo = FabricaNegocios.obtenerControladorArticulo(getContext());
        controladorOferta = FabricaNegocios.obtenerControladorOferta(getContext());
        lvPedidos();
        return rootView;
    }

    //    @Override
    //    public void onViewStateRestored(@Nullable Bundle savedInstanceState)
    //    {
    //        if (savedInstanceState != null)
    //        {
    //            idCliente = savedInstanceState.getString(ID_CLIENTE_SELECCIONADO);
    //        }
    //        super.onViewStateRestored(savedInstanceState);
    //    }
    //
    //    @Override
    //    public void onSaveInstanceState(@NonNull Bundle outState)
    //    {
    //        outState.putString(ID_CLIENTE_SELECCIONADO, idCliente);
    //        super.onSaveInstanceState(outState);
    //    }
}
