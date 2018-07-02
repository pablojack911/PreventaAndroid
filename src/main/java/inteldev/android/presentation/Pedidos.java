package inteldev.android.presentation;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import inteldev.android.CONSTANTES;
import inteldev.android.R;
import inteldev.android.accesoadatos.Dao;
import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.Articulo;
import inteldev.android.modelo.CabOper;
import inteldev.android.modelo.DetBonif;
import inteldev.android.modelo.DetOper;
import inteldev.android.modelo.EstadoGPS;
import inteldev.android.modelo.Linea;
import inteldev.android.modelo.MotivoNoCompra;
import inteldev.android.modelo.OferEsp;
import inteldev.android.modelo.OferOper;
import inteldev.android.modelo.PosicionesGPS;
import inteldev.android.modelo.Rubro;
import inteldev.android.negocios.ControladorArticulo;
import inteldev.android.negocios.ControladorConvenio;
import inteldev.android.negocios.ControladorOferta;
import inteldev.android.negocios.ControladorPedido;
import inteldev.android.negocios.ControladorPosicionesGPS;
import inteldev.android.negocios.ControladorPrecio;
import inteldev.android.negocios.ControladorStock;
import inteldev.android.negocios.ConvertirFotoABase64;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.Fecha;
import inteldev.android.negocios.Mapeador;
import inteldev.android.negocios.SharedPreferencesManager;
import inteldev.android.negocios.SpinnerManager;
import inteldev.android.negocios.WebServiceHelper;
import inteldev.android.presentation.DescuentosConvenios.DescuentosConveniosCollection;
import inteldev.android.presentation.vistaModelo.DetallePedido;
import inteldev.android.servicios.GPSIntentService;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static inteldev.android.CONSTANTES.ARTICULO_SELECCIONADO;
import static inteldev.android.CONSTANTES.CLAVE_UNICA_CABOPER;
import static inteldev.android.CONSTANTES.CODIGO_OFERTA;
import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;
import static inteldev.android.CONSTANTES.INTENT_BUSCAR_ARTICULO;
import static inteldev.android.CONSTANTES.INTENT_CONVENIO;
import static inteldev.android.CONSTANTES.INTENT_OFERTA;
import static inteldev.android.CONSTANTES.INTENT_TAKE_PICTURE;
import static inteldev.android.CONSTANTES.NOMBRE_CLIENTE_KEY;
import static inteldev.android.CONSTANTES.POSICION_GPS_KEY;
import static inteldev.android.CONSTANTES.USUARIO_KEY;
import static inteldev.android.presentation.Utiles.cambiaVisibilidad;

public class Pedidos extends AppCompatActivity
{
    Uri output;
    Spinner spLinea;
    Spinner spRubro;
    Spinner spArticulo;
    EditText edtCodigoArticulo;
    EditText edtArticulo;
    EditText edtUnitario;
    EditText edtDescuento;
    com.travijuu.numberpicker.library.NumberPicker edtBultos;
    com.travijuu.numberpicker.library.NumberPicker edtFracciones;
    EditText edtFinal;
    EditText edtTotalPedido;
    String idLineaSeleccionada = "XXX";
    String idRubroSeleccionado = "XXX";
    //    String idArticuloSeleccionado = null;
    String idClienteSeleccionado = null;
    String claveUnicaCabOper = null;
    String nombre;
    TabHost.TabSpec tabDetalle;
    TabHost.TabSpec tabPedido;
    String tipoConvenio;
    String detalleTipoConvenio;
    float descuentoConvenio;
    int cant_sc;
    TextView edtBuscadorCodigoOferta;
    Button btnBuscarCodigoArticulo;
    ControladorPedido controladorPedido;
    ControladorOferta controladorOferta;
    ControladorPrecio controladorPrecio;
    ControladorArticulo controladorArticulo;
    ControladorStock controladorStock;
    ControladorPosicionesGPS controladorPosicionesGPS;
    ArrayList<OferEsp> listaOferEsp;
    boolean primerSeleccion = false;
    ListView lvOfertas;
    Button btnSeleccionMotivoNoCompra;
    String foto;
    RadioButton rbCerrado;
    RadioButton rbNoEstabaElResponsable;
    RadioButton rbTieneStock;
    RadioButton rbNoTieneDinero;
    RadioGroup rgMotivoNoCompra;
    String loginUsuario;
    IDao dao;
    Articulo articuloSeleccionado;


    private Float precioUnitarioUnidad;
    private Float precioUnitarioBulto;
    private Button btnOk;
    private boolean modoBuscadorArticulo;
    private LinearLayout llFraccuiones;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        loginUsuario = SharedPreferencesManager.getLoginUsuario(Pedidos.this);
//        if (this.getIntent().hasExtra(USUARIO_KEY))
//        {
//            loginUsuario = this.getIntent().getStringExtra(USUARIO_KEY);
//        }
        Intent intent = this.getIntent();
        if (intent.hasExtra(ID_CLIENTE_SELECCIONADO) && intent.hasExtra(NOMBRE_CLIENTE_KEY))
        {
            idClienteSeleccionado = intent.getStringExtra(ID_CLIENTE_SELECCIONADO);
            nombre = intent.getStringExtra(NOMBRE_CLIENTE_KEY);
            setTitle(idClienteSeleccionado + " - " + nombre);
        }

        final TabHost tabs = findViewById(android.R.id.tabhost);
        tabs.setup();

        tabPedido = tabs.newTabSpec("TabPedido");
        tabPedido.setContent(R.id.tabPedido);
        tabPedido.setIndicator("Pedido");
        tabs.addTab(tabPedido);

        tabDetalle = tabs.newTabSpec("TabOferta");
        tabDetalle.setContent(R.id.tabOferta);
        tabDetalle.setIndicator("Oferta");
        tabs.addTab(tabDetalle);

        tabDetalle = tabs.newTabSpec("TabDetalle");
        tabDetalle.setContent(R.id.tabDetalle);
        tabDetalle.setIndicator("Detalle");
        tabs.addTab(tabDetalle);

        tabDetalle = tabs.newTabSpec("TabNoCompra");
        tabDetalle.setContent(R.id.tabNoCompra);
        tabDetalle.setIndicator("No Compra");
        tabs.addTab(tabDetalle);

        edtArticulo = findViewById(R.id.edtArticulo);
//        edtArticulo.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after)
//            {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//                if (s.length() == 7)
//                {
//                    buscarArticulo();
//                }
//                else if (s.length() == 13)
//                {
//                    buscarArticulo();
//                }
//                else if (s.length() == 15)
//                {
//                    buscarArticulo();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s)
//            {
//
//            }
//        });

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

        bindUiBultosUnidades();

        tabs.setCurrentTab(0);

        spLinea = findViewById(R.id.spLinea);
        spRubro = findViewById(R.id.spRubro);
        spArticulo = findViewById(R.id.spArticulo);

        edtCodigoArticulo = findViewById(R.id.edtCodigoArticulo);
        btnBuscarCodigoArticulo = findViewById(R.id.btnBuscarCodigoArticulo);

        edtUnitario = findViewById(R.id.edtUnitario);
        edtDescuento = findViewById(R.id.edtDescuento);
//        edtCantidad = (EditText) findViewById(R.id.edtCantidad);
        edtFinal = findViewById(R.id.edtFinal);

        edtBuscadorCodigoOferta = (EditText) findViewById(R.id.edtBuscadorCodigoOferta);

        dao = FabricaNegocios.obtenerDao(this);

        edtTotalPedido = findViewById(R.id.edtTotalPedido);

        btnOk = findViewById(R.id.btnOk);

        rbCerrado = findViewById(R.id.rbCerrado);
        rbNoEstabaElResponsable = findViewById(R.id.rbNoEstabaElResponsable);
        rbTieneStock = findViewById(R.id.rbTieneStock);
        rbNoTieneDinero = findViewById(R.id.rbNoTieneDinero);
        rgMotivoNoCompra = findViewById(R.id.rgMotivosNoCompra);

        btnSeleccionMotivoNoCompra = findViewById(R.id.btnOkNoCompra);
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
                    enviarPosicion(posicionesGPS);
                    motivo = controladorPosicionesGPS.obtenerMotivoNoCompra(idClienteSeleccionado, Fecha.obtenerFechaActual().toString());

                    if (motivo == MotivoNoCompra.NO_HAY_DATOS_REGISTRADOS)
                    {
                        controladorPosicionesGPS.actualizar(idClienteSeleccionado, Fecha.obtenerFechaActual().toString(), posicionesGPS);
                    }
                    else
                    {
                        enviarPosicion(posicionesGPS);
                    }
                    finish();
                }
            }
        });

        controladorPedido = FabricaNegocios.obtenerControladorPedido(Pedidos.this);
        controladorOferta = FabricaNegocios.obtenerControladorOferta(Pedidos.this);
        controladorPrecio = FabricaNegocios.obtenerControladorPrecio(Pedidos.this);
        controladorArticulo = FabricaNegocios.obtenerControladorArticulo(Pedidos.this);
        controladorStock = FabricaNegocios.obtenerControladorStock(Pedidos.this);
        controladorPosicionesGPS = FabricaNegocios.obtenerControladorPosicionesGPS(Pedidos.this);

        spLinea();

        spDescuento();

        btnOk();

        lvPedidos();

        seleccionOferta();

        edtBuscadorCodigoOferta();

        btnBuscarCodigoArticulo();

        cargarMotivoNoCompra();

        claveUnicaCabOper = controladorPedido.ObtenerClaveUnicaPedido(idClienteSeleccionado, Fecha.obtenerFechaActual());
        if (claveUnicaCabOper != null)
        {
            FabricaMensaje.dialogoAlertaPrecarga(Pedidos.this, "NUEVA PRECARGA", "¿Desea crear una nueva precarga?", new DialogoAlertaSiNo()
            {
                @Override
                public void Positivo()
                {
                    claveUnicaCabOper = null;
                }

                @Override
                public void Negativo()
                {
                }
            }).show();
        }
//        edtArticulo.requestFocus();
    }

    private void bindUiBultosUnidades()
    {
        edtBultos = findViewById(R.id.etCantidadBultos);
        llFraccuiones = findViewById(R.id.llFracciones);
        edtFracciones = findViewById(R.id.etCantidadUnidades);
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
                            Toast.makeText(Pedidos.this, "Esta pidiendo mas sin cargos que los disponibles: " + String.valueOf(cant_sc), Toast.LENGTH_LONG).show();
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
                                    FabricaMensaje.dialogoOk(Pedidos.this, "Minimo venta alcanzado", "No puede cargar menos que " + articuloSeleccionado.minimoVenta + ".", new DialogoAlertaNeutral()
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
//                        edtFracciones.setValue(articuloSeleccionado.minimoVenta);
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

    private void enviarPosicion(PosicionesGPS posicionesGPS)
    {
        Intent mServiceIntent = new Intent(this, GPSIntentService.class);
        mServiceIntent.putExtra(POSICION_GPS_KEY, posicionesGPS);
        this.startService(mServiceIntent);
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

    private void btnBuscarCodigoArticulo()
    {

        btnBuscarCodigoArticulo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (edtArticulo.getText().toString().isEmpty())
                {
                    Intent intent = new Intent(Pedidos.this, BuscarArticulo.class);
                    startActivityForResult(intent, CONSTANTES.INTENT_BUSCAR_ARTICULO);
                }
                buscarArticulo();
            }
        });

    }

    private void edtBuscadorCodigoOferta()
    {

        edtBuscadorCodigoOferta.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
//                String codigo = edtBuscadorCodigoOferta.getText().toString();
//
//                if (codigo.isEmpty())
//                {
//                    codigo = "0";
//                }
//
//                String codigoABuscar = String.format("%08d", Integer.valueOf(codigo));
//
//                boolean existeOferta = controladorOferta.buscarOfertaPorCodigo(codigoABuscar);
//
//                if (existeOferta)
//                {
//
//                    String codigoItemLista;
//                    // Busco en la array la posicion con el codigo de oferta
//                    int i;
//                    for (i = 0; i < listaOferEsp.size(); i++)
//                    {
//
//                        codigoItemLista = listaOferEsp.get(i).getCodigo();
//                        if (codigoItemLista.equals(codigoABuscar))
//                        {
//                            lvOfertas.setSelection(i);
//                            break;
//                        }
//                    }
//                }

            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

    }

    private void seleccionOferta()
    {
        listaOferEsp = controladorOferta.obtenerOfertas(idClienteSeleccionado);
        lvOfertas = findViewById(R.id.lvOferta);

        lvOfertas.setAdapter(new AdaptadorListaGenerico(this, R.layout.activity_lista_oferta, listaOferEsp)
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

                Intent intentOferta = new Intent(Pedidos.this, Oferta.class);
                intentOferta.putExtra(CODIGO_OFERTA, elegido.Codigo);
                intentOferta.putExtra(ID_CLIENTE_SELECCIONADO, idClienteSeleccionado);
                intentOferta.putExtra(CLAVE_UNICA_CABOPER, claveUnicaCabOper);
                intentOferta.putExtra(USUARIO_KEY, loginUsuario);
                startActivityForResult(intentOferta, INTENT_OFERTA);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case INTENT_CONVENIO:
                if (data != null)
                {
                    tipoConvenio = data.getStringExtra("tipo");
                    detalleTipoConvenio = data.getStringExtra("detalletipo");
                    descuentoConvenio = data.getFloatExtra("descuento", 0);
                    claveUnicaCabOper = data.getStringExtra(CLAVE_UNICA_CABOPER);
                    edtDescuento.setText(String.format("%.2f", descuentoConvenio));
                    calcularPrecioFinal();
                }
                break;
            case INTENT_OFERTA:
                lvPedidos();
                break;
            case INTENT_TAKE_PICTURE:
                ImageView iv = findViewById(R.id.ivCliente);
                iv.setImageBitmap(BitmapFactory.decodeFile(foto));

                File file = new File(foto);
                if (file.exists())
                {
                    String base64 = ConvertirFotoABase64.convertir(Pedidos.this, output);
                    WebServiceHelper webServiceHelper = new WebServiceHelper();
                    //Subir foto Cliente
                    webServiceHelper.addService("http://www.inteldevmobile.com.ar", "SubirFotoCliente", "http://mhergo.ddns.net:8888/inteldevwebservice/Service.asmx?WSDL", "http://www.inteldevmobile.com.ar/SubirFotoCliente", "SubirFotoCliente", 2000);
                    webServiceHelper.addMethodParameter("SubirFotoCliente", "usuario", loginUsuario);
                    webServiceHelper.addMethodParameter("SubirFotoCliente", "cliente", idClienteSeleccionado);
                    webServiceHelper.addMethodParameter("SubirFotoCliente", "foto", base64);
                    SoapObject respuesta = webServiceHelper.executeService("SubirFotoCliente");
                    Toast.makeText(Pedidos.this, respuesta.getProperty(0).toString(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Pedidos.this, "No se ha realizado la foto", Toast.LENGTH_SHORT).show();
                }
                break;
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
            default:
                break;
        }
    }


    private void spDescuento()
    {
        Button btnDescuento = findViewById(R.id.btnDescuento);
        btnDescuento.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intentConvenio = new Intent(Pedidos.this, Convenio.class);
                startActivityForResult(intentConvenio, CONSTANTES.INTENT_CONVENIO);
            }
        });

    }

    private void lvPedidos()
    {
        ArrayList<DetallePedido> listadetallePedido = new ArrayList<DetallePedido>();
        final Dao dao = new Dao(Pedidos.this);
        Cursor cursor = consultarPedidos();
        calcularTotal(cursor);
        if (cursor != null && cursor.moveToFirst())
        {
            Mapeador<DetallePedido> detallePedidoMapeador = new Mapeador<DetallePedido>(new DetallePedido());
            listadetallePedido = detallePedidoMapeador.cursorToList(cursor);
        }
        final ListView lvPedidos = findViewById(R.id.lvPedidos);
        lvPedidos.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        lvPedidos.setAdapter(new AdaptadorListaGenerico(this, R.layout.activity_lista_pedido_mayorista, listadetallePedido)
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
                        Cursor cursorOferOper = dao.ejecutarConsultaSql("select idArticulo from oferoper where claveUnica = '" + claveUnica + "'");
                        if (cursorOferOper != null && cursorOferOper.moveToFirst())
                        {
                            codigoArticulo = cursorOferOper.getString(0);
                            tvCodigoArticulo.setText(codigoArticulo);
                        }
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

                FabricaMensaje.dialogoAlertaSiNo(Pedidos.this, "", "¿Seguro que borra el pedido?", new DialogoAlertaSiNo()
                {
                    @Override
                    public void Positivo()
                    {

                        if (elegido.getOferta() == null)
                        {
                            dao.delete("detOper", "idFila=?", new String[]{elegido.getIdFila()});
                        }
                        else
                        {
                            String idFila = elegido.getIdFila();
                            dao.delete("detOper", "idFila=?", new String[]{elegido.idFila});
                            dao.delete("oferOper", "claveUnica=?", new String[]{idFila});
                        }
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
                    Intent intentDetalle_Oferta = new Intent(Pedidos.this, Detalle_Oferta.class);
                    intentDetalle_Oferta.putExtra("claveUnica", elegido.getIdFila());
                    startActivity(intentDetalle_Oferta);
                }
            }
        });

    }

    private Cursor consultarPedidos()
    {
        return dao.ejecutarConsultaSql("select cabOper.idCliente, cabOper.fecha, detOper.idArticulo, detOper.entero, detOper.fraccion, detOper.precio, detOper.descuento, detOper.idFila, detOper.oferta, detOper.claveUnica, detOper.unidadVenta from cabOper inner join detOper on detOper.claveUnica = cabOper.claveUnica where cabOper.idCliente = '" + idClienteSeleccionado + "' and cabOper.fecha = '" + Fecha.obtenerFechaActual().toString() + "'");
    }

    /*
       select cabOper.idCliente
       cabOper.fecha
       detOper.idArticulo
       detOper.entero
       detOper.fraccion
       detOper.precio
       detOper.descuento
       detOper.idFila
       detOper.oferta
       detOper.claveUnica
       detOper.unidadVenta
       from cabOper
       inner join detOper on detOper.claveUnica = cabOper.claveUnica
       where cabOper.idCliente = 'idClienteSeleccionado' and
       cabOper.fecha = Fecha.obtenerFechaActual()
    */
    private void calcularTotal(Cursor cursor)
    {
        float total = 0f;
        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                float precio = cursor.getFloat(5);
                int bultos = cursor.getInt(3);
                int fracciones = cursor.getInt(4);
                int unidadVenta = cursor.getInt(10);
                int cantidad = bultos * unidadVenta + fracciones;
                float descuento = cursor.getFloat(6);
                float importeDescuento = precio * (descuento / 100);
                total = total + ((precio - importeDescuento) * cantidad);
            } while (cursor.moveToNext());
        }

        edtTotalPedido.setText(String.format("%.2f", total));
    }

    private void btnOk()
    {
        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (articuloSeleccionado != null)
                {
                    if (edtBultos.getValue() == 0 && edtFracciones.getValue() == 0)
                    {
                        FabricaMensaje.dialogoOk(Pedidos.this, "Faltan Datos".toUpperCase(), "No indicó la cantidad.", new DialogoAlertaNeutral()
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
                            FabricaMensaje.dialogoOk(Pedidos.this, "alerta".toUpperCase(), "No hay stock.", new DialogoAlertaNeutral()
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
                            CabOper cabOper = new CabOper();
                            if (claveUnicaCabOper == null)
                            {
                                claveUnica = java.util.UUID.randomUUID().toString();
                                claveUnicaCabOper = claveUnica;

                                cabOper.claveUnica = claveUnica;

                                cabOper.idOperacion = 1;
                                cabOper.idCliente = idClienteSeleccionado;

                                java.sql.Date sqlDate = Fecha.sumarFechasDias(Fecha.obtenerFechaActual(), 0);

                                cabOper.fecha = sqlDate;
                                cabOper.fechaEntrega = sqlDate;
                                cabOper.idVendedor = loginUsuario;
                                cabOper.enviado = 0;

                                cabOper.domicilio = "";

                                Mapeador<CabOper> cabOperMapeador = new Mapeador<CabOper>(cabOper);
                                ContentValues contentValues = cabOperMapeador.entityToContentValues();
                                dao.insert("cabOper", contentValues);

                            }
                            else
                            {
                                claveUnica = claveUnicaCabOper;
                            }

                            DetOper detOper = new DetOper();
                            detOper.claveUnica = claveUnica;
                            if (descuentoConvenio == 0)
                            {
                                detOper.idArticulo = articuloSeleccionado.idArticulo;
                            }
                            else
                            {
                                detOper.idArticulo = "CONV " + idClienteSeleccionado;
                            }
                            detOper.unidadVenta = articuloSeleccionado.unidadVenta;
                            detOper.descuento = Float.valueOf(edtDescuento.getText().toString());
                            detOper.entero = edtBultos.getValue();
                            detOper.fraccion = edtFracciones.getValue();
                            detOper.precio = Float.valueOf(edtUnitario.getText().toString());
                            detOper.idFila = java.util.UUID.randomUUID().toString();

                            if (cant_sc != 0)
                            {
                                detOper.sincargo = 1;
                            }

                            Mapeador<DetOper> detOperMapeador = new Mapeador<DetOper>(detOper);
                            ContentValues contentValuesdetOper = detOperMapeador.entityToContentValues();

                            dao.insert("detOper", contentValuesdetOper);

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

                                Mapeador<OferOper> oferOperMapeador = new Mapeador<OferOper>(oferOper);
                                ContentValues contentValuesOferOper = oferOperMapeador.entityToContentValues();
                                dao.insert("oferOper", contentValuesOferOper);
                            }

                            lvPedidos();

                            edtBultos.setValue(0);
                            edtFracciones.setValue(0);
                            edtDescuento.setText(String.format("%.2f", 0.00));
                            descuentoConvenio = 0;
                            tipoConvenio = "";
                            detalleTipoConvenio = "";
                            edtFinal.setText(String.format("%.2f", 0.00));
                            edtArticulo.setText("");
//                            edtArticulo.requestFocus();
                        }
                    }
                }
                else
                {
                    FabricaMensaje.dialogoOk(Pedidos.this, "FALTAN DATOS", "No especificó un artículo", new DialogoAlertaNeutral()
                    {
                        @Override
                        public void Neutral()
                        {
//                            edtArticulo.requestFocus();
                        }
                    });
                }
            }
        });
    }


    private void spLinea()
    {
        Linea linea = new Linea();

        //String consulta = "select cast(idLinea as int) as _id,nombre from lineas order by nombre";

        String consulta = "select rowid as _id, nombre from lineas order by nombre ";

        SpinnerManager<Linea> llenarLinea = new SpinnerManager<Linea>(linea, Pedidos.this, spLinea);
        llenarLinea.llenarDesdeCursor(consulta, "nombre");


        spLinea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (!modoBuscadorArticulo)
                {
                    if (primerSeleccion)
                    {
                        Linea lineaSeleccionada = controladorArticulo.buscarLineaPorRowId(l);
                        idLineaSeleccionada = lineaSeleccionada.idLinea; // String.format("%03d", l);
                        spRubro();
                    }
                    else
                    {
                        primerSeleccion = true;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
    }

    private void spRubro()
    {
        Rubro rubro = new Rubro();

        String consulta = "SELECT distinct rubros.rowid  as _id, rubros.nombre from rubros join articulos as art on art.idrubro = rubros.idrubro where art.idLinea = '" + idLineaSeleccionada + "'";

        SpinnerManager<Rubro> llenarRubro = new SpinnerManager<Rubro>(rubro, Pedidos.this, spRubro);
        llenarRubro.llenarDesdeCursor(consulta, "nombre");
        spRubro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (!modoBuscadorArticulo)
                {
                    Rubro rubroSeleccionado = controladorArticulo.buscarRubroPorRowId(l);
                    idRubroSeleccionado = rubroSeleccionado.idRubro; // String.format("%03d", l);
                    spArticulo();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
    }

    private void spArticulo()
    {
        final Articulo articulo = new Articulo();

        String consulta = "select rowid as _id,nombre from articulos where idlinea = '" + idLineaSeleccionada + "' and idRubro = '" + idRubroSeleccionado + "' order by nombre ";

        SpinnerManager<Articulo> llenarArticulo = new SpinnerManager<Articulo>(articulo, Pedidos.this, spArticulo);
        llenarArticulo.llenarDesdeCursor(consulta, "nombre");
        spArticulo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                edtBultos.setValue(0);
                edtFracciones.setValue(0);
                edtDescuento.setText(String.format("%.2f", 0.00));
                descuentoConvenio = 0;
                tipoConvenio = "";
                detalleTipoConvenio = "";
                edtFinal.setText(String.format("%.2f", 0.00));
                articuloSeleccionado = controladorArticulo.buscarArticuloPorRowId(l);
                edtCodigoArticulo.setText(articuloSeleccionado.idArticulo);

                cant_sc = 0;

                precioUnitarioBulto = controladorPrecio.obtener(Pedidos.this, articuloSeleccionado.idArticulo, idClienteSeleccionado);
                precioUnitarioUnidad = precioUnitarioBulto / articuloSeleccionado.unidadVenta;
                edtUnitario.setText(String.format("%.2f", precioUnitarioUnidad));
                edtDescuento.setText(String.format("%.2f", 0.00));

                ControladorConvenio controladorConvenio = FabricaNegocios.obtenerControladorConvenio(Pedidos.this, idClienteSeleccionado);

                final DetBonif detBonif = controladorConvenio.obtenerPorcentaje(articuloSeleccionado.idArticulo, false);
                edtDescuento.setText(String.format("%.2f", detBonif.porcentaje));

                final DetBonif detBonifSinCargos = controladorConvenio.obtenerPorcentaje(articuloSeleccionado.idArticulo, true);

                java.sql.Date sqlDate = Fecha.sumarFechasDias(Fecha.obtenerFechaActual(), 0);
                int sinCargosUtilizados = controladorConvenio.obtenerSinCargosUtilizados(sqlDate, articuloSeleccionado.idArticulo);
                final int sinCargosDisponibles = detBonifSinCargos.cant_sc - sinCargosUtilizados;

                if (sinCargosDisponibles > 0)
                {
                    FabricaMensaje.dialogoAlertaSiNo(Pedidos.this, "Tiene " + sinCargosDisponibles + " disponibles", "¿Pide sin cargos?", new DialogoAlertaSiNo()
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
                    cambiaVisibilidad(llFraccuiones, GONE);
                    edtFracciones.setValue(0);
                }
                else
                {
                    cambiaVisibilidad(llFraccuiones, VISIBLE);
//                    edtFracciones.setMax(articuloSeleccionado.unidadVenta);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });
        spArticulo.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                modoBuscadorArticulo = false;
                return false;
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
//            edtArticulo.requestFocus();
       }
        edtArticulo.setText("");
    }

    private void seleccionArticulo()
    {
        modoBuscadorArticulo = true;
        long idArticulo = controladorArticulo.buscarRowIdArticuloPorCodigo(articuloSeleccionado.idArticulo);
        long idLinea = controladorArticulo.buscarRowIdLineaPorCodigo(articuloSeleccionado.idLinea);
        long idRubro = controladorArticulo.buscarRowIdRubroPorRowId(articuloSeleccionado.idRubro);

        spLinea.setSelection(getIndex(spLinea, idLinea));
        idLineaSeleccionada = articuloSeleccionado.idLinea;

        spRubro();

        spRubro.setSelection(getIndex(spRubro, idRubro));
        idRubroSeleccionado = articuloSeleccionado.idRubro;

        spArticulo();
        spArticulo.setSelection(getIndex(spArticulo, idArticulo));
    }

//    private void cerrarTeclado()
//    {
//        // Check if no view has focus:
//        //View view = this.getCurrentFocus();
//        edtArticulo.requestFocus();
////        if (view != null)
////        {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        if (imm != null)
//        {
//            imm.showSoftInput(edtArticulo, InputMethodManager.SHOW_IMPLICIT);
////                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
////        }
//    }

    private void calcularPrecioFinal()
    {
        if (edtBultos.getValue() > 0 || edtFracciones.getValue() > 0)
        {
            if (precioUnitarioBulto > 0)
            {
                int bultos = edtBultos.getValue() * articuloSeleccionado.unidadVenta;
                int unidades = edtFracciones.getValue();
                int cantidad = bultos + unidades;
                float descuento = Float.parseFloat(edtDescuento.getText().toString());
                float precioFinal = 0;
                if (descuento < 100)
                {
                    float importeDescuento = (precioUnitarioUnidad * descuento) / 100;
                    precioFinal = (precioUnitarioUnidad - importeDescuento) * cantidad;
                }
                edtFinal.setText(String.format("%.2f", precioFinal));
            }
        }
        else
        {
            edtFinal.setText(String.format("%.2f", 0.00));
        }
    }

    //private method of your class
    private int getIndex(Spinner spinner, Long rowid)
    {
        int i;
        for (i = 0; i < spinner.getCount(); i++)
        {
            Cursor value = (Cursor) spinner.getItemAtPosition(i);
            long id = value.getLong(value.getColumnIndex("_id"));
            if (id == rowid)
            {
                break;
            }
        }
        return i;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pedidos, menu);

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {

                Date d = new Date();
                CharSequence s = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime());
                foto = Environment.getExternalStorageDirectory() + "/Foto_" + idClienteSeleccionado + "_" + s.toString() + ".jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                output = Uri.fromFile(new File(foto));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                startActivityForResult(intent, INTENT_TAKE_PICTURE);
                return true;
            }
        });

        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem item)
            {
                Intent intent = new Intent(Pedidos.this, DescuentosConveniosCollection.class);
                intent.putExtra(ID_CLIENTE_SELECCIONADO, idClienteSeleccionado);
                startActivity(intent);
                return true;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            PosicionesGPS posicionesGPS = new PosicionesGPS();
            posicionesGPS.estado = EstadoGPS.CHECKOUT_CLIENTE;
            posicionesGPS.motivoNoCompra = MotivoNoCompra.COMPRADOR;
            posicionesGPS.idCliente = this.idClienteSeleccionado;
            posicionesGPS.bultos = calcularBultos();
            posicionesGPS.pesos = Float.valueOf(edtTotalPedido.getText().toString());
            posicionesGPS.usuario = loginUsuario;
            enviarPosicion(posicionesGPS);
        }

        return super.onKeyDown(keyCode, event);

    }

    private int calcularBultos()
    {

        int bultos = 0;

        Cursor cursor = consultarPedidos();

        if (cursor != null && cursor.moveToFirst())
        {
            do
            {
                int bultosPedidos = cursor.getInt(3);

                bultos = bultos + bultosPedidos;

            } while (cursor.moveToNext());
        }
        return bultos;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
