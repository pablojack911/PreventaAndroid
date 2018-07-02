package inteldev.android.presentation;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.Articulo;
import inteldev.android.modelo.CabOper;
import inteldev.android.modelo.DetBonif;
import inteldev.android.modelo.DetOper;
import inteldev.android.modelo.EstadoGPS;
import inteldev.android.modelo.MotivoNoCompra;
import inteldev.android.modelo.OferEsp;
import inteldev.android.modelo.OferOper;
import inteldev.android.modelo.PosicionesGPS;
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
import inteldev.android.negocios.WebServiceHelper;
import inteldev.android.presentation.DescuentosConvenios.DescuentosConveniosCollection;
import inteldev.android.presentation.vistaModelo.DetallePedido;
import inteldev.android.servicios.GPSIntentService;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static inteldev.android.CONSTANTES.ARTICULO_SELECCIONADO;
import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;
import static inteldev.android.CONSTANTES.INTENT_BUSCAR_ARTICULO;
import static inteldev.android.CONSTANTES.INTENT_CONVENIO;
import static inteldev.android.CONSTANTES.INTENT_OFERTA;
import static inteldev.android.CONSTANTES.INTENT_TAKE_PICTURE;
import static inteldev.android.CONSTANTES.NOMBRE_CLIENTE_KEY;
import static inteldev.android.CONSTANTES.POSICION_GPS_KEY;
import static inteldev.android.presentation.Utiles.cambiaVisibilidad;

public class PedidosMayorista extends AppCompatActivity
{
    Uri output;
    EditText edtArticulo;
    EditText edtDescuento;
    TextView edtCodigoArticulo;
    TextView edtArticuloSeleccionado;
    TextView edtUnitarioDtoFolder;
    TextView edtUnitarioLista;
    TextView edtFinal;
    TextView edtTotalPedido;
    com.travijuu.numberpicker.library.NumberPicker edtBultos;
    com.travijuu.numberpicker.library.NumberPicker edtFracciones;
    String idClienteSeleccionado = null;
    String claveUnicaCabOper = null;
    String nombre;
    TabHost.TabSpec tabDetalle;
    TabHost.TabSpec tabPedido;
    String tipoConvenio;
    String detalleTipoConvenio;
    float descuentoConvenio;
    int cant_sc;
    EditText edtBuscadorCodigoOferta;
    Button btnBuscarCodigoArticulo;
    ControladorPedido controladorPedido;
    ControladorOferta controladorOferta;
    ControladorPrecio controladorPrecio;
    ControladorArticulo controladorArticulo;
    ControladorStock controladorStock;
    ControladorPosicionesGPS controladorPosicionesGPS;
    ArrayList<OferEsp> listaOferEsp;
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
    private float precioFolderDto;
    private TextView tvFacciones;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos_mayorista);
        loginUsuario = SharedPreferencesManager.getLoginUsuario(PedidosMayorista.this);
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

//        configuraEdtArticulo();
//        configuraBultosUnidades();
//        configuraMotivoNoCompra();
//        configuraDescuento();
//        configuraBtnOk();
//        configuraBtnBuscarCodigoArticulo();
        edtArticuloSeleccionado = findViewById(R.id.edtArticuloSeleccionado);
        edtCodigoArticulo = findViewById(R.id.edtCodigoArticulo);
        edtUnitarioDtoFolder = findViewById(R.id.edtUnitarioDtoFolder);
        edtUnitarioLista = findViewById(R.id.edtUnitarioLista);
        edtFinal = findViewById(R.id.edtFinal);
        edtTotalPedido = findViewById(R.id.edtTotalPedido);

        tabs.setCurrentTab(0);

        dao = FabricaNegocios.obtenerDao(this);
        controladorPedido = FabricaNegocios.obtenerControladorPedido(PedidosMayorista.this);
        controladorOferta = FabricaNegocios.obtenerControladorOferta(PedidosMayorista.this);
        controladorPrecio = FabricaNegocios.obtenerControladorPrecio(PedidosMayorista.this);
        controladorArticulo = FabricaNegocios.obtenerControladorArticulo(PedidosMayorista.this);
        controladorStock = FabricaNegocios.obtenerControladorStock(PedidosMayorista.this);
        controladorPosicionesGPS = FabricaNegocios.obtenerControladorPosicionesGPS(PedidosMayorista.this);

//        cargarMotivoNoCompra();



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












    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pedidos_mayorista, menu);

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
                Intent intent = new Intent(PedidosMayorista.this, DescuentosConveniosCollection.class);
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
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
