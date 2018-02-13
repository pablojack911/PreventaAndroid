package inteldev.android.presentation;

/**
 * Created by Operador on 28/04/14.
 */
public class LoginObservable
{

    private static LoginObservable instancia;
    private boolean login;
    private String loginUsuario;
    private OnLoginListener onLoginListener;

    protected LoginObservable()
    {

    }

    public static LoginObservable getInstancia()
    {
        if (instancia == null)
        {
            instancia = new LoginObservable();
        }

        return instancia;
    }

    public void setOnLoginListener(OnLoginListener onLoginListener)
    {
        this.onLoginListener = onLoginListener;
    }

    public boolean isLogin()
    {
        return login;
    }

    public LoginObservable setLogin(boolean login)
    {
        this.login = login;
        this.onLoginListener.OnLoginChange(login);
        return getInstancia();
    }

    public String getLoginUsuario()
    {
        return loginUsuario;
    }

    public LoginObservable setLoginUsuario(String loginUsuario)
    {
        this.loginUsuario = loginUsuario;
        return getInstancia();
    }
}
