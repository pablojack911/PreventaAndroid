package inteldev.android.presentation.DescuentosConvenios;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import inteldev.android.R;
import inteldev.android.negocios.ControladorArticulo;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.Fecha;
import inteldev.android.presentation.AdaptadorListaGenerico;
import inteldev.android.presentation.vistaModelo.ArticuloDescuento;

import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;

/**
 * Created by Pocho on 03/04/2017.
 */

public class FragmentDescuentos extends Fragment
{
    ControladorArticulo controladorArticulo;
    String idClienteSeleccionado;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_dtos_articulo, container, false);
//        controladorArticulo = FabricaNegocios.obtenerControladorArticulo(container.getContext());
        controladorArticulo = FabricaNegocios.obtenerControladorArticulo(getActivity());

        Bundle args = getArguments();
        idClienteSeleccionado = args.getString(ID_CLIENTE_SELECCIONADO);

        ArrayList<ArticuloDescuento> mArticuloDescuentos = controladorArticulo.obtenerDescuentosMayorista(idClienteSeleccionado);
//        ArrayList<ArticuloDescuento> mArticuloDescuentos = controladorArticulo.obtenerDescuentos(idClienteSeleccionado);
        ListView lvArticulosDescuento = (ListView) rootView.findViewById(R.id.lvArticulosDescuento);
//        lvArticulosDescuento.setAdapter(new AdaptadorListaGenerico(container.getContext(), R.layout.simple_list_articulo_dto, mArticuloDescuentos)
        lvArticulosDescuento.setAdapter(new AdaptadorListaGenerico(getActivity(), R.layout.simple_list_articulo_dto, mArticuloDescuentos)
        {
            @Override
            public void onEntrada(Object entrada, View view)
            {
                TextView tvCodigoArticulo = (TextView) view.findViewById(R.id.tvCodigoArticulo);
                TextView tvArticulo = (TextView) view.findViewById(R.id.tvArticulo);
                TextView tvDescuento = (TextView) view.findViewById(R.id.tvDescuento);
                TextView tvPrecio = (TextView) view.findViewById(R.id.tvPrecio);
                TextView tvVencimiento = (TextView) view.findViewById(R.id.tvVencimiento);

                ArticuloDescuento mArticuloDescuento = (ArticuloDescuento) entrada;

                tvCodigoArticulo.setText(mArticuloDescuento.getIdArticulo());
                tvArticulo.setText(mArticuloDescuento.getNombre());
                tvDescuento.setText(String.valueOf(mArticuloDescuento.getPorcentaje()));
                tvPrecio.setText(String.valueOf(mArticuloDescuento.getPrecio()));
                tvVencimiento.setText(Fecha.convertir(mArticuloDescuento.getVencimiento()));
            }
        });
        return rootView;
    }
}
