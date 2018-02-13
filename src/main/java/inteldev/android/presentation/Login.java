package inteldev.android.presentation;

import android.app.Activity;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import inteldev.android.R;
import inteldev.android.accesoadatos.Dao;
import inteldev.android.negocios.FabricaNegocios;
//import inteldev.android.servicios.ServicioGeoLocalizacion;


public class Login extends Activity
{
    TextView tvNumSerie;
    private boolean estadoOk;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvNumSerie = (TextView) findViewById(R.id.tvNumSerie);

        final Dao controladorDb = new Dao(this); //cambiar por un COntrolador

        final ControlLogin controlLogin = (ControlLogin) findViewById(R.id.CtlLogin);
        controlLogin.setOnLoginListener(new OnLogin()
        {
            String loginUsuario;

            @Override
            public void onLogin(String usuario, String contrasenia)
            {

                if (usuario.equals("inteldev".toUpperCase()) && contrasenia.equals("testing"))
                {
                    controlLogin.setMensaje("Login correcto!");
                    MainActivity.loginIdVendedor = "inteldev";
                    loginUsuario = "inteldev";
                    MainActivity.loginPass = "testing";
                    tvNumSerie.setText(FabricaNegocios.obtenerImei(getApplicationContext()));
                    estadoOk = true;
                }
                else
                {
                    if (!FabricaNegocios.leerEstadoParaUsoApp(getApplicationContext()))
                    {
                        FabricaMensaje.dialogoOk(Login.this, "No se puede continuar", "El GPS esta desactivado", new DialogoAlertaNeutral()
                        {
                            @Override
                            public void Neutral()
                            {
                            }
                        }).show();
                    }
                    else
                    {

                        Cursor cursor = controladorDb.ejecutarConsultaSql("select * from vendedores where usuario = '" + usuario + "' and pass = '" + contrasenia + "'");

                        if (cursor.moveToFirst())
                        {
                            MainActivity.loginIdVendedor = cursor.getString(0);
                            loginUsuario = usuario;
                            MainActivity.loginPass = contrasenia;
                            controlLogin.setMensaje("Login correcto!");
                            estadoOk = true;
                        }
                        else
                        {
                            loginUsuario = null;
                            controlLogin.setMensaje("Vuelva a intentarlo.");
                            estadoOk = false;
                        }
                    }
                }
                LoginObservable.getInstancia().setLoginUsuario(loginUsuario);
                LoginObservable.getInstancia().setLogin(estadoOk);
                if (estadoOk)
                {
                    finish();
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
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
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.login, menu);
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
}
