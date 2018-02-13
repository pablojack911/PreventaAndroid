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
import inteldev.android.presentation.vistaModelo.ArticuloSinCargo;

import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;

/**
 * Created by Pocho on 03/04/2017.
 */

public class FragmentConveniosSinCargo extends Fragment
{
    ControladorArticulo controladorArticulo;
    String idClienteSeleccionado;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_convs_sin_cargo, container, false);
//        controladorArticulo = FabricaNegocios.obtenerControladorArticulo(container.getContext());
        controladorArticulo = FabricaNegocios.obtenerControladorArticulo(getActivity());

        Bundle args = getArguments();
        idClienteSeleccionado = args.getString(ID_CLIENTE_SELECCIONADO);

        ArrayList<ArticuloSinCargo> mArticuloSinCargo = controladorArticulo.obtenerSinCargos(idClienteSeleccionado);
        ListView lvArticulosSinCargo = (ListView) rootView.findViewById(R.id.lvArticulosSinCargo);
//        lvArticulosDescuento.setAdapter(new AdaptadorListaGenerico(container.getContext(), R.layout.simple_list_articulo_dto, mArticuloDescuentos)
        lvArticulosSinCargo.setAdapter(new AdaptadorListaGenerico(getActivity(), R.layout.simple_list_conv_sc, mArticuloSinCargo)
        {
            @Override
            public void onEntrada(Object entrada, View view)
            {
                TextView tvCodigoArticulo = (TextView) view.findViewById(R.id.tvCodigoArticulo);
                TextView tvArticulo = (TextView) view.findViewById(R.id.tvArticulo);
                TextView tvDescuento = (TextView) view.findViewById(R.id.tvCantSinCargo);
                TextView tvVencimiento = (TextView) view.findViewById(R.id.tvVencimiento);

                ArticuloSinCargo mArticuloSinCargo = (ArticuloSinCargo) entrada;

                tvCodigoArticulo.setText(mArticuloSinCargo.getIdArticulo());
                tvArticulo.setText(mArticuloSinCargo.getNombre());
                tvDescuento.setText(String.valueOf(mArticuloSinCargo.getSinCargo()));
                tvVencimiento.setText(Fecha.convertir(mArticuloSinCargo.getVencimiento()));
            }
        });
        return rootView;
    }
}
