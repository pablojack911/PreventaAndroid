package inteldev.android.presentation;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import inteldev.android.R;

import static inteldev.android.CONSTANTES.CONFIG_CONVENIO_DESCUENTO;
import static inteldev.android.CONSTANTES.CONFIG_CONVENIO_DETALLE_TIPO;
import static inteldev.android.CONSTANTES.CONFIG_CONVENIO_PRECIO;
import static inteldev.android.CONSTANTES.CONFIG_CONVENIO_TIPO;


public class ConvenioMayorista extends AppCompatActivity
{
    EditText edtDescuento;
    EditText edtMotivo;
    EditText edtPrecioUnitario;
    EditText edtPrecioUnitarioConDescuento;
    Spinner spMotivo;
    Button btnAgregar;
    float precioUnitarioUnidad;
    float descuento;
    String motivo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convenio_mayorista);

        configuraBtnAgregar();
        configuraEdtDescuento();
        configuraEdtPrecioUnitarioConDescuento();
        configuraSpMotivo();
        edtMotivo = findViewById(R.id.edtMotivo);
        edtPrecioUnitario = findViewById(R.id.edtPrecioUnitario);

        if (savedInstanceState != null)
        {
            if (savedInstanceState.containsKey(CONFIG_CONVENIO_PRECIO))
            {
                precioUnitarioUnidad = savedInstanceState.getFloat(CONFIG_CONVENIO_PRECIO);
            }
            if (savedInstanceState.containsKey(CONFIG_CONVENIO_DESCUENTO))
            {
                descuento = savedInstanceState.getFloat(CONFIG_CONVENIO_DESCUENTO);
            }
            if (savedInstanceState.containsKey(CONFIG_CONVENIO_DETALLE_TIPO))
            {
                motivo = savedInstanceState.getString(CONFIG_CONVENIO_DETALLE_TIPO);
            }
        }
        else
        {
            if (getIntent().hasExtra(CONFIG_CONVENIO_PRECIO))
            {
                precioUnitarioUnidad = getIntent().getFloatExtra(CONFIG_CONVENIO_PRECIO, 0);
            }
            if (getIntent().hasExtra(CONFIG_CONVENIO_DESCUENTO))
            {
                descuento = getIntent().getFloatExtra(CONFIG_CONVENIO_DESCUENTO, 0);
            }
            if (getIntent().hasExtra(CONFIG_CONVENIO_DETALLE_TIPO))
            {
                motivo = getIntent().getStringExtra(CONFIG_CONVENIO_DETALLE_TIPO);
            }
        }

        edtPrecioUnitario.setText(String.format("%.2f", precioUnitarioUnidad));
        edtDescuento.setText(String.format("%.2f", descuento));
        edtPrecioUnitarioConDescuento.setText(String.valueOf(0));
        edtMotivo.setText(motivo);
    }

    private void configuraSpMotivo()
    {
        spMotivo = findViewById(R.id.spMotivo);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ConvenioMayorista.this, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        adapter.addAll(getResources().getStringArray(R.array.motivoconvenio));
        spMotivo.setAdapter(adapter);
    }

    private void configuraEdtPrecioUnitarioConDescuento()
    {
        edtPrecioUnitarioConDescuento = findViewById(R.id.edtPrecioUnitarioConDescuento);
        edtPrecioUnitarioConDescuento.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (getCurrentFocus() == edtPrecioUnitarioConDescuento)
                {
                    if (s.length() > 0 && !s.toString().equals("0"))
                    {
                        float precioConDto = Float.parseFloat(s.toString());
                        float dto = 100 - ((precioConDto * 100) / precioUnitarioUnidad);
                        edtDescuento.setText(String.format("%.2f", dto));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    private void configuraEdtDescuento()
    {
        edtDescuento = findViewById(R.id.edtDescuento);
        edtDescuento.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (getCurrentFocus() == edtDescuento)
                {
                    if (s.length() > 0 && !s.toString().equals("0"))
                    {
                        float dto = Float.parseFloat(s.toString());
                        float precioConDto = (precioUnitarioUnidad * dto) / 100;
                        edtPrecioUnitarioConDescuento.setText(String.format("%.2f", precioUnitarioUnidad - precioConDto));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        outState.putFloat(CONFIG_CONVENIO_PRECIO, precioUnitarioUnidad);
        outState.putFloat(CONFIG_CONVENIO_DESCUENTO, descuento);
        outState.putString(CONFIG_CONVENIO_DETALLE_TIPO, motivo);
        super.onSaveInstanceState(outState);
    }

    private void configuraBtnAgregar()
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
                    descuento = Float.valueOf(dto);
                    if (descuento > 100)
                    {
                        Toast.makeText(getApplicationContext(), "El descuento no puede superar el 100%", Toast.LENGTH_LONG).show();
                        edtDescuento.requestFocus();
                    }
                    else
                    {
                        if (descuento < 0)
                        {
                            Toast.makeText(getApplicationContext(), "El descuento no puede ser negativo", Toast.LENGTH_LONG).show();
                            edtDescuento.requestFocus();
                        }
                        else
                        {
                            String is = spMotivo.getSelectedItem().toString();
                            Intent resultData = new Intent();
                            resultData.putExtra(CONFIG_CONVENIO_DESCUENTO, descuento);
                            resultData.putExtra(CONFIG_CONVENIO_TIPO, is);
                            String resultString = edtMotivo.getText().toString().replaceAll("[^\\x00-\\x7F]", "");
                            resultData.putExtra(CONFIG_CONVENIO_DETALLE_TIPO, resultString);
                            setResult(RESULT_OK, resultData);
                            finish();
                        }
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
