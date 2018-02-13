package inteldev.android.presentation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.serialization.SoapObject;

import inteldev.android.R;
import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.EstadoGPS;
import inteldev.android.modelo.PosicionesGPS;
import inteldev.android.negocios.CursorToXml;
import inteldev.android.negocios.EnviarEstadosGeoLocalizacion;
import inteldev.android.negocios.EstadoWebService;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.Fecha;
import inteldev.android.negocios.ServiceRegistry;
import inteldev.android.negocios.WebServiceHelper;
import inteldev.android.presentation.ViewPager.ClienteCollection;
import inteldev.android.servicios.GPSIntentService;
import inteldev.android.servicios.ServiceGeolocation;

import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;
import static inteldev.android.CONSTANTES.ID_VENDEDOR_KEY;
import static inteldev.android.CONSTANTES.INTENT_LOGIN;
import static inteldev.android.CONSTANTES.PASSWORD_KEY;
import static inteldev.android.CONSTANTES.POSICION_GPS_KEY;
import static inteldev.android.CONSTANTES.USUARIO_KEY;

public class MainActivity extends FragmentActivity implements OnLoginListener
{
    public static String loginUsuario = "";
    public static String loginIdVendedor = "";
    public static String loginPass = "";
    public Context context;
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        enable_buttons();
        Button btn = (Button) findViewById(R.id.btnPOCHO);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, ClienteCollection.class);
                intent.putExtra(ID_CLIENTE_SELECCIONADO, "90000");
                startActivity(intent);
            }
        });
        updateValuesFromBundle(savedInstanceState);
    }

    protected void updateValuesFromBundle(Bundle savedInstanceState)
    {
//        super.updateValuesFromBundle(savedInstanceState);
        if (savedInstanceState != null)
        {
            if (savedInstanceState.keySet().contains(PASSWORD_KEY))
            {
                loginPass = savedInstanceState.getString(PASSWORD_KEY);
            }
            if (savedInstanceState.keySet().contains(ID_VENDEDOR_KEY))
            {
                loginIdVendedor = savedInstanceState.getString(ID_VENDEDOR_KEY);
            }
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putString(USUARIO_KEY, loginUsuario);
        outState.putString(PASSWORD_KEY, loginPass);
        outState.putString(ID_VENDEDOR_KEY, loginIdVendedor);
        super.onSaveInstanceState(outState);
    }

    private void enable_buttons()
    {
        LoginObservable.getInstancia().setOnLoginListener(this);

        btnVentas(this);

        btnEnviar(this);

        context = this;

        System.setProperty("http.keepAlive", "true");
    }

    @Override
    public void onBackPressed()
    {
        if (loginUsuario != null && !loginUsuario.equals(""))
        {
            FabricaMensaje.dialogoAlertaSiNo(context, "¡Atencion!", "¿Seguro que desea salir?", new DialogoAlertaSiNo()
            {
                @Override
                public void Positivo()
                {
                    PosicionesGPS posicionesGPS = new PosicionesGPS();
                    posicionesGPS.usuario = loginUsuario;
                    posicionesGPS.estado = EstadoGPS.DESLOGUEADO;
                    posicionesGPS.idCliente = "00000";

                    Intent mServiceIntent = new Intent(MainActivity.this, GPSIntentService.class);
                    mServiceIntent.putExtra(POSICION_GPS_KEY, posicionesGPS);
                    MainActivity.this.startService(mServiceIntent);

                    EnviarEstadosGeoLocalizacion enviarEstadosGeoLocalizacion = new EnviarEstadosGeoLocalizacion();
                    enviarEstadosGeoLocalizacion.enviarEstadosGeoLocalizacion(getApplicationContext());

                    Intent intent = new Intent(MainActivity.this, ServiceGeolocation.class);
                    stopService(intent);

//                    finishAffinity();

                    finishAndRemoveTask();
                }

                @Override
                public void Negativo()
                {

                }
            }).show();
        }
        else
        {
            finish();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
//    protected void enviarPosicion()
//    {
//        PosicionesGPS posicionesGPS = new PosicionesGPS();
//        posicionesGPS.imei = FabricaNegocios.obtenerImei(this);
//        posicionesGPS.fecha = Fecha.obtenerFechaHoraActual();
//        posicionesGPS.usuario = loginUsuario;
//
//        Intent mServiceIntent = new Intent(this, GPSIntentService.class);
//        mServiceIntent.putExtra(POSICION_GPS_KEY, posicionesGPS);
//        this.startService(mServiceIntent);
//    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if (keyCode == KeyEvent.KEYCODE_BACK && loginUsuario != null || loginUsuario != "")
//        {
//            FabricaMensaje.dialogoAlertaSiNo(context, "¡Atencion!", "¿Seguro que desea salir?", new DialogoAlertaSiNo()
//            {
//                @Override
//                public void Positivo()
//                {
////                    PosicionesGPS posicionesGPS = controladorPosicionesGPS.creaPosicion(getApplicationContext(), MainActivity.loginUsuario, EstadoGPS.DESLOGUEADO, MotivoNoCompra.COMPRADOR, "00000", 0, 0);
////                    controladorPosicionesGPS.insertar(posicionesGPS);
////                    EnviarEstadosGeoLocalizacion enviarEstadosGeoLocalizacion = new EnviarEstadosGeoLocalizacion();
////                    enviarEstadosGeoLocalizacion.enviarEstadosGeoLocalizacion(getApplicationContext(), loginUsuario);
////
////                    Intent intent = new Intent(MainActivity.this, ServiceGeolocation.class);
////                    stopService(intent);
//
//                    finishAffinity();
//                    Process.killProcess(Process.myPid());
//                }
//
//                @Override
//                public void Negativo()
//                {
//
//                }
//            }).show();
//
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//
//    }

    private void btnEnviar(final Context context)
    {

        Button btnEnviar = (Button) findViewById(R.id.btnEnviar);

        btnEnviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

                if (!FabricaNegocios.obtenerEstadoConexion(connectivityManager).Conectado())
                {
                    FabricaMensaje.dialogoOk(MainActivity.this, "Sin Conexion", "No dispone de conexion 3G o Wifi", new DialogoAlertaNeutral()
                    {
                        @Override
                        public void Neutral()
                        {

                        }
                    }).show();
                }

                else
                {

                    EstadoWebService.Estado estadoWebService;

                    String servicioSubirDatosMobile;

                    EstadoWebService oEstadoWebService = FabricaNegocios.obtenerEstadoWebService(context, loginUsuario);

                    estadoWebService = oEstadoWebService.verificarConexionServicio("hola", "holaRemoto");

                    if (estadoWebService == EstadoWebService.Estado.NoDisponible)
                    {

                        FabricaMensaje.dialogoOk(MainActivity.this, "Verifique!", "Servicio Web no disponible", new DialogoAlertaNeutral()
                        {
                            @Override
                            public void Neutral()
                            {

                            }
                        }).show();
                        return;
                    }
                    else
                    {
                        if (estadoWebService == EstadoWebService.Estado.ConexionLocal)
                        {
                            servicioSubirDatosMobile = "subirDatosMobile3";
                        }
                        else
                        {
                            servicioSubirDatosMobile = "subirDatosMobile3Remoto";
                        }
                    }

                    IDao dao = FabricaNegocios.obtenerDao(context);

                    CursorToXml cursorToXml = new CursorToXml("NewDataSet");

                    Cursor cursorCabOper = dao.ejecutarConsultaSql("select claveUnica," +
                            "idOperacion," +
                            "idCliente," +
                            "fecha," +
                            "fechaEntrega," +
                            "domicilio," +
                            "enviado," +
                            "idvendedor as idvendedor from cabOper" +
                            " where enviado = 0" +
                            " or  caboper.claveunica in (select detoper.claveunica from detoper where enviado = 0)");

                    if (cursorCabOper.getCount() == 0)
                    {
                        FabricaMensaje.dialogoOk(context, "Información", "No hay datos a enviar", new DialogoAlertaNeutral()
                        {
                            @Override
                            public void Neutral()
                            {

                            }
                        }).show();
                    }
                    else
                    {

                        cursorToXml.AgregarTabla(cursorCabOper, "CabOper");

                        Cursor cursorDetOper = dao.ejecutarConsultaSql("select claveUnica," +
                                "idArticulo," +
                                "precio," +
                                "descuento," +
                                "entero," +
                                "fraccion," +
                                "enviado," +
                                "idFila as idfila," +
                                "0 as sincargo from detOper" +
                                " where enviado = 0");
                        cursorToXml.AgregarTabla(cursorDetOper, "DetOper");

                        Cursor cursorOferOper = dao.ejecutarConsultaSql("select claveunica as claveunica, " +
                                "idFila as idfila," +
                                "precio," +
                                "idArticulo," +
                                "descuento," +
                                "cantidad," +
                                "tipo from oferOper" +
                                " where enviado = 0");

                        cursorToXml.AgregarTabla(cursorOferOper, "oferOper");

                        String xml = cursorToXml.GenerarXml();
                        xml = xml.substring(38, xml.length());


                        String imei = FabricaNegocios.obtenerImei(context);

                        // Llamar a webservice
                        ServiceRegistry serviceRegistry = new ServiceRegistry(context, loginUsuario);
                        WebServiceHelper webServiceHelper = serviceRegistry.getWebServiceHelper();
                        webServiceHelper.addMethodParameter(servicioSubirDatosMobile, "usuario", loginUsuario);
                        webServiceHelper.addMethodParameter(servicioSubirDatosMobile, "clave", loginPass);
                        webServiceHelper.addMethodParameter(servicioSubirDatosMobile, "imei", imei);
                        webServiceHelper.addMethodParameter(servicioSubirDatosMobile, "xmlDatos", xml);

                        SoapObject respuesta = webServiceHelper.executeService(servicioSubirDatosMobile);

                        String respuestaString = respuesta.getProperty(0).toString();

                        if (respuestaString.equals("anyType{}"))
                        {
                            dao.ejecutarSentenciaSql("update CabOper set enviado = 1 where enviado = 0");
                            dao.ejecutarSentenciaSql("update DetOper set enviado = 1 where enviado = 0");
                            dao.ejecutarSentenciaSql("update OferOper set enviado = 1 where enviado = 0");

                            Cursor cursor = dao.ejecutarConsultaSql("select claveUnica from cabOper " +
                                    " where fecha<'" + Fecha.sumarFechasDias(Fecha.obtenerFechaActual(), 0).toString() + "'");

                            if (cursor != null && cursor.moveToFirst())
                            {
                                do
                                {

                                    String claveUnica = cursor.getString(0);

                                    dao.delete("CabOper", "claveUnica = '" + claveUnica + "'");

                                    Cursor cursorDetOperEnviado = dao.ejecutarConsultaSql("select idFila from DetOper" +
                                            " where claveUnica='" + claveUnica + "'");

                                    if (cursorDetOperEnviado != null && cursorDetOperEnviado.moveToFirst())
                                    {

                                        String idFila = cursorDetOperEnviado.getString(0);

                                        dao.delete("DetOper", "claveUnica = '" + claveUnica + "'");
                                        dao.delete("OferOper", "claveUnica = '" + idFila + "'");
                                    }

                                } while (cursor.moveToNext());
                            }
                            Toast.makeText(context, "Datos enviados", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            FabricaMensaje.dialogoOk(context, "Informe a Sistemas", respuestaString, new DialogoAlertaNeutral()
                            {
                                @Override
                                public void Neutral()
                                {

                                }
                            }).show();
                        }
                    }
                }
            }
        });

    }

    private void btnVentas(Context context)
    {
        Button btnVentas = (Button) findViewById(R.id.btnVentas);

        btnVentas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getApplicationContext(), RutaDeVenta.class);
                intent.putExtra(USUARIO_KEY, loginUsuario);
                startActivity(intent);
            }
        });

    }

    public void OnLoginChange(boolean estado)
    {
        Button btnVentas = (Button) findViewById(R.id.btnVentas);
        Button btnEnviar = (Button) findViewById(R.id.btnEnviar);

        btnVentas.setEnabled(estado);
        btnEnviar.setEnabled(estado);

        TextView tvVendedor = (TextView) findViewById(R.id.tvVendedor);

        if (LoginObservable.getInstancia().isLogin())
        {
            loginUsuario = LoginObservable.getInstancia().getLoginUsuario();
            Intent i = new Intent(MainActivity.this, ServiceGeolocation.class);
            startService(i);
            tvVendedor.setText("Usuario " + loginUsuario);
        }
        else
        {
            tvVendedor.setText("NO ESTA LOGUEADO");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivityForResult(intent, INTENT_LOGIN);
                return true;
            }
        });

        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {

                Intent intent = new Intent(getApplicationContext(), Recibir.class);
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
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
