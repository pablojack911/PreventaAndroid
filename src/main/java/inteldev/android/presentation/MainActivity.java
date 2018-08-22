package inteldev.android.presentation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.serialization.SoapObject;

import inteldev.android.CONSTANTES;
import inteldev.android.R;
import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.EstadoGPS;
import inteldev.android.modelo.PosicionesGPS;
import inteldev.android.negocios.CursorToXml;
import inteldev.android.negocios.EstadoWebService;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.ServiceRegistry;
import inteldev.android.negocios.SharedPreferencesManager;
import inteldev.android.negocios.WebServiceHelper;
import inteldev.android.servicios.GPSIntentService;
import inteldev.android.servicios.GPSLocationService;

import static inteldev.android.CONSTANTES.PERMISSIONS;
import static inteldev.android.CONSTANTES.POSICION_GPS_KEY;
import static inteldev.android.CONSTANTES.REQUEST_CHECK_SETTINGS;

public class MainActivity extends AppCompatActivity
{
    //    public static String loginUsuario = "";
    //    public static String loginIdVendedor = "";
    //    public static String loginPass = "";
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Utiles.hasPermissions(this, PERMISSIONS))
        {
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CHECK_SETTINGS);
        }
        bindUI();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    private void bindUI()
    {
        btnVentas();
        btnEnviar();
        enableButtons(SharedPreferencesManager.isLogin(MainActivity.this));
    }

    @Override
    public void onBackPressed()
    {
        if (SharedPreferencesManager.isLogin(MainActivity.this))
        {
            FabricaMensaje.dialogoAlertaSiNo(MainActivity.this, "¡Atencion!", "¿Seguro que desea salir?", new DialogoAlertaSiNo()
            {
                @Override
                public void Positivo()
                {
                    PosicionesGPS posicionesGPS = new PosicionesGPS();
                    posicionesGPS.usuario = SharedPreferencesManager.getLoginUsuario(MainActivity.this);
                    posicionesGPS.estado = EstadoGPS.DESLOGUEADO;
                    posicionesGPS.idCliente = "00000";

                    Intent mServiceIntent = new Intent(MainActivity.this, GPSIntentService.class);
                    mServiceIntent.putExtra(POSICION_GPS_KEY, posicionesGPS);
                    MainActivity.this.startService(mServiceIntent);
                    SharedPreferencesManager.setLogin(MainActivity.this, false);

                    finishAffinity();
                }

                @Override
                public void Negativo()
                {

                }
            }).show();
        }
        else
        {
            finishAffinity();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case CONSTANTES.REQUEST_CHECK_SETTINGS:
                if (grantResults.length > 0)
                {
                    for (int res : grantResults)
                    {
                        if (res != PackageManager.PERMISSION_GRANTED)
                        {
                            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_CHECK_SETTINGS);
                        }
                    }
                }
            case CONSTANTES.REQUEST_STORAGE_PERMISSION:
                if (grantResults.length > 0)
                {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CONSTANTES.REQUEST_STORAGE_PERMISSION);
                    }
                }
                break;
            case CONSTANTES.REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0)
                {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, CONSTANTES.REQUEST_LOCATION_PERMISSION);
                    }
                }
                break;
            case CONSTANTES.REQUEST_PHONE_STATE_PERMISSION:
                if (grantResults.length > 0)
                {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, CONSTANTES.REQUEST_PHONE_STATE_PERMISSION);
                    }
                }
                break;
        }
    }

    private void btnEnviar()
    {
        Button btnEnviar = findViewById(R.id.btnEnviar);
        btnEnviar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                String loginUsuario = SharedPreferencesManager.getLoginUsuario(MainActivity.this);
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
                    EstadoWebService oEstadoWebService = FabricaNegocios.obtenerEstadoWebService(MainActivity.this, loginUsuario);
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
                    IDao dao = FabricaNegocios.obtenerDao(MainActivity.this);

                    CursorToXml cursorToXml = new CursorToXml("NewDataSet");
                    String queryCabOper = "select claveUnica," + "idOperacion," + "idCliente," + "fecha," + "fechaEntrega," + "domicilio," + "enviado," + "idvendedor as idvendedor from cabOper" + " where enviado = 0" + " or  caboper.claveunica in (select detoper.claveunica from detoper where enviado = 0)";
                    Cursor cursorCabOper = dao.ejecutarConsultaSql(queryCabOper);

                    if (cursorCabOper.getCount() == 0)
                    {
                        FabricaMensaje.dialogoOk(MainActivity.this, "Información", "No hay datos a enviar", new DialogoAlertaNeutral()
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

                        String queryDetOper = "select claveUnica," + "idArticulo," + "precio," + "descuento," + "entero," + "fraccion," + "enviado," + "idFila as idfila," + "0 as sincargo from detOper" + " where enviado = 0";
                        Cursor cursorDetOper = dao.ejecutarConsultaSql(queryDetOper);
                        cursorToXml.AgregarTabla(cursorDetOper, "DetOper");

                        String queryOferOper = "select claveunica as claveunica, " + "idFila as idfila," + "precio," + "idArticulo," + "descuento," + "cantidad," + "tipo from oferOper" + " where enviado = 0";
                        Cursor cursorOferOper = dao.ejecutarConsultaSql(queryOferOper);
                        cursorToXml.AgregarTabla(cursorOferOper, "oferOper");

                        String xml = cursorToXml.GenerarXml();
                        xml = xml.substring(38, xml.length());

                        String imei = SharedPreferencesManager.getImei(MainActivity.this);

                        // Llamar a webservice
                        ServiceRegistry serviceRegistry = new ServiceRegistry(MainActivity.this, loginUsuario);
                        WebServiceHelper webServiceHelper = serviceRegistry.getWebServiceHelper();
                        webServiceHelper.addMethodParameter(servicioSubirDatosMobile, "usuario", loginUsuario);
                        webServiceHelper.addMethodParameter(servicioSubirDatosMobile, "clave", "");
                        webServiceHelper.addMethodParameter(servicioSubirDatosMobile, "imei", imei);
                        webServiceHelper.addMethodParameter(servicioSubirDatosMobile, "xmlDatos", xml);

                        SoapObject respuesta = webServiceHelper.executeService(servicioSubirDatosMobile);

                        String respuestaString = respuesta.getProperty(0).toString();

                        if (respuestaString.equals("anyType{}"))
                        {
                            dao.ejecutarSentenciaSql("update CabOper set enviado = 1 where enviado = 0");
                            dao.ejecutarSentenciaSql("update DetOper set enviado = 1 where enviado = 0");
                            dao.ejecutarSentenciaSql("update OferOper set enviado = 1 where enviado = 0");
                            FabricaMensaje.dialogoOk(MainActivity.this, "informacion", "Datos enviados", new DialogoAlertaNeutral()
                            {
                                @Override
                                public void Neutral()
                                {
                                }
                            }).show();
                        }
                        else
                        {
                            FabricaMensaje.dialogoOk(MainActivity.this, "Informe a Sistemas", respuestaString, new DialogoAlertaNeutral()
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

    private void btnVentas()
    {
        Button btnVentas = findViewById(R.id.btnVentas);

        btnVentas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this, RutaDeVenta.class);
                startActivity(intent);
            }
        });
    }

    private void enableButtons(boolean conectado)
    {
        Button btnVentas = findViewById(R.id.btnVentas);
        Button btnEnviar = findViewById(R.id.btnEnviar);

        btnVentas.setEnabled(conectado);
        btnEnviar.setEnabled(conectado);

        TextView tvVendedor = findViewById(R.id.tvVendedor);

        if (conectado)
        {
            startService(new Intent(MainActivity.this, GPSLocationService.class));
            tvVendedor.setText(String.format("Usuario %s", SharedPreferencesManager.getLoginUsuario(MainActivity.this)));
        }
        else
        {
            tvVendedor.setText(R.string.not_logged_in);
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
                Intent intent = new Intent(MainActivity.this, Recibir.class);
                startActivity(intent);
                return true;
            }
        });

        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
        {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem)
            {
                SharedPreferencesManager.setLogin(MainActivity.this, false);
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
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
}
