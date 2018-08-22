package inteldev.android.presentation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.serialization.SoapObject;

import java.util.Objects;

import inteldev.android.R;
import inteldev.android.accesoadatos.IDao;
import inteldev.android.negocios.DescargaClient;
import inteldev.android.negocios.EstadoWebService;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.IncorporaXml;
import inteldev.android.negocios.ServiceRegistry;
import inteldev.android.negocios.WebServiceHelper;

import static inteldev.android.CONSTANTES.USUARIO_KEY;


public class Recibir extends Activity
{

    public static ProgressDialog dialogoProgresoRecibir;
    public boolean botonClicked;
    TextView tvEstado;
    Button btnRecibir;
    String loginUsuario;
    private Context view;
    private ConnectivityManager conMan;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recibir);
        if (getIntent().hasExtra(USUARIO_KEY))
        {
            loginUsuario = getIntent().getStringExtra(USUARIO_KEY);
        }
        botonClicked = false;
        btnRecibir();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();
    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu)
    //    {
    //        // Inflate the menu; this adds items to the action bar if it is present.
    //        getMenuInflater().inflate(R.menu.recibir, menu);
    //        return true;
    //    }
    //
    //    @Override
    //    public boolean onOptionsItemSelected(MenuItem item)
    //    {
    //        // Handle action bar item clicks here. The action bar will
    //        // automatically handle clicks on the Home/Up button, so long
    //        // as you specify a parent activity in AndroidManifest.xml.
    //        int id = item.getItemId();
    //        if (id == R.id.action_settings)
    //        {
    //            return true;
    //        }
    //        return super.onOptionsItemSelected(item);
    //    }

    public void btnRecibir()
    {
        btnRecibir = (Button) findViewById(R.id.btnRecibir);

        conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        btnRecibir.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (!botonClicked)
                {
                    botonClicked = true;
                    if (!FabricaNegocios.obtenerEstadoConexion(conMan).Conectado())
                    {
                        FabricaMensaje.dialogoOk(Recibir.this, "sin conexion".toUpperCase(), "Active 4G o WIFI", new DialogoAlertaNeutral()
                        {
                            @Override
                            public void Neutral()
                            {
                                botonClicked = false;
                            }
                        }).show();
                    }
                    else
                    {

                        dialogoProgresoRecibir = ProgressDialog.show(Recibir.this, "", "Recibiendo. Por favor espere...", true);

                        final DescargaClient clienteDescarga = new DescargaClient(Recibir.this, loginUsuario);

                        if (clienteDescarga.estadoWebService == EstadoWebService.Estado.NoDisponible)
                        {
                            FabricaMensaje.dialogoOk(Recibir.this, "sistemas".toUpperCase(), "Servicio Web no disponible.", new DialogoAlertaNeutral()
                            {
                                @Override
                                public void Neutral()
                                {

                                }
                            }).show();
                            dialogoProgresoRecibir.cancel();
                            return;
                        }
                        clienteDescarga.setDescargaListener(new DescargaListener()
                        {
                            @SuppressLint("StaticFieldLeak")
                            @Override
                            public void Notificar(final boolean estado)
                            {
                                new AsyncTask<Void, Void, Boolean>()
                                {

                                    @Override
                                    protected Boolean doInBackground(Void... voids)
                                    {
                                        if (estado)
                                        {
                                            IDao dao = FabricaNegocios.obtenerDao(getApplicationContext());
                                            long res = dao.delete("PosicionesGPS", "enviado=?", new String[]{"1"});
                                            Log.d("Borrando", "PosGPS -> " + String.valueOf(res) + " filas.");
                                            res = dao.delete("CabOper", "enviado = ?", new String[]{"1"});
                                            Log.d("Borrando", "CabOper -> " + String.valueOf(res) + " filas.");
                                            res = dao.delete("DetOper", "enviado = ?", new String[]{"1"});
                                            Log.d("Borrando", "DetOper -> " + String.valueOf(res) + " filas.");
                                            res = dao.delete("OferOper", "enviado = ?", new String[]{"1"});
                                            Log.d("Borrando", "OferOper -> " + String.valueOf(res));

                                            IncorporaXml incorporaXml = new IncorporaXml();
                                            incorporaXml.incorporar(getApplicationContext());
                                        }
                                        else
                                        {
                                            return false;
                                        }
                                        return true;
                                    }

                                    @Override
                                    protected void onPostExecute(Boolean procesoOk)
                                    {
                                        if (procesoOk)
                                        {
                                            ServiceRegistry serviceRegistry = new ServiceRegistry(getApplicationContext(), loginUsuario);
                                            WebServiceHelper webServiceHelper = serviceRegistry.getWebServiceHelper();
                                            SoapObject respuesta;
                                            if (clienteDescarga.estadoWebService == EstadoWebService.Estado.ConexionLocal)
                                            {
                                                respuesta = webServiceHelper.executeService("actualizaFechaDownload");
                                            }
                                            else
                                            {
                                                respuesta = webServiceHelper.executeService("actualizaFechaDownloadRemoto");
                                            }
                                            Log.d("Recibir", "actualizaFechaDownload - " + respuesta.toString());
                                            FabricaMensaje.dialogoOk(Recibir.this, "informacion".toUpperCase(), "Datos recibidos.", new DialogoAlertaNeutral()
                                            {
                                                @Override
                                                public void Neutral()
                                                {
                                                    botonClicked = false;
                                                    Recibir.this.cerrar();
                                                }
                                            }).show();
                                        }
                                        else
                                        {
                                            FabricaMensaje.dialogoOk(Recibir.this, "error".toUpperCase(), "Reintente recibir", new DialogoAlertaNeutral()
                                            {
                                                @Override
                                                public void Neutral()
                                                {
                                                    botonClicked = false;
                                                }
                                            }).show();
                                        }
                                        dialogoProgresoRecibir.cancel();
                                    }
                                }.execute();
                            }
                        });
                    }
                }
                else
                {
                    dialogoProgresoRecibir.cancel();
                    FabricaMensaje.dialogoOk(Recibir.this, "recibiendo".toUpperCase(), "Aguarde un momento...", new DialogoAlertaNeutral()
                    {
                        @Override
                        public void Neutral()
                        {

                        }
                    }).show();
                }
            }
        });
    }

    private void cerrar()
    {
        this.finish();
    }

}
