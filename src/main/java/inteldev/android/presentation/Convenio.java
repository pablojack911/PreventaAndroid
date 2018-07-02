package inteldev.android.presentation;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import inteldev.android.R;


public class Convenio extends AppCompatActivity
{

    EditText edtDescuento;

    EditText edtMotivo;

    Spinner spMotivo;

    Button btnAgregar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convenio);

        edtDescuento = findViewById(R.id.edtDescuento);
        edtMotivo = findViewById(R.id.edtMotivo);
        spMotivo = findViewById(R.id.spMotivo);

        btnAgregar();

    }

    private void btnAgregar()
    {
        btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String dto = edtDescuento.getText().toString();
                if (dto.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Debe indicar un descuento", Toast.LENGTH_LONG).show();
                    edtDescuento.requestFocus();
                }
                else
                {
                    float descuento = Float.valueOf(dto);
                    if (descuento > 100)
                    {
                        Toast.makeText(getApplicationContext(), "El descuento no puede superar el 100%", Toast.LENGTH_LONG).show();
                        edtDescuento.requestFocus();
                    }
                    else
                    {
                        String is = spMotivo.getSelectedItem().toString();
                        Intent resultData = new Intent();
                        resultData.putExtra("descuento", descuento);
                        resultData.putExtra("tipo", is);
                        String resultString = edtMotivo.getText().toString().replaceAll("[^\\x00-\\x7F]", "");
                        resultData.putExtra("detalletipo", resultString);
                        setResult(RESULT_OK, resultData);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }
}
