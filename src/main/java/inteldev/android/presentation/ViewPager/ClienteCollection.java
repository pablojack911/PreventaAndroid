package inteldev.android.presentation.ViewPager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.EditText;

import inteldev.android.R;
import inteldev.android.modelo.EstadoGPS;
import inteldev.android.modelo.MotivoNoCompra;
import inteldev.android.modelo.PosicionesGPS;
import inteldev.android.negocios.ControladorPedido;
import inteldev.android.negocios.ControladorPosicionesGPS;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.Fecha;
import inteldev.android.negocios.SharedPreferencesManager;
import inteldev.android.presentation.DialogoAlertaSiNo;
import inteldev.android.presentation.FabricaMensaje;

import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;
import static inteldev.android.CONSTANTES.NOMBRE_CLIENTE_KEY;

/**
 * Created by Pocho on 31/03/2017.
 */

public class ClienteCollection extends AppCompatActivity implements Updateable
{
    ClienteCollectionPagerAdapter mClienteCollectionPagerAdapter;
    ViewPager mViewPager;
    private EditText edtTotalPedido;
    private String loginUsuario;
    private String idClienteSeleccionado;
    private ControladorPedido controladorPedido;
    private ControladorPosicionesGPS controladorPosicionesGPS;
    private String claveUnicaCabOper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        loginUsuario = SharedPreferencesManager.getLoginUsuario(ClienteCollection.this);
        if (getIntent().hasExtra(ID_CLIENTE_SELECCIONADO) && getIntent().hasExtra(NOMBRE_CLIENTE_KEY))
        {
            idClienteSeleccionado = getIntent().getStringExtra(ID_CLIENTE_SELECCIONADO);
            String nombre = getIntent().getStringExtra(NOMBRE_CLIENTE_KEY);
            setTitle(idClienteSeleccionado + " - " + nombre);
        }
        else
        {
            idClienteSeleccionado = null;
        }

        mClienteCollectionPagerAdapter = new ClienteCollectionPagerAdapter(getApplicationContext(), getSupportFragmentManager());

        controladorPedido = FabricaNegocios.obtenerControladorPedido(ClienteCollection.this);
        controladorPosicionesGPS = FabricaNegocios.obtenerControladorPosicionesGPS(ClienteCollection.this);

        claveUnicaCabOper = controladorPedido.ObtenerClaveUnicaPedido(idClienteSeleccionado, Fecha.obtenerFechaActual());
        if (claveUnicaCabOper != null)
        {
            FabricaMensaje.dialogoAlertaPrecarga(ClienteCollection.this, "NUEVA PRECARGA", "¿Desea crear una nueva precarga?", new DialogoAlertaSiNo()
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
        mClienteCollectionPagerAdapter.clienteSeleccionado = idClienteSeleccionado;
        mClienteCollectionPagerAdapter.claveUnicaCabOper = claveUnicaCabOper;
        mViewPager = findViewById(R.id.cliente_pager);
        mViewPager.setAdapter(mClienteCollectionPagerAdapter);
        edtTotalPedido = findViewById(R.id.edtTotalPedido);
        calcularTotal();
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);
    }

    private void calcularTotal()
    {
        float total = controladorPedido.calcularTotal(idClienteSeleccionado);
        if (total != 0)
        {
            edtTotalPedido.setText(String.format("%.2f", total));
        }
        else
        {
            edtTotalPedido.setText("");
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        calcularTotal();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==INTENT_TAKE_PICTURE)
//        {
//            ImageView iv = findViewById(R.id.ivCliente);
//            iv.setImageBitmap(BitmapFactory.decodeFile(foto));
//
//            File file = new File(foto);
//            if (file.exists())
//            {
//                String base64 = ConvertirFotoABase64.convertir(PedidosMayorista.this, output);
//                WebServiceHelper webServiceHelper = new WebServiceHelper();
//                //Subir foto Cliente
//                webServiceHelper.addService("http://www.inteldevmobile.com.ar", "SubirFotoCliente", "http://mhergo.ddns.net:8888/inteldevwebservice/Service.asmx?WSDL", "http://www.inteldevmobile.com.ar/SubirFotoCliente", "SubirFotoCliente", 2000);
//                webServiceHelper.addMethodParameter("SubirFotoCliente", "usuario", loginUsuario);
//                webServiceHelper.addMethodParameter("SubirFotoCliente", "cliente", idClienteSeleccionado);
//                webServiceHelper.addMethodParameter("SubirFotoCliente", "foto", base64);
//                SoapObject respuesta = webServiceHelper.executeService("SubirFotoCliente");
//                Toast.makeText(PedidosMayorista.this, respuesta.getProperty(0).toString(), Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                Toast.makeText(PedidosMayorista.this, "No se ha realizado la foto", Toast.LENGTH_SHORT).show();
//            }
//        }
        calcularTotal();
        for (Fragment fragment : getSupportFragmentManager().getFragments())
        {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            PosicionesGPS posicionesGPS = new PosicionesGPS();
            posicionesGPS.estado = EstadoGPS.CHECKOUT_CLIENTE;
            posicionesGPS.motivoNoCompra = MotivoNoCompra.COMPRADOR;
            posicionesGPS.idCliente = idClienteSeleccionado;
            posicionesGPS.bultos = controladorPedido.calcularBultos(idClienteSeleccionado);
            posicionesGPS.pesos = controladorPedido.calcularTotal(idClienteSeleccionado);
            posicionesGPS.usuario = loginUsuario;
            controladorPosicionesGPS.enviarPosicion(ClienteCollection.this, posicionesGPS);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void updateTotalPedido()
    {
        calcularTotal();
    }
}
