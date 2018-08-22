package inteldev.android.presentation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.serialization.SoapObject;

import inteldev.android.R;
import inteldev.android.accesoadatos.Dao;
import inteldev.android.negocios.EstadoWebService;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.ServiceRegistry;
import inteldev.android.negocios.SharedPreferencesManager;
import inteldev.android.negocios.WebServiceHelper;

import static inteldev.android.CONSTANTES.PERMISSIONS;
import static inteldev.android.CONSTANTES.REQUEST_CHECK_SETTINGS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity
{
    // UI references.
    private EditText mUsuarioView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    //    private UserLoginTask mAuthTask;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        solicitarPermisos();

        // Set up the login form.
        mUsuarioView = findViewById(R.id.usuario);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent)
            {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL)
                {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLogin = findViewById(R.id.btnIniciarSesion);
        mLogin.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                showProgress(true);
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void goToMain()
    {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void solicitarPermisos()
    {
        if (!Utiles.hasPermissions(this, PERMISSIONS))
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(PERMISSIONS, REQUEST_CHECK_SETTINGS);
            }
        }
        if (SharedPreferencesManager.getLoginUsuario(LoginActivity.this).length() > 0 && SharedPreferencesManager.isLogin(LoginActivity.this))
        {
            goToMain();
            finish();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == REQUEST_CHECK_SETTINGS)
        {
            if (grantResults.length > 0)
            {
                for (int res : grantResults)
                {
                    if (res != PackageManager.PERMISSION_GRANTED)
                    {
                        finish();
                    }
                }
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin()
    {
        // Reset errors.
        mUsuarioView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String usuario = mUsuarioView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password))
        {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(usuario))
        {
            mUsuarioView.setError(getString(R.string.error_field_required));
            focusView = mUsuarioView;
            cancel = true;
        }

        if (cancel)
        {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else if (!FabricaNegocios.leerEstadoParaUsoApp(getApplicationContext()))
        {
            FabricaMensaje.dialogoOk(LoginActivity.this, "No se puede continuar", "El GPS esta desactivado", new DialogoAlertaNeutral()
            {
                @Override
                public void Neutral()
                {
                }
            }).show();
        }
        else
        {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (!FabricaNegocios.obtenerEstadoConexion(connectivityManager).Conectado())
            {
                FabricaMensaje.dialogoOk(LoginActivity.this, "Sin Conexion", "No dispone de conexion 3G o Wifi", new DialogoAlertaNeutral()
                {
                    @Override
                    public void Neutral()
                    {
                    }
                }).show();
            }
            else
            {
                Dao dao = new Dao(LoginActivity.this);
                Cursor cursor = dao.ejecutarConsultaSql("select * from vendedores where usuario = '" + usuario + "' and pass = '" + password + "'");
                if (cursor.moveToNext())
                {
                    SharedPreferencesManager.setLogin(LoginActivity.this, true);
                    SharedPreferencesManager.setLoginUsuario(LoginActivity.this, usuario);
                    goToMain();
                    finish();
                }
                else
                {
                    EstadoWebService.Estado estadoWebService;
                    String servicioLogin;
                    EstadoWebService oEstadoWebService = FabricaNegocios.obtenerEstadoWebService(LoginActivity.this, usuario);
                    estadoWebService = oEstadoWebService.verificarConexionServicio("hola", "holaRemoto");
                    if (estadoWebService == EstadoWebService.Estado.NoDisponible)
                    {
                        FabricaMensaje.dialogoOk(LoginActivity.this, "Avise a sistemas", "Servicio Web no disponible", new DialogoAlertaNeutral()
                        {
                            @Override
                            public void Neutral()
                            {
                            }
                        }).show();
                    }
                    else
                    {
                        if (estadoWebService == EstadoWebService.Estado.ConexionLocal)
                        {
                            servicioLogin = "IniciarSesion";
                        }
                        else
                        {
                            servicioLogin = "IniciarSesionRemoto";
                        }

                        ServiceRegistry serviceRegistry = new ServiceRegistry(LoginActivity.this, usuario);
                        WebServiceHelper webServiceHelper = serviceRegistry.getWebServiceHelper();
                        webServiceHelper.addMethodParameter(servicioLogin, "usuario", usuario);
                        webServiceHelper.addMethodParameter(servicioLogin, "clave", password);
                        webServiceHelper.addMethodParameter(servicioLogin, "imei", SharedPreferencesManager.getImei(LoginActivity.this));

                        SoapObject respuesta = webServiceHelper.executeService(servicioLogin);

                        String respuestaString = respuesta.getProperty(0).toString();
                        if (respuestaString.equals("anyType{}"))
                        {
                            SharedPreferencesManager.setLogin(LoginActivity.this, true);
                            SharedPreferencesManager.setLoginUsuario(LoginActivity.this, usuario);
                            goToMain();
                            finish();
                        }
                        else
                        {
                            mPasswordView.setError(respuestaString);
                            mPasswordView.requestFocus();
                        }
                        //                    mAuthTask = new UserLoginTask(usuario, password, servicioLogin);
                        //                    mAuthTask.execute((Void) null);
                    }
                }
            }
        }
        showProgress(false);
    }


    private boolean isPasswordValid(String password)
    {
        return password.length() > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show)
    {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0).setListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }
    //
    //    /**
    //     * Represents an asynchronous login/registration task used to authenticate
    //     * the user.
    //     */
    //    public class UserLoginTask extends AsyncTask<Void, Void, Boolean>
    //    {
    //
    //        private final String mUsuario;
    //        private final String mPassword;
    //        private final String mServicioLogin;
    //
    //        UserLoginTask(String usuario, String password, String servicioLogin)
    //        {
    //            mUsuario = usuario;
    //            mPassword = password;
    //            mServicioLogin = servicioLogin;
    //        }
    //
    //        @Override
    //        protected Boolean doInBackground(Void... params)
    //        {
    ////            Dao controladorDb = new Dao(LoginActivity.this);
    ////            Cursor cursor = controladorDb.ejecutarConsultaSql("select * from vendedores where usuario = '" + mUsuario + "' and pass = '" + mPassword + "'");
    ////            return cursor.moveToFirst();
    //
    //
    //
    //        }
    //
    //        @Override
    //        protected void onPostExecute(final Boolean success)
    //        {
    //            mAuthTask = null;
    //            showProgress(false);
    //
    //            if (success)
    //            {
    //                SharedPreferencesManager.setLogin(LoginActivity.this, true);
    //                SharedPreferencesManager.setLoginUsuario(LoginActivity.this, mUsuario);
    //                goToMain();
    //                finish();
    //            }
    //            else
    //            {
    //                mPasswordView.setError(getString(R.string.error_incorrect_password));
    //                mPasswordView.requestFocus();
    //            }
    //        }
    //
    //        @Override
    //        protected void onCancelled()
    //        {
    //            mAuthTask = null;
    //            showProgress(false);
    //        }
    //    }
}

