package inteldev.android.presentation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.ArrayList;

import inteldev.android.R;
import inteldev.android.modelo.Articulo;
import inteldev.android.modelo.DetBonif;
import inteldev.android.modelo.OferEsp;
import inteldev.android.modelo.Ofer_Det;
import inteldev.android.negocios.ControladorArticulo;
import inteldev.android.negocios.ControladorConvenio;
import inteldev.android.negocios.ControladorOferta;
import inteldev.android.negocios.ControladorPrecio;
import inteldev.android.negocios.ControladorStock;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.presentation.vistaModelo.OferOperOfertas;

import static inteldev.android.CONSTANTES.CLAVE_UNICA_CABOPER;
import static inteldev.android.CONSTANTES.CODIGO_OFERTA;
import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;
import static inteldev.android.CONSTANTES.INTENT_DETALLE_OFERTA_NO_CONFIRMADA;
import static inteldev.android.CONSTANTES.OFERTA_ESPECIAL;
import static inteldev.android.CONSTANTES.TOTAL_OFERTA;
//import inteldev.android.servicios.ServicioGeoLocalizacion;


public class Oferta extends FragmentActivity
{

    Context context;

    String claveUnicaCabOper;
    Boolean graboDetOper = false;

    ControladorOferta controladorOferta;
    ControladorArticulo controladorArticulo;
    ControladorPrecio controladorPrecio;
    ControladorConvenio controladorConvenio;
    ControladorStock controladorStock;

    TabHost.TabSpec tabCargaOferta;
    TabHost.TabSpec tabDetalleOferta;

    String codigoOferta;
    String idClienteSeleccionado;

    Spinner SpTipoParteOferta;
    Spinner spArticulo;

    EditText edtCodigoArticuloOferta;
    EditText edtArticuloOferta;
    EditText edtUnitarioOferta;
    EditText edtCantidadOferta;
    EditText edtDescuentoOferta;
    EditText edtFinalOferta;

    EditText edtTotalOferta;

    OferEsp oferEsp;

    ArrayList<Ofer_Det> ofer_detsBase;
    ArrayList<Ofer_Det> ofer_detsBonificada;

    ArrayList<OferOperOfertas> oferOpers;

    Button btnOkOferta;
    Button btnContinuar;
//    Button btnCerrar;

    Spinner spTipoParteOferta;

    ListView lvDetalleOferta;

    EditText edtpru;

    Dialog dialogoCantidad;

    int rowIdSeleccionado;
    private String loginUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oferta);

        Intent intent = getIntent();
        if (intent.hasExtra(CODIGO_OFERTA))
        {
            codigoOferta = intent.getStringExtra(CODIGO_OFERTA);
        }
        if (intent.hasExtra(ID_CLIENTE_SELECCIONADO))
        {
            idClienteSeleccionado = intent.getStringExtra(ID_CLIENTE_SELECCIONADO);
        }
        if (intent.hasExtra(CLAVE_UNICA_CABOPER))
        {
            claveUnicaCabOper = intent.getStringExtra(CLAVE_UNICA_CABOPER);
        }
        loginUsuario = LoginObservable.getInstancia().getLoginUsuario();
//        if (intent.hasExtra(USUARIO_KEY))
//        {
//            loginUsuario = intent.getStringExtra(USUARIO_KEY);
//        }

        controladorOferta = FabricaNegocios.obtenerControladorOferta(getApplicationContext());
        controladorPrecio = FabricaNegocios.obtenerControladorPrecio(getApplicationContext());
        controladorConvenio = FabricaNegocios.obtenerControladorConvenio(getApplicationContext(), idClienteSeleccionado);
        controladorStock = FabricaNegocios.obtenerControladorStock(getApplicationContext());

        oferEsp = controladorOferta.obtenerOfertaPorCodigo(this.codigoOferta);

        ofer_detsBase = controladorOferta.obtenerArticulosOferta(codigoOferta, "BASE");
        ofer_detsBonificada = controladorOferta.obtenerArticulosOferta(codigoOferta, "BONIFICADA");

        edtCodigoArticuloOferta = (EditText) findViewById(R.id.edtCodigoArticuloOferta);
        edtArticuloOferta = (EditText) findViewById(R.id.edtArticuloOferta);

        edtUnitarioOferta = (EditText) findViewById(R.id.edtUnitarioOferta);
        edtCantidadOferta = (EditText) findViewById(R.id.edtCantidadOferta);
        edtDescuentoOferta = (EditText) findViewById(R.id.edtDescuentoOferta);
        edtFinalOferta = (EditText) findViewById(R.id.edtFinalOferta);

        edtTotalOferta = (EditText) findViewById(R.id.edtTotalOferta);

        btnOkOferta = (Button) findViewById(R.id.btnOkOferta);
        btnContinuar = (Button) findViewById(R.id.btnContinuarOferta);
        spTipoParteOferta = (Spinner) findViewById(R.id.spTipoParteOferta);

        oferOpers = new ArrayList<OferOperOfertas>();

        spTipoParteOferta(this);
        spArticulo(this, ofer_detsBase);
        edtArticuloOferta(this);

        edtCantidadOferta(this);
        btnMasOferta(this);
        btnMenosOferta(this);

        btnOkOferta(this);
        btnContinuarOferta(this);

        edtArticuloOferta.requestFocus();
        context = this;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == INTENT_DETALLE_OFERTA_NO_CONFIRMADA)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                oferOpers = data.getParcelableArrayListExtra("oferOpers");
                calcularTotal(oferOpers);
            }
            if (resultCode == Activity.RESULT_CANCELED)
            {
                Oferta.this.finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK && oferOpers.size() > 0)
        {

            FabricaMensaje.dialogoAlertaSiNo(context, "¡Atención no cerro la oferta!", "¿Seguro que desea salir?", new DialogoAlertaSiNo()
            {
                @Override
                public void Positivo()
                {
                    Oferta.this.finish();
                }

                @Override
                public void Negativo()
                {

                }
            }).show();

            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void calcularTotal(ArrayList<OferOperOfertas> oferOpers)
    {
        float total = 0f;

        for (OferOperOfertas oferOper : oferOpers)
        {
//            OferOper oferOper = (OferOper) oferOper1;

            float precio = oferOper.precio;
            int cantidad = oferOper.cantidad;
            float descuento = oferOper.descuento;

            float importeDescuento = precio * (descuento / 100);
            total = total + ((precio - importeDescuento) * cantidad);
        }
//        EditText edtTotalOferta = (EditText) findViewById(R.id.edtTotalOferta);
        edtTotalOferta.setText(String.valueOf(total));
    }

    private void btnOkOferta(final Oferta oferta)
    {

        btnOkOferta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (edtCantidadOferta.getText().toString().equals("0") || edtCantidadOferta.getText().toString().isEmpty())
                {
                    FabricaMensaje.dialogoOk(context, "Faltan Datos", "No indico la cantidad", new DialogoAlertaNeutral()
                    {
                        @Override
                        public void Neutral()
                        {

                        }
                    }).show();
                }
                else
                {

                    if (!controladorStock.hayStock(edtCodigoArticuloOferta.getText().toString().trim()))
                    {

                        FabricaMensaje.dialogoOk(context, "Información", "No hay stock", new DialogoAlertaNeutral()
                        {
                            @Override
                            public void Neutral()
                            {

                            }
                        }).show();
                    }
                    else
                    {
                        OferOperOfertas oferOper = new OferOperOfertas();
                        oferOper.cantidad = Integer.valueOf(edtCantidadOferta.getText().toString());
                        oferOper.descuento = Float.valueOf(edtDescuentoOferta.getText().toString());
                        oferOper.idArticulo = edtCodigoArticuloOferta.getText().toString().trim();
                        oferOper.precio = Float.valueOf(edtUnitarioOferta.getText().toString());
                        oferOper.tipo = spTipoParteOferta.getSelectedItem().toString();
                        oferOper.rowIdOferOper = rowIdSeleccionado;

                        oferOpers.add(oferOper);

                        edtCantidadOferta.setText("1");
                        edtFinalOferta.setText("0");

                        edtArticuloOferta.requestFocus();
                        calcularTotal(oferOpers);
                    }
                }
            }
        });
    }

    private void btnContinuarOferta(final Oferta oferta)
    {
        btnContinuar = (Button) findViewById(R.id.btnContinuarOferta);
        btnContinuar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intentDetalle_Oferta_NoConfirmada = new Intent(getApplicationContext(), Detalle_Oferta_NoConfirmada.class);
                intentDetalle_Oferta_NoConfirmada.putExtra("oferOpers", oferOpers);
                intentDetalle_Oferta_NoConfirmada.putExtra("ofer_detsBase", ofer_detsBase);
                intentDetalle_Oferta_NoConfirmada.putExtra("ofer_detsBonificada", ofer_detsBonificada);
                intentDetalle_Oferta_NoConfirmada.putExtra(CLAVE_UNICA_CABOPER, claveUnicaCabOper);
                intentDetalle_Oferta_NoConfirmada.putExtra(ID_CLIENTE_SELECCIONADO, idClienteSeleccionado);
                intentDetalle_Oferta_NoConfirmada.putExtra(CODIGO_OFERTA, codigoOferta);
                intentDetalle_Oferta_NoConfirmada.putExtra(OFERTA_ESPECIAL, oferEsp);
                String total = edtTotalOferta.getText().toString();
                if (total.equals(""))
                {
                    total = "0";
                }
                intentDetalle_Oferta_NoConfirmada.putExtra(TOTAL_OFERTA, Float.valueOf(total));
                startActivityForResult(intentDetalle_Oferta_NoConfirmada, INTENT_DETALLE_OFERTA_NO_CONFIRMADA);
            }
        });
    }

    private void calcularPrecioFinal()
    {

        if (!edtCantidadOferta.getText().toString().isEmpty())
        {
            if (!edtUnitarioOferta.getText().toString().isEmpty())
            {

                BigDecimal precioUnitario = new BigDecimal(edtUnitarioOferta.getText().toString());
                BigDecimal cantidad = new BigDecimal(edtCantidadOferta.getText().toString());

                BigDecimal descuento = new BigDecimal(edtDescuentoOferta.getText().toString());

                BigDecimal importeDescuento = new BigDecimal(0.00);

                importeDescuento = precioUnitario.multiply(descuento);

                importeDescuento = importeDescuento.divide(new BigDecimal(100.00));

                BigDecimal precioFinal = new BigDecimal(0.00);

                precioFinal = precioUnitario.subtract(importeDescuento);
                precioFinal = precioFinal.multiply(cantidad);

//                double precioFinal = (precioUnitario - importeDescuento) * cantidad;

                edtFinalOferta.setText(String.valueOf(precioFinal));
            }
        }
    }

    private void edtCantidadOferta(Context context)
    {
        edtCantidadOferta.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3)
            {
                calcularPrecioFinal();
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    private void btnMasOferta(Context context)
    {
        Button btnMasOferta = (Button) findViewById(R.id.btnMasOferta);

        btnMasOferta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String valor = edtCantidadOferta.getText().toString();
                int numero = Integer.parseInt(valor);
                numero = numero + 1;
                edtCantidadOferta.setText(Integer.toString(numero));
                edtCantidadOferta.requestFocus();
                edtCantidadOferta.selectAll();
            }
        });

    }

    private void btnMenosOferta(Context context)
    {
        Button btnMenosOferta = (Button) findViewById(R.id.btnMenosOferta);

        btnMenosOferta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String valor = edtCantidadOferta.getText().toString();
                int numero = 0;
                if (!valor.trim().equals(""))
                {
                    numero = Integer.parseInt(valor);
                    if (numero > 0)
                    {
                        numero = numero + -1;
                    }
                }
                edtCantidadOferta.setText(Integer.toString(numero));
                edtCantidadOferta.requestFocus();
                edtCantidadOferta.selectAll();

            }
        });

    }

    private void edtArticuloOferta(Oferta oferta)
    {

        final EditText edtArticuloOferta = (EditText) findViewById(R.id.edtArticuloOferta);
        final Spinner spArticuloDeLaOferta = (Spinner) findViewById(R.id.spArticuloDeLaOferta);

        edtArticuloOferta.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

                ArrayList<Ofer_Det> ofer_detSeleccionado;
                String tp = spTipoParteOferta.getSelectedItem().toString();
                if (spTipoParteOferta.getSelectedItem().toString().equals("BASE"))
                {
                    ofer_detSeleccionado = ofer_detsBase;
                }
                else
                {
                    ofer_detSeleccionado = ofer_detsBonificada;
                }

                int indice = 0;
                for (Ofer_Det ofer_det : ofer_detSeleccionado)
                {
                    if (ofer_det.Articulo.trim().equals(edtArticuloOferta.getText().toString()))
                    {
                        spArticuloDeLaOferta.setSelection(indice);
                        edtArticuloOferta.setText("");
                        edtCantidadOferta.requestFocus();
                        edtCantidadOferta.selectAll();
                        break;
                    }
                    indice = indice + 1;
                }
                //    return false;
            }


            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });
    }

    private void spTipoParteOferta(final Context context)
    {

        spTipoParteOferta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (l == 0)
                {
                    spArticulo(context, ofer_detsBase);
                }
                else
                {
                    spArticulo(context, ofer_detsBonificada);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    private void spArticulo(final Context context, final ArrayList<Ofer_Det> ofer_dets)
    {

        Spinner spArticuloDeLaOferta = (Spinner) findViewById(R.id.spArticuloDeLaOferta);

        spArticuloDeLaOferta.setAdapter(new AdaptadorListaGenerico(context, android.R.layout.simple_spinner_item, ofer_dets)
        {
            @Override
            public void onEntrada(Object entrada, View view)
            {

                TextView tvArticuloDeLaOferta = (TextView) view.findViewById(android.R.id.text1);

                String articuloDeLaOferta = ((Ofer_Det) entrada).getArticulo();

                controladorArticulo = FabricaNegocios.obtenerControladorArticulo(context);

                Articulo articulo = controladorArticulo.buscarArticuloPorCodigo(articuloDeLaOferta.trim());

                if (articulo != null)
                {
                    tvArticuloDeLaOferta.setText(articulo.nombre);
                }
            }
        });

        spArticuloDeLaOferta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (ofer_dets.size() > 0)
                {
                    edtCodigoArticuloOferta = (EditText) findViewById(R.id.edtCodigoArticuloOferta);
                    edtCodigoArticuloOferta.setText(ofer_dets.get(i).getArticulo());

                    Float unitario = controladorPrecio.obtener(context, idClienteSeleccionado, ofer_dets.get(i).getArticulo().trim());

                    edtUnitarioOferta = (EditText) findViewById(R.id.edtUnitarioOferta);
                    edtUnitarioOferta.setText(unitario.toString());

                    EditText edtDescuentoOferta = (EditText) findViewById(R.id.edtDescuentoOferta);

                    if (spTipoParteOferta.getSelectedItem().toString().equals("BONIFICADA"))
                    {
                        edtDescuentoOferta.setText("100");
                    }
                    else
                    {
                        float descuento = ofer_dets.get(i).getDescuento1();

                        if (oferEsp.TomaConv == 1)
                        {
                            DetBonif detBonif = controladorConvenio.obtenerPorcentaje(ofer_dets.get(i).getArticulo().trim(), false);
                            float descuentoConvenio = Float.valueOf(detBonif.porcentaje);

                            float porcentaje = 100f;
                            porcentaje = porcentaje - (porcentaje * descuentoConvenio / 100);
                            descuento = porcentaje - (porcentaje * ofer_dets.get(i).getDescuento1() / 100);
                            descuento = 100 - descuento;

                        }
                        edtDescuentoOferta.setText(String.valueOf(descuento));
                    }

                    edtCantidadOferta.setText("1");
                    edtCantidadOferta.requestFocus();
                    edtCantidadOferta.selectAll();

                    rowIdSeleccionado = ofer_dets.get(i).rowid;

                    calcularPrecioFinal();

                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }
}
