package inteldev.android.presentation;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.app.Activity;

import java.lang.Object;

import inteldev.android.R;


/**
 * Created by Operador on 25/04/14.
 */
public class ControlLogin extends LinearLayout {

    private OnLogin listener;

    EditText edtUsuario;
    EditText edtContrasenia;
    Button btnLogin;
    TextView tvResultado;


    public void setOnLoginListener(OnLogin onLoginListener) {
        listener = onLoginListener;
    }

    public ControlLogin(Context context) {
        super(context);
        inicializar();
    }

    public ControlLogin(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inicializar();
    }

    private void inicializar() {
        String infService = Context.LAYOUT_INFLATER_SERVICE;

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(infService);
        layoutInflater.inflate(R.layout.control_login, this, true);

        edtUsuario = (EditText) findViewById(R.id.edtUsuario);
        edtContrasenia = (EditText) findViewById(R.id.edtContrasenia);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        tvResultado = (TextView) findViewById(R.id.tvResultado);

       /* edtUsuario.requestFocus();
        InputMethodManager imm = (InputMethodManager) layoutInflater.getContext().getSystemService(
               Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edtUsuario, 0);*/


        asignarEventos();
    }

    private void asignarEventos() {
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onLogin(edtUsuario.getText().toString(),
                        edtContrasenia.getText().toString());
            }
        });
    }

    public void setMensaje(String mensaje) {
        TextView tvResultado = (TextView) findViewById(R.id.tvResultado);
        tvResultado.setText(mensaje);

    }
}
