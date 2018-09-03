package inteldev.android.presentation.Mayorista.PedidoManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import inteldev.android.R;
import inteldev.android.modelo.EstadoGPS;
import inteldev.android.modelo.MotivoNoCompra;
import inteldev.android.modelo.PosicionesGPS;
import inteldev.android.negocios.ControladorPosicionesGPS;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.Fecha;
import inteldev.android.negocios.SharedPreferencesManager;

import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;

/**
 * Created by Pocho on 31/03/2017.
 */

public class FragmentMotivoNoCompra extends Fragment
{
    private View rootView;
    private ControladorPosicionesGPS controladorPosicionesGPS;
    private RadioButton rbNoTieneDinero;
    private RadioButton rbTieneStock;
    private RadioButton rbNoEstabaElResponsable;
    private RadioButton rbCerrado;
    private RadioGroup rgMotivoNoCompra;
    private String idClienteSeleccionado;
    private String loginUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_motivo_no_compra, container, false);

        loginUsuario = SharedPreferencesManager.getLoginUsuario(getContext());
        idClienteSeleccionado = SharedPreferencesManager.getString(getContext(), ID_CLIENTE_SELECCIONADO);

        controladorPosicionesGPS = FabricaNegocios.obtenerControladorPosicionesGPS(getContext());
        configuraMotivoNoCompra();
        cargarMotivoNoCompra();
        return rootView;
    }

    private void configuraMotivoNoCompra()
    {
        rbCerrado = rootView.findViewById(R.id.rbCerrado);
        rbNoEstabaElResponsable = rootView.findViewById(R.id.rbNoEstabaElResponsable);
        rbTieneStock = rootView.findViewById(R.id.rbTieneStock);
        rbNoTieneDinero = rootView.findViewById(R.id.rbNoTieneDinero);
        rgMotivoNoCompra = rootView.findViewById(R.id.rgMotivosNoCompra);
        Button btnSeleccionMotivoNoCompra = rootView.findViewById(R.id.btnOkNoCompra);
        btnSeleccionMotivoNoCompra.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // Returns an integer which represents the selected radio button's ID
                int selected = rgMotivoNoCompra.getCheckedRadioButtonId();
                int motivo;
                if (selected > -1)
                {
                    switch (selected)
                    {
                        case R.id.rbCerrado:
                            motivo = MotivoNoCompra.CERRADO;
                            break;
                        case R.id.rbNoEstabaElResponsable:
                            motivo = MotivoNoCompra.NO_ESTA_EL_RESPONSABLE;
                            break;
                        case R.id.rbNoTieneDinero:
                            motivo = MotivoNoCompra.NO_TIENE_DINERO;
                            break;
                        case R.id.rbTieneStock:
                            motivo = MotivoNoCompra.TIENE_STOCK;
                            break;
                        default:
                            motivo = MotivoNoCompra.COMPRADOR;
                            break;
                    }
                    PosicionesGPS posicionesGPS = new PosicionesGPS();
                    posicionesGPS.usuario = loginUsuario;
                    posicionesGPS.estado = EstadoGPS.OK;
                    posicionesGPS.motivoNoCompra = motivo;
                    posicionesGPS.idCliente = idClienteSeleccionado;
                    controladorPosicionesGPS.enviarPosicion(getContext(), posicionesGPS);
                    //                    motivo = controladorPosicionesGPS.obtenerMotivoNoCompra(idClienteSeleccionado, Fecha.obtenerFechaActual().toString());

                    //                    if (motivo == MotivoNoCompra.NO_HAY_DATOS_REGISTRADOS)
                    //                    {
                    //                        controladorPosicionesGPS.actualizar(idClienteSeleccionado, Fecha.obtenerFechaActual().toString(), posicionesGPS);
                    //                    }
                    //                    else
                    //                    {
                    //                        controladorPosicionesGPS.enviarPosicion(getContext(), posicionesGPS);
                    //                    }
                    getActivity().finish();
                }
            }
        });
    }

    private void cargarMotivoNoCompra()
    {
        int motivo = controladorPosicionesGPS.obtenerMotivoNoCompra(idClienteSeleccionado, Fecha.obtenerFechaActual().toString());
        switch (motivo)
        {
            case 1:
                rgMotivoNoCompra.check(R.id.rbCerrado);
                break;
            case 2:
                rgMotivoNoCompra.check(R.id.rbNoEstabaElResponsable);
                break;
            case 3:
                rgMotivoNoCompra.check(R.id.rbTieneStock);
                break;
            case 4:
                rgMotivoNoCompra.check(R.id.rbNoTieneDinero);
                break;
            default:
                break;
        }
    }
}
