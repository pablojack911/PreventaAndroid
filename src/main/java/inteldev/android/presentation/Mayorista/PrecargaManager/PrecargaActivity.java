package inteldev.android.presentation.Mayorista.PrecargaManager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import inteldev.android.R;
import inteldev.android.negocios.ControladorPedido;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.SharedPreferencesManager;
import inteldev.android.presentation.AdaptadorListaGenerico;
import inteldev.android.presentation.Mayorista.PedidoManager.ClienteCollection;

import static inteldev.android.CONSTANTES.CLAVE_UNICA_CABOPER;
import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;
import static inteldev.android.CONSTANTES.INTENT_CLIENTE_MAYORISTA;
import static inteldev.android.CONSTANTES.NOMBRE_CLIENTE_KEY;

public class PrecargaActivity extends AppCompatActivity
{
    ControladorPedido controladorPedido;
    ListView lvPrecargas;
    FloatingActionButton fabNuevaPrecarga;
    String idCliente;
    String nombreCliente;
    String loginUsuario;
    private EditText edtTotalPedido;
    private AdaptadorListaGenerico adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_precarga);
        idCliente = SharedPreferencesManager.getString(PrecargaActivity.this, ID_CLIENTE_SELECCIONADO);
        nombreCliente = SharedPreferencesManager.getString(PrecargaActivity.this, NOMBRE_CLIENTE_KEY);
        setTitle(idCliente + " - " + nombreCliente);
        loginUsuario = SharedPreferencesManager.getLoginUsuario(PrecargaActivity.this);
        controladorPedido = FabricaNegocios.obtenerControladorPedido(PrecargaActivity.this);
        ArrayList<Precarga> precargas = controladorPedido.consultarPrecargas(idCliente);

        edtTotalPedido = findViewById(R.id.edtTotalPedido);
        calcularTotal();
        fabNuevaPrecarga = findViewById(R.id.fabNuevaPrecarga);
        fabNuevaPrecarga.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                prepararNuevaPrecarga();
            }
        });
        lvPrecargas = findViewById(R.id.lvPrecargas);
        adapter = new AdaptadorListaGenerico(PrecargaActivity.this, R.layout.item_precarga, precargas)
        {
            @Override
            public void onEntrada(Object entrada, View view)
            {
                TextView tvPrecarga = view.findViewById(R.id.tvPrecarga);
                TextView tvImportePrecarga = view.findViewById(R.id.tvImportePrecarga);
                tvPrecarga.setText(((Precarga) entrada).nombre);
                tvImportePrecarga.setText(String.format("%.2f", ((Precarga) entrada).totalPrecarga));
            }
        };
        lvPrecargas.setAdapter(adapter);
        lvPrecargas.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intentPedidos = new Intent(getApplicationContext(), ClienteCollection.class);
                SharedPreferencesManager.setString(PrecargaActivity.this, CLAVE_UNICA_CABOPER, ((Precarga) parent.getAdapter().getItem(position)).idCabOper);
                startActivityForResult(intentPedidos, INTENT_CLIENTE_MAYORISTA);
            }
        });

        if (precargas.size() == 0)
        {
            prepararNuevaPrecarga();
        }
    }

    private void prepararNuevaPrecarga()
    {
        SharedPreferencesManager.setString(PrecargaActivity.this, CLAVE_UNICA_CABOPER, controladorPedido.crearNuevaPrecarga(idCliente, loginUsuario));
        Intent intentPedidos = new Intent(getApplicationContext(), ClienteCollection.class);
        startActivityForResult(intentPedidos, INTENT_CLIENTE_MAYORISTA);
    }

    private void calcularTotal()
    {
        float total = controladorPedido.calcularTotal(idCliente);
        if (total != 0)
        {
            edtTotalPedido.setText(String.format("%.2f", total));
        }
        else
        {
            edtTotalPedido.setText("");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        refrescarListaPrecargas();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void refrescarListaPrecargas()
    {
        ArrayList<Precarga> precargas = controladorPedido.consultarPrecargas(idCliente);
        adapter.getEntradas().clear();
        adapter.setEntradas(precargas);
        adapter.notifyDataSetChanged();
        calcularTotal();
    }
}
