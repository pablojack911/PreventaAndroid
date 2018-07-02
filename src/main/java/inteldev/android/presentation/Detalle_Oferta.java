package inteldev.android.presentation;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import inteldev.android.R;
import inteldev.android.modelo.Articulo;
import inteldev.android.modelo.OferOper;
import inteldev.android.negocios.ControladorArticulo;
import inteldev.android.negocios.ControladorPedido;
import inteldev.android.negocios.FabricaNegocios;


public class Detalle_Oferta extends AppCompatActivity
{

    ControladorPedido controladorPedido;
    ControladorArticulo controladorArticulo;

    String claveUnica;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_oferta);

        Resources res = getResources();
        Bundle bundle = this.getIntent().getExtras();
        this.claveUnica = bundle.getString("claveUnica");

        ArrayList<OferOper> oferOpers = new ArrayList<OferOper>();

        controladorArticulo = FabricaNegocios.obtenerControladorArticulo(getApplicationContext());

        controladorPedido = FabricaNegocios.obtenerControladorPedido(getApplicationContext());
        oferOpers = controladorPedido.obtenerOfertaPedido(claveUnica);

        final ListView lvDetalleOfertaPedidos = (ListView) findViewById(R.id.lvDetalle_Oferta_Pedidos);

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

                float total = (precio - ((precio * descuento) / 100)) * cantidad;
                tvFinal.setText(String.valueOf(total));

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detalle_oferta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }
}
