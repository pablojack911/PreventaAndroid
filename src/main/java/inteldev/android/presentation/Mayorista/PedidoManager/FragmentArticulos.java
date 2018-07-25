package inteldev.android.presentation.Mayorista.PedidoManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;

import java.util.Objects;

import inteldev.android.CONSTANTES;
import inteldev.android.R;
import inteldev.android.modelo.Articulo;
import inteldev.android.modelo.DetBonif;
import inteldev.android.modelo.DetOper;
import inteldev.android.modelo.OferOper;
import inteldev.android.negocios.ControladorArticulo;
import inteldev.android.negocios.ControladorConvenio;
import inteldev.android.negocios.ControladorPedido;
import inteldev.android.negocios.ControladorPrecio;
import inteldev.android.negocios.ControladorStock;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.Fecha;
import inteldev.android.negocios.SharedPreferencesManager;
import inteldev.android.presentation.Articulos.BuscarArticulo;
import inteldev.android.presentation.DialogoAlertaNeutral;
import inteldev.android.presentation.DialogoAlertaSiNo;
import inteldev.android.presentation.FabricaMensaje;
import inteldev.android.presentation.Mayorista.ConvenioMayorista;
import inteldev.android.presentation.Mayorista.Folder.Folder;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static inteldev.android.CONSTANTES.ARTICULO_SELECCIONADO;
import static inteldev.android.CONSTANTES.CLAVE_UNICA_CABOPER;
import static inteldev.android.CONSTANTES.CONFIG_CONVENIO_DESCUENTO;
import static inteldev.android.CONSTANTES.CONFIG_CONVENIO_DETALLE_TIPO;
import static inteldev.android.CONSTANTES.CONFIG_CONVENIO_PRECIO;
import static inteldev.android.CONSTANTES.CONFIG_CONVENIO_TIPO;
import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;
import static inteldev.android.CONSTANTES.INTENT_BUSCAR_ARTICULO;
import static inteldev.android.CONSTANTES.INTENT_CONVENIO;
import static inteldev.android.CONSTANTES.INTENT_FOLDER;
import static inteldev.android.CONSTANTES.INTENT_OFERTA;
import static inteldev.android.presentation.Utiles.cambiaVisibilidad;

/**
 * Created by Pocho on 31/03/2017.
 */

public class FragmentArticulos extends Fragment
{
    //    Updateable updateable;
    private View rootView;
    private EditText edtArticulo;
    private EditText edtDescuento;
    private EditText edtFinal;
    private EditText edtCodigoArticulo;
    private EditText edtArticuloSeleccionado;
    private EditText edtStock;
    private EditText edtUnitarioLista;
    private EditText edtUnitarioDtoFolder;
    private TextView tvFacciones;
    private com.travijuu.numberpicker.library.NumberPicker edtBultos;
    private com.travijuu.numberpicker.library.NumberPicker edtFracciones;
    private Articulo articuloSeleccionado;
    private ControladorArticulo controladorArticulo;
    private ControladorPedido controladorPedido;
    private ControladorPrecio controladorPrecio;
    private ControladorStock controladorStock;
    private int cant_sc;
    private float precioUnitarioUnidad;
    private float descuentoConvenio;
    private float precioUnitarioBulto;
    private float precioFolderDto;
    private String idClienteSeleccionado;
    private String tipoConvenio;
    private String detalleTipoConvenio;
    private String claveUnicaCabOper;
    private String loginUsuario;
    //
    //    public void setUpdateable(Updateable updateable)
    //    {
    //        this.updateable = updateable;
    //    }

    private void configuraBtnFolder()
    {
        rootView.findViewById(R.id.btnFolder).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), Folder.class);
                startActivityForResult(intent, INTENT_FOLDER);
            }
        });
    }

    private void configuraEdtArticuloSeleccionado()
    {
        edtArticuloSeleccionado = rootView.findViewById(R.id.edtArticuloSeleccionado);
        edtArticuloSeleccionado.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getContext(), BuscarArticulo.class);
                intent.putExtra(CONSTANTES.TITULO, getResources().getString(R.string.busca_artic));
                startActivityForResult(intent, INTENT_BUSCAR_ARTICULO);
            }
        });
    }

    private void configuraEdtArticulo()
    {
        edtArticulo = rootView.findViewById(R.id.edtArticulo);
        edtArticulo.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                {
                    Log.d("edtArticulo", String.valueOf(i));

                    switch (i)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            buscarArticulo();
                            break;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }

    //    @Override
    //    public void onResume()
    //    {
    //        limpiarControles();
    //        calcularTotal();
    //        super.onResume();
    //    }

    private void configuraBultosUnidades()
    {
        edtBultos = rootView.findViewById(R.id.etCantidadBultos);
        tvFacciones = rootView.findViewById(R.id.tvFracciones);
        edtFracciones = rootView.findViewById(R.id.etCantidadUnidades);
        edtBultos.setValueChangedListener(new ValueChangedListener()
        {
            @Override
            public void valueChanged(int value, ActionEnum action)
            {
                if (articuloSeleccionado != null)
                {
                    if (cant_sc != 0)
                    {
                        if (edtBultos.getValue() > cant_sc)
                        {
                            Toast.makeText(getContext(), "Esta pidiendo mas sin cargos que los disponibles: " + String.valueOf(cant_sc), Toast.LENGTH_LONG).show();
                            edtBultos.setValue(cant_sc);
                        }
                    }
                    else
                    {
                        edtBultos.setValue(value);
                    }
                    calcularPrecioFinal();
                }
            }
        });
        edtFracciones.setValueChangedListener(new ValueChangedListener()
        {
            @Override
            public void valueChanged(int value, ActionEnum action)
            {
                if (articuloSeleccionado != null)
                {
                    if (value == articuloSeleccionado.minimoVenta)
                    {
                        switch (action)
                        {
                            case DECREMENT:
                                if (edtBultos.getValue() == 0)
                                {
                                    FabricaMensaje.dialogoOk(getContext(), "Minimo venta alcanzado", "No puede cargar menos que " + articuloSeleccionado.minimoVenta + ".", new DialogoAlertaNeutral()
                                    {
                                        @Override
                                        public void Neutral()
                                        {

                                        }
                                    });
                                }
                                else
                                {
                                    edtFracciones.setValue(value);
                                }
                                break;
                            default:
                                edtFracciones.setValue(value);
                                break;
                        }
                    }
                    else if (value == articuloSeleccionado.unidadVenta)
                    {
                        switch (action)
                        {
                            case DECREMENT:
                                edtFracciones.setValue(value);
                                break;
                            default:
                                edtBultos.increment();
                                edtFracciones.setValue(0);
                                break;
                        }
                    }
                    else if (value > articuloSeleccionado.unidadVenta)
                    {
                        switch (action)
                        {
                            case MANUAL:
                                int cantidadBultos = value / articuloSeleccionado.unidadVenta;
                                int cantidadFraccion = value % articuloSeleccionado.unidadVenta;
                                edtBultos.setValue(cantidadBultos);
                                edtFracciones.setValue(cantidadFraccion);
                                break;
                            default:
                                break;
                        }
                    }
                    else
                    {
                        edtFracciones.setValue(value);
                    }
                    calcularPrecioFinal();
                }
            }
        });
    }

    private void configuraDescuento()
    {
        edtDescuento = rootView.findViewById(R.id.edtDescuento);
        edtDescuento.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (articuloSeleccionado != null)
                {
                    Intent intentConvenio = new Intent(getContext(), ConvenioMayorista.class);
                    intentConvenio.putExtra(CONFIG_CONVENIO_PRECIO, precioUnitarioUnidad);
                    intentConvenio.putExtra(CONFIG_CONVENIO_DESCUENTO, descuentoConvenio);
                    intentConvenio.putExtra(CONFIG_CONVENIO_TIPO, tipoConvenio);
                    intentConvenio.putExtra(CONFIG_CONVENIO_DETALLE_TIPO, detalleTipoConvenio);
                    startActivityForResult(intentConvenio, INTENT_CONVENIO);
                }
            }
        });
    }

    private void configuraBtnOk()
    {
        rootView.findViewById(R.id.btnOk).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (articuloSeleccionado != null)
                {
                    if (edtBultos.getValue() == 0 && edtFracciones.getValue() == 0)
                    {
                        FabricaMensaje.dialogoOk(getContext(), "Faltan Datos".toUpperCase(), "No indicó la cantidad.", new DialogoAlertaNeutral()
                        {
                            @Override
                            public void Neutral()
                            {

                            }
                        }).show();
                    }
                    else
                    {
                        if (!controladorStock.hayStock(articuloSeleccionado.idArticulo))
                        {
                            FabricaMensaje.dialogoOk(getContext(), "alerta".toUpperCase(), "No hay stock.", new DialogoAlertaNeutral()
                            {
                                @Override
                                public void Neutral()
                                {

                                }
                            }).show();

                        }
                        else
                        {
                            //GRABAMOS EL PEDIDO
                            String claveUnica;
                            claveUnica = claveUnicaCabOper;

                            DetOper detOper = new DetOper();
                            detOper.idFila = java.util.UUID.randomUUID().toString();
                            detOper.claveUnica = claveUnica;
                            if (precioFolderDto > 0)
                            {
                                if (descuentoConvenio == 0)//es Folder
                                {
                                    detOper.idArticulo = articuloSeleccionado.idArticulo;
                                    detOper.precio = precioFolderDto;
                                }
                                else //es descuento
                                {
                                    detOper.idArticulo = "CONV " + idClienteSeleccionado;
                                    detOper.precio = precioUnitarioUnidad;
                                }
                            }
                            else //sin descuento
                            {
                                detOper.idArticulo = articuloSeleccionado.idArticulo;
                                detOper.precio = precioUnitarioUnidad;
                            }
                            detOper.unidadVenta = articuloSeleccionado.unidadVenta;
                            detOper.descuento = descuentoConvenio;
                            detOper.entero = edtBultos.getValue();
                            detOper.fraccion = edtFracciones.getValue();

                            if (cant_sc != 0)
                            {
                                detOper.sincargo = 1;
                            }

                            controladorPedido.grabarDetOper(detOper);

                            if (descuentoConvenio != 0)
                            {
                                OferOper oferOper = new OferOper();
                                oferOper.claveUnica = detOper.idFila;
                                oferOper.idArticulo = articuloSeleccionado.idArticulo;
                                oferOper.precio = detOper.precio;
                                oferOper.cantidad = detOper.entero;
                                oferOper.descuento = detOper.descuento;
                                oferOper.enviado = 0;
                                oferOper.idFila = java.util.UUID.randomUUID().toString();

                                if (detalleTipoConvenio != null && detalleTipoConvenio.isEmpty())
                                {
                                    oferOper.tipo = tipoConvenio;
                                }
                                else
                                {
                                    oferOper.tipo = detalleTipoConvenio;
                                }

                                controladorPedido.grabarOferOper(oferOper);
                            }


                            limpiarControles();
                            ((ClienteCollection) Objects.requireNonNull(getActivity())).updateTotalPedido();
                            //                            updateable.updateTotalPedido();
                            //                            calcularTotal();
                        }
                    }
                }
                else
                {
                    FabricaMensaje.dialogoOk(getContext(), "FALTAN DATOS", "No especificó un artículo", new DialogoAlertaNeutral()
                    {
                        @Override
                        public void Neutral()
                        {
                        }
                    });
                }
            }
        });
    }

    private void configuraBtnBuscarCodigoArticulo()
    {
        Button btnBuscarCodigoArticulo = rootView.findViewById(R.id.btnBuscarCodigoArticulo);
        btnBuscarCodigoArticulo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (edtArticulo.getText().toString().isEmpty())
                {
                    Intent intent = new Intent(getContext(), BuscarArticulo.class);
                    startActivityForResult(intent, INTENT_BUSCAR_ARTICULO);
                }
                buscarArticulo();
            }
        });

    }

    private void buscarArticulo()
    {
        String articulo = edtArticulo.getText().toString();
        articuloSeleccionado = controladorArticulo.buscarArticulo(articulo);
        if (articuloSeleccionado != null)
        {
            seleccionArticulo();
        }
        edtArticulo.setText("");
    }

    private void seleccionArticulo()
    {
        //        edtBultos.setValue(0);
        //        edtFracciones.setValue(0);
        //        edtDescuento.setText("");
        //        edtFinal.setText("");
        descuentoConvenio = 0;
        tipoConvenio = "";
        detalleTipoConvenio = "";
        edtCodigoArticulo.setText(articuloSeleccionado.idArticulo);
        edtArticuloSeleccionado.setText(articuloSeleccionado.nombre);

        cant_sc = 0;

        precioUnitarioBulto = controladorPrecio.obtener(getContext(), idClienteSeleccionado, articuloSeleccionado.idArticulo);
        precioUnitarioUnidad = precioUnitarioBulto / articuloSeleccionado.unidadVenta;
        edtUnitarioLista.setText(String.format("%.2f", precioUnitarioUnidad));
        float precioFolderBulto = controladorPrecio.obtenerFolder(getContext(), articuloSeleccionado.idArticulo, idClienteSeleccionado);
        precioFolderDto = precioFolderBulto / articuloSeleccionado.unidadVenta;
        if (precioFolderDto > 0)
        {
            edtUnitarioDtoFolder.setText(String.format("%.2f", precioFolderDto));
            edtDescuento.setText("");
            //            cambiaVisibilidad(rootView.findViewById(R.id.il_dto), GONE);
            //            cambiaVisibilidad(edtDescuento, GONE);
        }
        else
        {
            //            cambiaVisibilidad(rootView.findViewById(R.id.il_dto), VISIBLE);
            //            cambiaVisibilidad(edtDescuento, VISIBLE);
            edtUnitarioDtoFolder.setText("");
            edtDescuento.setText("");
        }

        ControladorConvenio controladorConvenio = FabricaNegocios.obtenerControladorConvenio(getContext(), idClienteSeleccionado);
        final DetBonif detBonif = controladorConvenio.obtenerPorcentaje(articuloSeleccionado.idArticulo, false);
        if (detBonif.porcentaje != 0)
        {
            edtDescuento.setText(String.format("%.2f", detBonif.porcentaje));
        }
        else
        {
            edtDescuento.setText("");
        }

        final DetBonif detBonifSinCargos = controladorConvenio.obtenerPorcentaje(articuloSeleccionado.idArticulo, true);

        java.sql.Date sqlDate = Fecha.sumarFechasDias(Fecha.obtenerFechaActual(), 0);
        int sinCargosUtilizados = controladorConvenio.obtenerSinCargosUtilizados(sqlDate, articuloSeleccionado.idArticulo);
        final int sinCargosDisponibles = detBonifSinCargos.cant_sc - sinCargosUtilizados;

        if (sinCargosDisponibles > 0)
        {
            FabricaMensaje.dialogoAlertaSiNo(getContext(), "Tiene " + sinCargosDisponibles + " disponibles", "¿Pide sin cargos?", new DialogoAlertaSiNo()
            {
                @Override
                public void Positivo()
                {
                    cant_sc = sinCargosDisponibles;
                    edtDescuento.setText((String.valueOf(100)));
                }

                @Override
                public void Negativo()
                {
                }
            }).show();
        }


        if (articuloSeleccionado.minimoVenta == articuloSeleccionado.unidadVenta)
        {
            cambiaVisibilidad(tvFacciones, GONE);
            cambiaVisibilidad(edtFracciones, GONE);
            edtFracciones.setValue(0);
        }
        else
        {
            cambiaVisibilidad(tvFacciones, VISIBLE);
            cambiaVisibilidad(edtFracciones, VISIBLE);
        }

        float stock = controladorStock.obtenerStock(articuloSeleccionado.idArticulo);
        edtStock.setText(String.format("%.2f", stock));

        if (!controladorStock.hayStock(articuloSeleccionado.idArticulo))
        {
            FabricaMensaje.dialogoOk(getContext(), "alerta".toUpperCase(), "No hay stock ni orden de compra planificada.", new DialogoAlertaNeutral()
            {
                @Override
                public void Neutral()
                {

                }
            }).show();
        }
        else
        {
            //TODO: en el futuro, preguntar si desea modificar la cantidad del pedido, si acepta -> cargar el articulo con el convenio (si tiene) y la cantidad con la que se cargó. actualizar el item cuando se guarde
            if (controladorPedido.existeEnPrecarga(claveUnicaCabOper, articuloSeleccionado.idArticulo))
            {
                FabricaMensaje.dialogoOk(getContext(), "alerta", "El articulo seleccionado ya está en la precarga.", new DialogoAlertaNeutral()
                {
                    @Override
                    public void Neutral()
                    {

                    }
                }).show();
            }
        }
    }

    private void calcularPrecioFinal()
    {
        if (edtBultos.getValue() > 0 || edtFracciones.getValue() > 0)
        {
            if (precioUnitarioBulto > 0)
            {
                int bultos = edtBultos.getValue() * articuloSeleccionado.unidadVenta;
                int unidades = edtFracciones.getValue();
                int cantidad = bultos + unidades;
                float precioFinal;
                if (descuentoConvenio > 0 || precioFolderDto > 0)
                {
                    precioFinal = precioFolderDto * cantidad;
                }
                else
                {
                    if (descuentoConvenio == 100)
                    {
                        precioFinal = 0;
                    }
                    else
                    {
                        precioFinal = (precioUnitarioUnidad - descuentoConvenio) * cantidad;
                    }
                }

                edtFinal.setText(String.format("%.2f", precioFinal));
            }
        }
        else
        {
            edtFinal.setText("");
        }
    }

    private void limpiarControles()
    {
        edtBultos.setValue(0);
        edtFracciones.setValue(0);
        edtDescuento.setText("");
        edtStock.setText("");
        edtFinal.setText("");
        //        edtTotalPedido.setText("");
        edtCodigoArticulo.setText("");
        edtArticuloSeleccionado.setText("");
        edtArticulo.setText("");
        edtUnitarioDtoFolder.setText("");
        edtUnitarioLista.setText("");
        descuentoConvenio = 0;
        precioFolderDto = 0;
        tipoConvenio = "";
        detalleTipoConvenio = "";
        articuloSeleccionado = null;
        //        String.format("%.2f", 0.00)
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case INTENT_CONVENIO:
            {
                if (data != null)
                {
                    tipoConvenio = data.getStringExtra(CONFIG_CONVENIO_TIPO);
                    detalleTipoConvenio = data.getStringExtra(CONFIG_CONVENIO_DETALLE_TIPO);
                    descuentoConvenio = data.getFloatExtra(CONFIG_CONVENIO_DESCUENTO, 0);
                    //                    claveUnicaCabOper = data.getStringExtra(CLAVE_UNICA_CABOPER);
                    if (descuentoConvenio != 0)
                    {
                        edtDescuento.setText(String.format("%.2f", descuentoConvenio));
                        float dto = (precioUnitarioUnidad * descuentoConvenio) / 100;
                        precioFolderDto = precioUnitarioUnidad - dto;
                        edtUnitarioDtoFolder.setText(String.format("%.2f", precioFolderDto));
                    }
                    else
                    {
                        precioFolderDto = 0;
                        edtDescuento.setText("");
                        edtUnitarioDtoFolder.setText("");
                    }
                    calcularPrecioFinal();
                }
                break;
            }
            case INTENT_BUSCAR_ARTICULO:
                if (resultCode == RESULT_OK)
                {
                    if (data != null)
                    {
                        articuloSeleccionado = data.getParcelableExtra(ARTICULO_SELECCIONADO);
                        seleccionArticulo();
                    }
                }
                break;
            case INTENT_FOLDER:
                if (resultCode == RESULT_OK)
                {
                    if (data != null)
                    {
                        articuloSeleccionado = data.getParcelableExtra(ARTICULO_SELECCIONADO);
                        seleccionArticulo();
                    }
                }
                break;
            case INTENT_OFERTA:
                //                calcularTotal();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_articulos, container, false);

        idClienteSeleccionado = SharedPreferencesManager.getString(getContext(), ID_CLIENTE_SELECCIONADO);
        claveUnicaCabOper = SharedPreferencesManager.getString(getContext(), CLAVE_UNICA_CABOPER);
        loginUsuario = SharedPreferencesManager.getLoginUsuario(getContext());

        controladorArticulo = FabricaNegocios.obtenerControladorArticulo(getContext());
        controladorPedido = FabricaNegocios.obtenerControladorPedido(getContext());
        controladorStock = FabricaNegocios.obtenerControladorStock(getContext());
        controladorPrecio = FabricaNegocios.obtenerControladorPrecio(getContext());

        configuraEdtArticulo();
        configuraBultosUnidades();
        configuraDescuento();
        configuraBtnOk();
        configuraBtnBuscarCodigoArticulo();
        configuraEdtArticuloSeleccionado();
        configuraBtnFolder();
        edtStock = rootView.findViewById(R.id.edtStock);
        edtCodigoArticulo = rootView.findViewById(R.id.edtCodigoArticulo);
        edtUnitarioDtoFolder = rootView.findViewById(R.id.edtUnitarioDtoFolder);
        edtUnitarioLista = rootView.findViewById(R.id.edtUnitarioLista);
        edtFinal = rootView.findViewById(R.id.edtFinal);
        limpiarControles();
        //        calcularTotal();
        return rootView;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState)
    {
        limpiarControles();
        //        calcularTotal();
        if (savedInstanceState != null && savedInstanceState.containsKey(ARTICULO_SELECCIONADO))
        {
            articuloSeleccionado = savedInstanceState.getParcelable(ARTICULO_SELECCIONADO);
            if (articuloSeleccionado != null)
            {
                seleccionArticulo();
            }
        }

        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState)
    {
        outState.putParcelable(ARTICULO_SELECCIONADO, articuloSeleccionado);
        super.onSaveInstanceState(outState);
    }
}
