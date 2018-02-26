package inteldev.android.presentation;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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
import inteldev.android.negocios.SpinnerManager;
import inteldev.android.negocios.WebServiceHelper;
import inteldev.android.presentation.DescuentosConvenios.DescuentosConveniosCollection;
import inteldev.android.presentation.vistaModelo.DetallePedido;
import inteldev.android.servicios.GPSIntentService;

import static inteldev.android.CONSTANTES.CLAVE_UNICA_CABOPER;
import static inteldev.android.CONSTANTES.CLIENTE_KEY;
import static inteldev.android.CONSTANTES.CODIGO_OFERTA;
import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;
import static inteldev.android.CONSTANTES.INTENT_CONVENIO;
import static inteldev.android.CONSTANTES.INTENT_OFERTA;
import static inteldev.android.CONSTANTES.NOMBRE_CLIENTE_KEY;
import static inteldev.android.CONSTANTES.POSICION_GPS_KEY;
import static inteldev.android.CONSTANTES.USUARIO_KEY;

public class Pedidos extends FragmentActivity
{

//    public OferOper oferOper;

    //    public static LocationManager locationManager = null;
    Uri output;
    Spinner spLinea;
    Spinner spRubro;

    //    String idA;
    Spinner spArticulo;
    EditText edtCodigoArticulo;
    EditText edtArticulo;
    EditText edtUnitario;
    EditText edtDescuento;
    EditText edtCantidad;
    EditText edtFinal;
    String idLineaSeleccionada = "XXX";
    String idRubroSeleccionado = "XXX";
    String idArticuloSeleccionado = null;
    String idClienteSeleccionado = null;
    Boolean modoBuscadorArticulo = false;
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

    //    boolean continua;
    EditText edtTotalPedido;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        loginUsuario = LoginObservable.getInstancia().getLoginUsuario();
//        if (this.getIntent().hasExtra(USUARIO_KEY))
//        {
//            loginUsuario = this.getIntent().getStringExtra(USUARIO_KEY);
//        }
        Intent intent = this.getIntent();
        if (intent.hasExtra(CLIENTE_KEY) && intent.hasExtra(NOMBRE_CLIENTE_KEY))
        {
            idClienteSeleccionado = intent.getStringExtra(CLIENTE_KEY);
            nombre = intent.getStringExtra(NOMBRE_CLIENTE_KEY);
            setTitle(idClienteSeleccionado + " - " + nombre);
        }

//        Resources res = getResources();
//        Bundle bundle = this.getIntent().getExtras();
//        idClienteSeleccionado = bundle.getString("Cliente");

        final TabHost tabs = (TabHost) findViewById(android.R.id.tabhost);
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

        edtArticulo = (EditText) findViewById(R.id.edtArticulo);

        tabs.setCurrentTab(0);

        spLinea = (Spinner) findViewById(R.id.spLinea);
        spRubro = (Spinner) findViewById(R.id.spRubro);
        spArticulo = (Spinner) findViewById(R.id.spArticulo);

        edtCodigoArticulo = (EditText) findViewById(R.id.edtCodigoArticulo);
        btnBuscarCodigoArticulo = (Button) findViewById(R.id.btnBuscarCodigoArticulo);

        edtUnitario = (EditText) findViewById(R.id.edtUnitario);
        edtDescuento = (EditText) findViewById(R.id.edtDescuento);
        edtCantidad = (EditText) findViewById(R.id.edtCantidad);
        edtFinal = (EditText) findViewById(R.id.edtFinal);

        edtBuscadorCodigoOferta = (EditText) findViewById(R.id.edtBuscadorCodigoOferta);

        dao = FabricaNegocios.obtenerDao(this);

        edtTotalPedido = (EditText) findViewById(R.id.edtTotalPedido);

        rbCerrado = (RadioButton) findViewById(R.id.rbCerrado);
        rbNoEstabaElResponsable = (RadioButton) findViewById(R.id.rbNoEstabaElResponsable);
        rbTieneStock = (RadioButton) findViewById(R.id.rbTieneStock);
        rbNoTieneDinero = (RadioButton) findViewById(R.id.rbNoTieneDinero);
        rgMotivoNoCompra = (RadioGroup) findViewById(R.id.rgMotivosNoCompra);

        btnSeleccionMotivoNoCompra = (Button) findViewById(R.id.btnOkNoCompra);
        btnSeleccionMotivoNoCompra.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // Returns an integer which represents the selected radio button's ID
                int selected = rgMotivoNoCompra.getCheckedRadioButtonId();
                int motivo = MotivoNoCompra.NO_HAY_DATOS_REGISTRADOS;
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

        controladorPedido = FabricaNegocios.obtenerControladorPedido(getApplicationContext());
        controladorOferta = FabricaNegocios.obtenerControladorOferta(getApplicationContext());
        controladorPrecio = FabricaNegocios.obtenerControladorPrecio(getApplicationContext());
        controladorArticulo = FabricaNegocios.obtenerControladorArticulo(getApplicationContext());
        controladorStock = FabricaNegocios.obtenerControladorStock(getApplicationContext());
        controladorPosicionesGPS = FabricaNegocios.obtenerControladorPosicionesGPS(getApplicationContext());

        claveUnicaCabOper = controladorPedido.ObtenerClaveUnicaPedido(idClienteSeleccionado, Fecha.obtenerFechaActual());

        spLinea(this);

        edtArticulo(this);
        btnMas(this);
        btnMenos(this);

        edtCantidad(this);

        spDescuento(this);

        btnOk(this);

        lvPedidos(this);

        edtArticulo.requestFocus();

        seleccionOferta(this);

        edtBuscadorCodigoOferta(this);

        btnBuscarCodigoArticulo(this);

        cargarMotivoNoCompra(this);
    }

    private void enviarPosicion(PosicionesGPS posicionesGPS)
    {
        Intent mServiceIntent = new Intent(this, GPSIntentService.class);
        mServiceIntent.putExtra(POSICION_GPS_KEY, posicionesGPS);
        this.startService(mServiceIntent);
    }

    private void cargarMotivoNoCompra(Context context)
    {
        int motivo = controladorPosicionesGPS.obtenerMotivoNoCompra(idClienteSeleccionado, Fecha.obtenerFechaActual().toString());
        switch (motivo)
        {
            case 1:
//                rbCerrado.setSelected(true);
                rgMotivoNoCompra.check(R.id.rbCerrado);
                break;
            case 2:
//                rbNoEstabaElResponsable.setSelected(true);
                rgMotivoNoCompra.check(R.id.rbNoEstabaElResponsable);
                break;
            case 3:
//                rbTieneStock.setSelected(true);
                rgMotivoNoCompra.check(R.id.rbTieneStock);
                break;
            case 4:
//                rbNoTieneDinero.setSelected(true);
                rgMotivoNoCompra.check(R.id.rbNoTieneDinero);
                break;
            default:
                break;
        }
    }

    private void btnBuscarCodigoArticulo(final Context context)
    {

        btnBuscarCodigoArticulo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                buscarArticulo(context, true);
            }
        });

    }

    private void edtBuscadorCodigoOferta(final Pedidos pedidos)
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
                String codigo = edtBuscadorCodigoOferta.getText().toString();

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


    private void seleccionOferta(final Context context)
    {
        listaOferEsp = controladorOferta.obtenerOfertas(idClienteSeleccionado);
        lvOfertas = (ListView) findViewById(R.id.lvOferta);

        lvOfertas.setAdapter(new AdaptadorListaGenerico(this, R.layout.activity_lista_oferta, listaOferEsp)
        {
            @Override
            public void onEntrada(Object entrada, View view)
            {

                TextView tvCodigoOferta = (TextView) view.findViewById(R.id.tvCodigoOferta);
                TextView tvNombreOferta = (TextView) view.findViewById(R.id.tvNombreOferta);

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

                Intent intentOferta = new Intent(getApplicationContext(), Oferta.class);
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

        if (requestCode == INTENT_CONVENIO)
        {
            if (data != null)
            {
                tipoConvenio = data.getStringExtra("tipo");
                detalleTipoConvenio = data.getStringExtra("detalletipo");
                descuentoConvenio = data.getFloatExtra("descuento", 0);
                claveUnicaCabOper = data.getStringExtra(CLAVE_UNICA_CABOPER);

                edtDescuento.setText(String.valueOf(descuentoConvenio));

                calcularPrecioFinal();

            }
        }
        else
        {
            if (requestCode == INTENT_OFERTA)
            {
                lvPedidos(getApplicationContext());
            }
            else
            {
                if (requestCode == 115)
                {

                    ImageView iv = (ImageView) findViewById(R.id.ivCliente);
                    iv.setImageBitmap(BitmapFactory.decodeFile(foto));

                    File file = new File(foto);
                    if (file.exists())
                    {

                        String base64 = ConvertirFotoABase64.convertir(getApplicationContext(), output);

                        WebServiceHelper webServiceHelper = new WebServiceHelper();
                        //Subir foto Cliente
                        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "SubirFotoCliente", "http://mhergo.ddns.net:8888/inteldevwebservice/Service.asmx?WSDL", "http://www.inteldevmobile.com.ar/SubirFotoCliente", "SubirFotoCliente", 0);
//                        webServiceHelper.addService("http://www.inteldevmobile.com.ar", "SubirFotoCliente", "http://192.168.1.99:8888/inteldevwebservice/Service.asmx?WSDL", "http://www.inteldevmobile.com.ar/SubirFotoCliente", "SubirFotoCliente", 0);

                        webServiceHelper.addMethodParameter("SubirFotoCliente", "usuario", loginUsuario);
                        webServiceHelper.addMethodParameter("SubirFotoCliente", "cliente", idClienteSeleccionado);
                        webServiceHelper.addMethodParameter("SubirFotoCliente", "foto", base64);

                        SoapObject respuesta = webServiceHelper.executeService("SubirFotoCliente");
                        Toast.makeText(getApplicationContext(), respuesta.getProperty(0).toString(), Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "No se ha realizado la foto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void spDescuento(Pedidos pedidos)
    {
        Button btnDescuento = (Button) findViewById(R.id.btnDescuento);
        btnDescuento.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                Intent intentConvenio = new Intent(getApplicationContext(), Convenio.class);
                startActivityForResult(intentConvenio, CONSTANTES.INTENT_CONVENIO);
            }
        });

    }

    private void lvPedidos(final Context context)
    {

        ArrayList<DetallePedido> listadetallePedido = new ArrayList<DetallePedido>();

        final DetallePedido detallePedido;

        final Dao dao = new Dao(context);

        Cursor cursor = consultarPedidos();

        calcularTotal(cursor);

        if (cursor != null && cursor.moveToFirst())
        {
            Mapeador<DetallePedido> detallePedidoMapeador = new Mapeador<DetallePedido>(new DetallePedido());

//            listadetallePedido = detallePedidoMapeador.cursorToList(cursor);
            listadetallePedido = detallePedidoMapeador.cursorToList(cursor);
        }

        final ListView lvPedidos = (ListView) findViewById(R.id.lvPedidos);

        lvPedidos.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        lvPedidos.setAdapter(new AdaptadorListaGenerico(this, R.layout.activity_lista_pedido, listadetallePedido)
        {
            @Override
            public void onEntrada(Object entrada, View view)
            {

                TextView tvCodigoArticulo = (TextView) view.findViewById(R.id.tvCodigoArticulo);
                TextView tvArticulo = (TextView) view.findViewById(R.id.tvArticulo);
                TextView tvCantidad = (TextView) view.findViewById(R.id.tvCantidad);
                TextView tvPrecio = (TextView) view.findViewById(R.id.tvPrecio);
                TextView tvDescuento = (TextView) view.findViewById(R.id.tvDescuento);
                TextView tvFinal = (TextView) view.findViewById(R.id.tvFinal);

                TextView tvEtiquetaDescuento = (TextView) view.findViewById(R.id.tvEtiquetaDescuento);

                Float precio = 0f;

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

                    tvEtiquetaDescuento.setVisibility(View.VISIBLE);
                    tvDescuento.setVisibility(View.VISIBLE);

                    precio = ((DetallePedido) entrada).getPrecio();
                }
                else
                {
                    String codigoOferta = ((DetallePedido) entrada).getOferta();
                    tvCodigoArticulo.setText("OFERTA " + codigoOferta);

                    OferEsp oferEsp = controladorOferta.obtenerOfertaPorCodigo(codigoOferta);
                    tvArticulo.setText(oferEsp.Nombre);

                    tvEtiquetaDescuento.setVisibility(View.INVISIBLE);
                    tvDescuento.setVisibility(View.INVISIBLE);

                    precio = ((DetallePedido) entrada).getPrecio() / ((DetallePedido) entrada).getEntero();

                }

                int cantidad = ((DetallePedido) entrada).getEntero();
                tvCantidad.setText(String.valueOf(cantidad));

                tvPrecio.setText(String.valueOf(precio));

                float total = 0;
                float descuento = ((DetallePedido) entrada).getDescuento();
                tvDescuento.setText(String.valueOf(descuento));

                if (descuento < 100)
                {
                    total = (precio - ((precio * descuento) / 100)) * cantidad;
                }
                tvFinal.setText(String.valueOf(total));
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
                            dao.delete("detOper", "idFila='" + elegido.getIdFila() + "'");
                        }
                        else
                        {
                            String idFila = elegido.getIdFila();
                            dao.delete("detOper", "idFila='" + elegido.idFila + "'");
                            dao.delete("oferOper", "claveUnica='" + idFila + "'");
                        }
                        lvPedidos(context);
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

                    Intent intentDetalle_Oferta = new Intent(getApplicationContext(), Detalle_Oferta.class);
                    intentDetalle_Oferta.putExtra("claveUnica", elegido.getIdFila());
                    startActivity(intentDetalle_Oferta);
                }
            }
        });

    }

    private Cursor consultarPedidos()
    {

        return dao.ejecutarConsultaSql("select cabOper.idCliente," + "cabOper.fecha, " + "detOper.idArticulo," + "detOper.entero," + "detOper.precio," + "detOper.descuento," + "detOper.idFila," + "detOper.oferta," + "detOper.claveUnica " + "  from cabOper" + " inner join detOper on detOper.claveUnica = cabOper.claveUnica " + " where cabOper.idCliente = '" + idClienteSeleccionado + "'" + "  and cabOper.fecha = '" + Fecha.obtenerFechaActual().toString() + "'");
    }

    private void calcularTotal(Cursor cursor)
    {
        float total = 0f;

        if (cursor != null && cursor.moveToFirst())
        {

            do
            {
                float precio = cursor.getFloat(4);
                int cantidad = cursor.getInt(3);
                float descuento = cursor.getFloat(5);

                float importeDescuento = precio * (descuento / 100);
                total = total + ((precio - importeDescuento) * cantidad);


            } while (cursor.moveToNext());
        }

        edtTotalPedido.setText(String.valueOf(total));
    }

    private void btnOk(final Context context)
    {

        Button btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (edtCantidad.getText().toString().equals("0") || edtCantidad.getText().toString().isEmpty())
                {
                    FabricaMensaje.dialogoOk(context, "Faltan Datos".toUpperCase(), "No indicó la cantidad.", new DialogoAlertaNeutral()
                    {
                        @Override
                        public void Neutral()
                        {

                        }
                    }).show();
                }
                else
                {
                    if (!controladorStock.hayStock(idArticuloSeleccionado))
                    {
                        FabricaMensaje.dialogoOk(context, "alerta".toUpperCase(), "No hay stock.", new DialogoAlertaNeutral()
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
                            detOper.idArticulo = idArticuloSeleccionado;
                        }
                        else
                        {
                            detOper.idArticulo = "CONV " + idClienteSeleccionado;
                        }

                        detOper.descuento = Float.valueOf(edtDescuento.getText().toString());
                        detOper.entero = Integer.valueOf(edtCantidad.getText().toString());
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
                            oferOper.idArticulo = idArticuloSeleccionado;
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

                        lvPedidos(context);

                        edtCantidad.setText("0");
                        edtDescuento.setText("0.0");
                        descuentoConvenio = 0;
                        tipoConvenio = "";
                        detalleTipoConvenio = "";
                        edtFinal.setText("0.0");
                        edtArticulo.setText("");
                        edtArticulo.requestFocus();
                    }
                }
            }
        });

    }


    private void spLinea(final Context context)
    {
        Linea linea = new Linea();

        //String consulta = "select cast(idLinea as int) as _id,nombre from lineas order by nombre";

        String consulta = "select rowid as _id, nombre from lineas order by nombre ";

        SpinnerManager<Linea> llenarLinea = new SpinnerManager<Linea>(linea, context, spLinea);
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
                        spRubro(context);
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

        spLinea.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                modoBuscadorArticulo = false;
                return false;
            }
        });
    }

    private void spRubro(final Context context)
    {
        Rubro rubro = new Rubro();

        String consulta = "SELECT distinct rubros.rowid  as _id, rubros.nombre from rubros join articulos as art on art.idrubro = rubros.idrubro where art.idLinea = '" + idLineaSeleccionada + "'";

        SpinnerManager<Rubro> llenarRubro = new SpinnerManager<Rubro>(rubro, context, spRubro);
        llenarRubro.llenarDesdeCursor(consulta, "nombre");
        Boolean noentres;
        spRubro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                if (!modoBuscadorArticulo)
                {

                    Rubro rubroSeleccionado = controladorArticulo.buscarRubroPorRowId(l);

                    idRubroSeleccionado = rubroSeleccionado.idRubro; // String.format("%03d", l);
                    spArticulo(context);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });

        spRubro.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                modoBuscadorArticulo = false;
                return false;
            }
        });
    }

    private void spArticulo(final Context context)
    {
        Articulo articulo = new Articulo();

        String consulta = "select rowid as _id,nombre from articulos where idlinea = '" + idLineaSeleccionada + "' and idRubro = '" + idRubroSeleccionado + "' order by nombre ";

        SpinnerManager<Articulo> llenarArticulo = new SpinnerManager<Articulo>(articulo, context, spArticulo);
        llenarArticulo.llenarDesdeCursor(consulta, "nombre");
        spArticulo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                idArticuloSeleccionado = controladorArticulo.buscarArticuloPorRowId(l).idArticulo;
                edtCodigoArticulo.setText(idArticuloSeleccionado);

                cant_sc = 0;

                Float unitario = controladorPrecio.obtener(context, idClienteSeleccionado, idArticuloSeleccionado);

                EditText edUnitario = (EditText) findViewById(R.id.edtUnitario);
                edUnitario.setText(unitario.toString());

                edtDescuento.setText((String.valueOf(0)));

                ControladorConvenio controladorConvenio = FabricaNegocios.obtenerControladorConvenio(context, idClienteSeleccionado);

                final DetBonif detBonif = controladorConvenio.obtenerPorcentaje(idArticuloSeleccionado, false);
                edtDescuento.setText((String.valueOf(detBonif.porcentaje)));

                final DetBonif detBonifSinCargos = controladorConvenio.obtenerPorcentaje(idArticuloSeleccionado, true);


                java.sql.Date sqlDate = Fecha.sumarFechasDias(Fecha.obtenerFechaActual(), 0);
                int sinCargosUtilizados = controladorConvenio.obtenerSinCargosUtilizados(sqlDate, idArticuloSeleccionado);
                final int sinCargosDisponibles = detBonifSinCargos.cant_sc - sinCargosUtilizados;

                if (sinCargosDisponibles > 0)
                {

                    FabricaMensaje.dialogoAlertaSiNo(context, "Tiene " + sinCargosDisponibles + " disponibles", "¿Pide sin cargos?", new DialogoAlertaSiNo()
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
                else
                {
                    edtCantidad.setText(String.valueOf(1));
                }
                edtCantidad.requestFocus();
                edtCantidad.selectAll();
                calcularPrecioFinal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
            }
        });

        spArticulo.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                modoBuscadorArticulo = false;
                edtCantidad.setText("1");
                edtCantidad.requestFocus();
                edtCantidad.selectAll();
                return false;
            }
        });

    }

    private void edtArticulo(final Pedidos pedidos)
    {
        //NO SE BUSCA MAS DESDE onTextChanged
//        final EditText edtArticulo = (EditText) findViewById(R.id.edtArticulo);

//        edtArticulo.addTextChangedListener(new TextWatcher()
//        {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
//            {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
//            {
//                buscarArticulo(pedidos, false);
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable)
//            {
//
//            }
//        });
    }

    private void buscarArticulo(Context context, boolean desdeBtnBuscarArticulo)
    {

        String articulo = edtArticulo.getText().toString();

//        Dao controladorDb = new Dao(context);
//
//        String cadenaPorcentual = "%";
//
//        if (desdeBtnBuscarArticulo)
//        {
//            cadenaPorcentual = "";
//        }
//
//        Cursor cursor = controladorDb.ejecutarConsultaSql("select idArticulo, idLinea, idRubro from articulos where idArticulo like '" + articulo + cadenaPorcentual + "'");

//        if (cursor.moveToFirst())
//        {
//            if (desdeBtnBuscarArticulo || cursor.getCount() == 1)
//            {
//
        Articulo art = controladorArticulo.buscarArticulo(articulo);
        if (art != null)
        {
            modoBuscadorArticulo = true;

            long idArticulo = controladorArticulo.buscarRowIdArticuloPorCodigo(art.idArticulo);
            long idLinea = controladorArticulo.buscarRowIdLineaPorCodigo(art.idLinea);
            long idRubro = controladorArticulo.buscarRowIdRubroPorRowId(art.idRubro);

            spLinea.setSelection(getIndex(spLinea, idLinea));
            idLineaSeleccionada = art.idLinea;

            spRubro(this);

            spRubro.setSelection(getIndex(spRubro, idRubro));
            idRubroSeleccionado = art.idRubro;

            spArticulo(this);
            spArticulo.setSelection(getIndex(spArticulo, idArticulo));
            idArticuloSeleccionado = art.idArticulo;

            edtCantidad.setText("1");
            edtArticulo.setText("");
            edtCantidad.requestFocus();
        }
    }

    private void edtCantidad(final Pedidos pedidos)
    {
        edtCantidad.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }


            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {
                if (cant_sc != 0)
                {
                    if (Integer.valueOf(edtCantidad.getText().toString()) > cant_sc)
                    {
                        Toast.makeText(getApplicationContext(), "Esta pidiendo mas sin cargos que los disponibles: " + String.valueOf(cant_sc), Toast.LENGTH_LONG).show();
                        edtCantidad.setText(String.valueOf(cant_sc));
                    }
                }
                calcularPrecioFinal();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });

    }

    private void calcularPrecioFinal()
    {
        if (!edtCantidad.getText().toString().isEmpty())
        {
            if (!edtUnitario.getText().toString().isEmpty())
            {
                float precioUnitario = Float.parseFloat(edtUnitario.getText().toString());
                int cantidad = Integer.parseInt(edtCantidad.getText().toString());
                float descuento = Float.parseFloat(edtDescuento.getText().toString());
                float precioFinal = 0;
                if (descuento < 100)
                {
                    float importeDescuento = (precioUnitario * descuento) / 100;
                    precioFinal = (precioUnitario - importeDescuento) * cantidad;
                }
                edtFinal.setText(String.valueOf(precioFinal));
            }
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

    private void btnMas(Context context)
    {
        Button btnMas = (Button) findViewById(R.id.btnMas);

        btnMas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String valor = edtCantidad.getText().toString();
                int numero = 0;
                if (!valor.trim().equals(""))
                {
                    numero = Integer.parseInt(valor);
                }
                numero = numero + 1;
                edtCantidad.setText(String.format("%d", numero));
                edtCantidad.requestFocus();
                edtCantidad.selectAll();
            }
        });

    }

    private void btnMenos(Context context)
    {
        Button btnMenos = (Button) findViewById(R.id.btnMenos);

        btnMenos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String valor = edtCantidad.getText().toString();
                int numero = 0;
                if (valor.trim() != "")
                {
                    numero = Integer.parseInt(valor);
                    if (numero > 0)
                    {
                        numero = numero + -1;
                    }
                }
                edtCantidad.setText(String.format("%d", numero));
                edtCantidad.requestFocus();
                edtCantidad.selectAll();
            }
        });

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

                final int TAKE_PICTURE = 115;
                Date d = new Date();
                CharSequence s = DateFormat.format("MM-dd-yy hh-mm-ss", d.getTime());
                foto = Environment.getExternalStorageDirectory() + "/Foto_" + idClienteSeleccionado + "_" + s.toString() + ".jpg";
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                output = Uri.fromFile(new File(foto));
                intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
                startActivityForResult(intent, TAKE_PICTURE);

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
        return id == R.id.action_settings ? true : super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {

        /*  String whereVisitado = " not exists ( select claveunica from caboper where " +
                    "caboper.idCliente = cl.idCliente and caboper.fecha = '" + Fecha.obtenerFechaActual().toString() + "')" +
                    "and not exists (select idCliente from PosicionesGPS where motivonocompra!=0 and PosicionesGPS.idCliente = cl.idCliente " +
                    "and strftime('%Y-%m-%d', PosicionesGPS.Fecha)== '" + Fecha.obtenerFechaActual().toString() + "')";

            Cursor cursor = dao.ejecutarConsultaSql("select idCliente from clientes as cl where " + whereVisitado + " and idCliente='" + idClienteSeleccionado + "'");

            continua = true;

            if (cursor.moveToFirst()) {
                FabricaMensaje.dialogoAlertaSiNo(this, "Atencion no especifico Motivo de no Compra!", "¿Seguro que desea salir?", new DialogoAlertaSiNo() {
                    @Override
                    public void Positivo() {
                        Pedidos.this.finish();
                    }

                    @Override
                    public void Negativo() {
                        continua = false;
                    }
                }).show();
            }

            if (continua == true) {*/
//
//            location = controladorPosicionesGPS.obtenerUbicacion(getApplicationContext());
//
////            PosicionesGPS posicionesGPS = new PosicionesGPS();
//
//            Double lat;
//            Double lng;
//
//            if (location != null)
//            {
//                lat = location.getLatitude();
//                lng = location.getLongitude();
//            }
//            else
//            {
//                lat = 0.00;
//                lng = 0.00;
//            }
//
//            posicionesGPS.latitud = lat.floatValue();
//            posicionesGPS.longitud = lng.floatValue();
//            posicionesGPS.fecha = Fecha.obtenerFechaHoraActual();
//            posicionesGPS.enviado = 0;
//            posicionesGPS.estado = 7; // CHECKOUT_CLIENTE
//            posicionesGPS.motivoNoCompra = 0;
//            posicionesGPS.idCliente = idClienteSeleccionado;
//            posicionesGPS.pesos = Float.valueOf(edtTotalPedido.getText().toString());
//            posicionesGPS.bultos = calcularBultos();

//            PosicionesGPS posicionesGPS = controladorPosicionesGPS.creaPosicion(getApplicationContext(), MainActivity.loginUsuario, EstadoGPS.CHECKOUT_CLIENTE, MotivoNoCompra.COMPRADOR, this.idClienteSeleccionado, calcularBultos(), Float.valueOf(edtTotalPedido.getText().toString()));
            PosicionesGPS posicionesGPS = new PosicionesGPS();
            posicionesGPS.estado = EstadoGPS.CHECKOUT_CLIENTE;
            posicionesGPS.motivoNoCompra = MotivoNoCompra.COMPRADOR;
            posicionesGPS.idCliente = this.idClienteSeleccionado;
            posicionesGPS.bultos = calcularBultos();
            posicionesGPS.pesos = Float.valueOf(edtTotalPedido.getText().toString());
            posicionesGPS.usuario = loginUsuario;
            enviarPosicion(posicionesGPS);
//            Mapeador<PosicionesGPS> posicionGPSMapeador = new Mapeador<PosicionesGPS>(posicionesGPS);
//            ContentValues contentValues = posicionGPSMapeador.entityToContentValues();
//            controladorPosicionesGPS.insertar(posicionesGPS);
//            dao.insert("PosicionesGPS", contentValues);

        /*  }else {
                return true;
            }*/
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
