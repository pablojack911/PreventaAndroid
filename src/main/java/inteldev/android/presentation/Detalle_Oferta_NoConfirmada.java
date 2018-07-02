package inteldev.android.presentation;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import inteldev.android.R;
import inteldev.android.accesoadatos.IDao;
import inteldev.android.modelo.Articulo;
import inteldev.android.modelo.CabOper;
import inteldev.android.modelo.DetOper;
import inteldev.android.modelo.OferEsp;
import inteldev.android.modelo.OferOper;
import inteldev.android.modelo.Ofer_Det;
import inteldev.android.negocios.ControladorArticulo;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.Fecha;
import inteldev.android.negocios.Mapeador;
import inteldev.android.negocios.SharedPreferencesManager;
import inteldev.android.presentation.vistaModelo.IListenerDialogoCantidad;
import inteldev.android.presentation.vistaModelo.OferOperOfertas;

import static inteldev.android.CONSTANTES.CLAVE_UNICA_CABOPER;
import static inteldev.android.CONSTANTES.CODIGO_OFERTA;
import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;
import static inteldev.android.CONSTANTES.OFERTA_ESPECIAL;

/**
 * Created by Pocho on 09/03/2017.
 */

public class Detalle_Oferta_NoConfirmada extends FragmentActivity
{
    Context context;
    ControladorArticulo controladorArticulo;
    ListView lvDetalleOfertaPedidos;
    Button btnVolver;
    Button btnConfirmarOferta;
    ArrayList<OferOperOfertas> oferOpers;
    ArrayList<Ofer_Det> ofer_detsBase;
    ArrayList<Ofer_Det> ofer_detsBonificada;
    OferEsp oferEsp;
    String claveUnicaCabOper;
    String idClienteSeleccionado;
    String codigoOferta;
    float totalOferta;
    Dialog dialogoCantidad;
    EditText edtTotalOfertaNoConfirmada;
    private String loginUsuario;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_oferta_no_confirmada);
        bindUI();

        context = this;

        loginUsuario = SharedPreferencesManager.getLoginUsuario(Detalle_Oferta_NoConfirmada.this);

        controladorArticulo = FabricaNegocios.obtenerControladorArticulo(getApplicationContext());

        Intent i = getIntent();
        oferOpers = i.getParcelableArrayListExtra("oferOpers");
        ofer_detsBase = i.getParcelableArrayListExtra("ofer_detsBase");
        ofer_detsBonificada = i.getParcelableArrayListExtra("ofer_detsBonificada");
        claveUnicaCabOper = i.getStringExtra(CLAVE_UNICA_CABOPER);
        idClienteSeleccionado = i.getStringExtra(ID_CLIENTE_SELECCIONADO);
        codigoOferta = i.getStringExtra(CODIGO_OFERTA);
        oferEsp = i.getParcelableExtra(OFERTA_ESPECIAL);
//        totalOferta = i.getFloatExtra(TOTAL_OFERTA, 0);
        totalOferta = calcularTotal();
        cargaDetalleOferta(context);


        btnVolver.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra("oferOpers", oferOpers);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        FabricaMensaje.setListenerDialogoCantidad(new IListenerDialogoCantidad()
        {
            @Override
            public void Positivo(Integer valor)
            {

                String mensaje = validaOferta(context, valor);
                if (!mensaje.isEmpty())
                {
                    FabricaMensaje.dialogoOk(context, "No se puede cerrar la oferta ", mensaje, new DialogoAlertaNeutral()
                    {
                        @Override
                        public void Neutral()
                        {

                        }
                    }).show();
                }
                else
                {
                    cerrarOferta(context, valor);
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
        dialogoCantidad = FabricaMensaje.dialogoCantidad(this);

        edtTotalOfertaNoConfirmada.setText(String.format("%.2f", totalOferta));

        btnConfirmarOferta.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialogoCantidad.show();
            }
        });
    }

    private void bindUI()
    {
        edtTotalOfertaNoConfirmada = (EditText) findViewById(R.id.edtTotalOfertaNoConfirmada);
        lvDetalleOfertaPedidos = (ListView) findViewById(R.id.lvDetalle_Oferta_Pedidos);
        btnConfirmarOferta = (Button) findViewById(R.id.btnConfirmarOferta);
        btnVolver = (Button) findViewById(R.id.btnVolver);
    }

    private String validaOferta(Context context, int cantidadTotal)
    {

        for (Iterator iteradorOferOper = oferOpers.listIterator(); iteradorOferOper.hasNext(); )
        {
            OferOperOfertas oferOperOfertas = (OferOperOfertas) iteradorOferOper.next();

            oferOperOfertas.procesado = false;

        }

        String mensaje = "";

        int cantidadTotalBase = 0;
        int cantidadTotalBonificado = 0;
        float cantidadBultosTotalBase = 0;
        // Validar Base
        for (Iterator iteradorOfer_Det = ofer_detsBase.listIterator(); iteradorOfer_Det.hasNext(); )
        {
            Ofer_Det ofer_det = (Ofer_Det) iteradorOfer_Det.next();

            //Recorro oferoper
            int cantidad = 0;
            for (Iterator iteradorOferOper = oferOpers.listIterator(); iteradorOferOper.hasNext(); )
            {
                OferOperOfertas oferOperOfertas = (OferOperOfertas) iteradorOferOper.next();

                if (oferOperOfertas.rowIdOferOper == ofer_det.rowid)
                {
                    if (!oferOperOfertas.procesado)
                    {
                        cantidadTotalBase = cantidadTotalBase + oferOperOfertas.cantidad;
                        oferOperOfertas.procesado = true;
                    }
                    cantidad = cantidad + oferOperOfertas.cantidad;
                }
            }

            if (ofer_det.D_Cant != 0 && ofer_det.H_Cant != 0)
            {
                if (!(cantidad >= ofer_det.D_Cant && cantidad <= ofer_det.H_Cant))
                {
                    mensaje = mensaje + "Error en Base: La cantidad de " + ofer_det.Descrip + "debe ser > " + String.valueOf(ofer_det.D_Cant) + " y <= " + String.valueOf(ofer_det.H_Cant) + "\n";
                }
            }

            if (ofer_det.Cpra_Oblig == 1 && cantidad == 0)
            {
                if (!mensaje.contains("Error en Base: Compra obligatoria en " + ofer_det.Descrip + "\n"))
                {
                    mensaje = mensaje + "Error en Base: Compra obligatoria en " + ofer_det.Descrip + "\n";
                }
            }
            if (ofer_det.Multiplo != 0)
            {
                if (cantidad / ofer_det.Multiplo != 0)
                {
                    mensaje = mensaje + "Error en base: Debe ser multiplo de " + String.valueOf(ofer_det.Multiplo) + " " + ofer_det.Descrip + "\n";
                }
            }
        }

        // Validar Bonificado
        for (Iterator iteradorOfer_Det = ofer_detsBonificada.listIterator(); iteradorOfer_Det.hasNext(); )
        {
            Ofer_Det ofer_det = (Ofer_Det) iteradorOfer_Det.next();

            //Recorro oferoper
            int cantidad = 0;
            for (Iterator iteradorOferOper = oferOpers.listIterator(); iteradorOferOper.hasNext(); )
            {
                OferOperOfertas oferOperOfertas = (OferOperOfertas) iteradorOferOper.next();

                if (oferOperOfertas.rowIdOferOper == ofer_det.rowid)
                {
                    cantidadTotalBonificado = cantidadTotalBonificado + oferOperOfertas.cantidad;
                }
            }
        }

        if (!(cantidadTotalBase >= oferEsp.Base_Dcant && cantidadTotalBase <= oferEsp.Base_Hcant))
        {
            mensaje = mensaje + "Error en BASE: La cantidad total debe ser > " + String.valueOf(oferEsp.Base_Dcant) + " y <= " + String.valueOf(oferEsp.Base_Hcant) + "\n";
        }

        if (mensaje.isEmpty())
        {
            if (oferEsp.Bon_Cada != 0 && oferEsp.Bon_Cant != 0)
            {
                float cadaUno = (float) oferEsp.Bon_Cant / oferEsp.Bon_Cada;
                float cantidadaBonificar = cadaUno * cantidadTotalBase;
                //int cantidadaBonificar = (int)(cantidadTotalBase/oferEsp.Bon_Cada);

                if ((int) cantidadaBonificar != cantidadTotalBonificado)
                {
                    mensaje = mensaje + "Error en BONIFICADA: La cantidad total debe ser " + String.valueOf((int) cantidadaBonificar) + "\n";
                }
            }
        }

        if (oferEsp.D_Cant != 0 && oferEsp.H_Cant != 0)
        {
            int cantidadTotalOfertaBaseBonificada = (cantidadTotalBase + cantidadTotalBonificado) * cantidadTotal;
            if (!(oferEsp.D_Cant >= cantidadTotalOfertaBaseBonificada && oferEsp.H_Cant <= cantidadTotalOfertaBaseBonificada))
            {
                mensaje = mensaje + "Error en la cantidad total de la oferta";
            }
        }

        return mensaje;
    }

    private void cerrarOferta(Context context, int cantidadOferta)
    {
        String claveUnica;
        IDao dao = FabricaNegocios.obtenerDao(context);

        CabOper cabOper = new CabOper();

        if (claveUnicaCabOper == null)
        {
            claveUnica = java.util.UUID.randomUUID().toString();
            claveUnicaCabOper = claveUnica;

            cabOper.claveUnica = claveUnica;

            cabOper.idOperacion = 1;
            cabOper.idCliente = idClienteSeleccionado;

            java.sql.Date sqlDate = Fecha.obtenerFechaActual();

            cabOper.fecha = sqlDate;
            cabOper.fechaEntrega = sqlDate;
            cabOper.idVendedor = loginUsuario;
            cabOper.enviado = 0;
            cabOper.domicilio = "";
            Mapeador<CabOper> cabOperMapeador = new Mapeador<CabOper>(cabOper);
            ContentValues contentValues = cabOperMapeador.entityToContentValues();
            dao.insert("cabOper", contentValues);

        }
        else
        {
            claveUnica = claveUnicaCabOper;
        }

        DetOper detOper = new DetOper();
        detOper.claveUnica = claveUnica;
        detOper.idArticulo = "OFER " + codigoOferta;
        detOper.entero = cantidadOferta; //Integer.valueOf(edtCantidadOferta.getText().toString());
        detOper.precio = totalOferta;
        detOper.idFila = java.util.UUID.randomUUID().toString();
        detOper.oferta = codigoOferta;

        Mapeador<DetOper> detOperMapeador = new Mapeador<DetOper>(detOper);
        ContentValues contentValuesdetOper = detOperMapeador.entityToContentValues();

        dao.insert("detOper", contentValuesdetOper);

        for (Iterator iterador = oferOpers.listIterator(); iterador.hasNext(); )
        {
            OferOper oferOper = (OferOper) iterador.next();

            OferOper oferOperNuevo = new OferOper();
            oferOperNuevo.claveUnica = detOper.idFila;
            oferOperNuevo.idArticulo = oferOper.idArticulo;
            oferOperNuevo.precio = oferOper.precio;
            oferOperNuevo.cantidad = oferOper.cantidad;
            oferOperNuevo.descuento = oferOper.descuento;
            oferOperNuevo.enviado = 0;
            oferOperNuevo.idFila = java.util.UUID.randomUUID().toString();
            oferOperNuevo.tipo = oferOper.tipo;

            Mapeador<OferOper> oferOperMapeador = new Mapeador<OferOper>(oferOperNuevo);
            ContentValues contentValuesOferOper = oferOperMapeador.entityToContentValues();
            dao.insert("oferOper", contentValuesOferOper);

        }

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent();
        intent.putExtra("oferOpers", oferOpers);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void cargaDetalleOferta(Context applicationContext)
    {
        lvDetalleOfertaPedidos.setAdapter(new AdaptadorListaGenerico(this, R.layout.activity_lista_pedido, oferOpers)
        {
            @Override
            public void onEntrada(Object entrada, View view)
            {

                TextView tvCodigoArticulo = (TextView) view.findViewById(R.id.tvCodigoArticulo);
                TextView tvArticulo = (TextView) view.findViewById(R.id.tvArticulo);
                TextView tvCantidad = (TextView) view.findViewById(R.id.tvCantidad);
                TextView tvPrecio = (TextView) view.findViewById(R.id.tvPrecio);
                TextView tvDescuento = (TextView) view.findViewById(R.id.tvDescuento);
                TextView tvFinal = (TextView) view.findViewById(R.id.tvFinal);

                String codigoArticulo = ((OferOper) entrada).idArticulo;
                tvCodigoArticulo.setText(codigoArticulo);

                Articulo articulo = controladorArticulo.buscarArticuloPorCodigo(codigoArticulo.trim());

                String nombre = articulo.nombre;
                tvArticulo.setText(nombre);

                float precio = ((OferOper) entrada).precio;
                tvPrecio.setText(String.valueOf(precio));

                float descuento = ((OferOper) entrada).descuento;
                tvDescuento.setText(String.valueOf(descuento));

                int cantidad = ((OferOper) entrada).cantidad;
                tvCantidad.setText(String.valueOf(cantidad));

                float total = 0;
                if (descuento < 100)
                {
                    total = (precio - ((precio * descuento) / 100)) * cantidad;
                }
                tvFinal.setText(String.valueOf(total));

            }
        });
        lvDetalleOfertaPedidos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l)
            {

                FabricaMensaje.dialogoAlertaSiNo(Detalle_Oferta_NoConfirmada.this, "", "¿Seguro que borra el pedido?", new DialogoAlertaSiNo()
                {
                    @Override
                    public void Positivo()
                    {

                        oferOpers.remove(i);
                        edtTotalOfertaNoConfirmada.setText(String.valueOf(calcularTotal()));
                        cargaDetalleOferta(Detalle_Oferta_NoConfirmada.this);
                    }

                    @Override
                    public void Negativo()
                    {

                    }
                }).show();

                return false;
            }
        });
    }

    private float calcularTotal()
    {

        float total = 0f;

        for (OferOperOfertas oferOper : oferOpers)
        {
            float precio = oferOper.precio;
            int cantidad = oferOper.cantidad;
            float descuento = oferOper.descuento;
            float parcial = 0;
            if (descuento < 100)
            {
                float importeDescuento = precio * (descuento / 100);
                parcial = ((precio - importeDescuento) * cantidad);
            }
            total += parcial;
        }
//        edtTotalOfertaNoConfirmada.setText(String.valueOf(total));
        return total;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }
}
