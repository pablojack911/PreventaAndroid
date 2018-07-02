package inteldev.android.presentation.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import inteldev.android.R;
import inteldev.android.modelo.OferEsp;
import inteldev.android.negocios.ControladorOferta;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.presentation.AdaptadorListaGenerico;
import inteldev.android.presentation.Oferta;

import static android.app.Activity.RESULT_OK;
import static inteldev.android.CONSTANTES.CLAVE_UNICA_CABOPER;
import static inteldev.android.CONSTANTES.CODIGO_OFERTA;
import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;
import static inteldev.android.CONSTANTES.INTENT_OFERTA;

/**
 * Created by Pocho on 31/03/2017.
 */

public class FragmentOfertas extends Fragment
{
    private View rootView;
    private ListView lvOfertas;
    private ControladorOferta controladorOferta;
    private String idCliente;
    private String claveUnicaCabOper;
    private ArrayList<OferEsp> listaOferEsp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_ofertas, container, false);
        Bundle args = getArguments();
        assert args != null;
        idCliente = args.getString(ID_CLIENTE_SELECCIONADO);
        claveUnicaCabOper = args.getString(CLAVE_UNICA_CABOPER);
//        ((TextView) rootView.findViewById(R.id.of_oferta)).setText(args.getString(CONSTANTES.CODIGO_OFERTA));
        controladorOferta = FabricaNegocios.obtenerControladorOferta(getContext());
        configuraBuscadorOferta();
        configuraLvOfertas();
        return rootView;
    }

    private void configuraBuscadorOferta()
    {
        EditText edtBuscadorCodigoOferta = rootView.findViewById(R.id.edtBuscadorCodigoOferta);
        edtBuscadorCodigoOferta.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String codigo = s.toString();
                if (codigo.isEmpty())
                {
                    codigo = "0";
                }
                String codigoABuscar = String.format("%08d", Integer.valueOf(codigo));
                boolean existeOferta = controladorOferta.buscarOfertaPorCodigo(codigoABuscar);
                if (existeOferta)
                {
                    String codigoItemLista;
                    // Busco en la array la posicion con el codigo de oferta
                    int i;
                    for (i = 0; i < listaOferEsp.size(); i++)
                    {

                        codigoItemLista = listaOferEsp.get(i).getCodigo();
                        if (codigoItemLista.equals(codigoABuscar))
                        {
                            lvOfertas.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    private void configuraLvOfertas()
    {
        listaOferEsp = controladorOferta.obtenerOfertas(idCliente);
        lvOfertas = rootView.findViewById(R.id.lvOferta);
        lvOfertas.setAdapter(new AdaptadorListaGenerico(getContext(), R.layout.activity_lista_oferta, listaOferEsp)
        {
            @Override
            public void onEntrada(Object entrada, View view)
            {

                TextView tvCodigoOferta = view.findViewById(R.id.tvCodigoOferta);
                TextView tvNombreOferta = view.findViewById(R.id.tvNombreOferta);

                String codigoArticulo = ((OferEsp) entrada).getCodigo();
                tvCodigoOferta.setText(codigoArticulo);

                String nombre = ((OferEsp) entrada).getNombre();
                tvNombreOferta.setText(nombre);
            }
        });

        lvOfertas.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                final OferEsp elegido = (OferEsp) adapterView.getItemAtPosition(i);

                Intent intentOferta = new Intent(getContext(), Oferta.class);
                intentOferta.putExtra(CODIGO_OFERTA, elegido.Codigo);
                intentOferta.putExtra(ID_CLIENTE_SELECCIONADO, idCliente);
                intentOferta.putExtra(CLAVE_UNICA_CABOPER, claveUnicaCabOper);
                startActivityForResult(intentOferta, INTENT_OFERTA);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == INTENT_OFERTA)
        {
            if (resultCode == RESULT_OK)
            {
                ((ClienteCollection) Objects.requireNonNull(getActivity())).updateTotalPedido();
            }
        }
    }

}
