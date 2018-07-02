package inteldev.android.presentation;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import inteldev.android.Enum.OrdenRutaDeVenta;
import inteldev.android.R;
import inteldev.android.accesoadatos.Dao;
import inteldev.android.modelo.Cliente;
import inteldev.android.modelo.EstadoGPS;
import inteldev.android.modelo.MotivoNoCompra;
import inteldev.android.modelo.PosicionesGPS;
import inteldev.android.negocios.ControladorPosicionesGPS;
import inteldev.android.negocios.ControladorRutaDeVenta;
import inteldev.android.negocios.FabricaNegocios;
import inteldev.android.negocios.SharedPreferencesManager;
import inteldev.android.presentation.ViewPager.ClienteCollection;
import inteldev.android.servicios.GPSIntentService;

import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;
import static inteldev.android.CONSTANTES.NOMBRE_CLIENTE_KEY;
import static inteldev.android.CONSTANTES.POSICION_GPS_KEY;
import static inteldev.android.CONSTANTES.USUARIO_KEY;

public class RutaDeVenta extends AppCompatActivity
{
    OrdenRutaDeVenta ordenRutaDeVentaSeleccionado = OrdenRutaDeVenta.Recorrido;
    ControladorRutaDeVenta controladorRutaDeVenta;
    ControladorPosicionesGPS controladorPosicionesGPS;
    int diaSeleccionado;
    ControlSpinnerDia controlSpinnerDia;
    RadioGroup rgVisitados;
    RadioButton rbNoVisitados;
    RadioButton rbVisitados;
    RadioButton rbTodos;
    Dao dao;
    String loginUsuario;
    private AdaptadorRutaVentaClientes adapter;
    private SearchView searchView;
    private MenuItem searchViewMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_de_venta);

        loginUsuario = SharedPreferencesManager.getLoginUsuario(RutaDeVenta.this);

        dao = new Dao(this);

        controladorRutaDeVenta = new ControladorRutaDeVenta(FabricaNegocios.obtenerDao(getApplicationContext()));
        controladorPosicionesGPS = new ControladorPosicionesGPS(FabricaNegocios.obtenerDao(getApplicationContext()));

        rgVisitados = findViewById(R.id.rgVisitados);
        rbNoVisitados = findViewById(R.id.rbNoVisitados);
        rbVisitados = findViewById(R.id.rbVisitados);
        rbTodos = findViewById(R.id.rbTodos);

        rbNoVisitados.setChecked(true);

        rgVisitados();
        spinnerDia();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //  if (requestCode == 0000 && resultCode ==RESULT_OK){
        //String resultado = data.getExtras().getString("resultado");
        // close search view if its visible
        lvClientes(diaSeleccionado - 1, ordenRutaDeVentaSeleccionado);
    }

    private void rgVisitados()
    {

        rgVisitados.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                lvClientes(diaSeleccionado - 1, ordenRutaDeVentaSeleccionado);
            }
        });

    }

    public int obtenerSeleccionrgVisitados()
    {

        int seleccion = 0;

        if (rbNoVisitados.isChecked())
        {
            seleccion = 0;
        }
        else
        {
            if (rbVisitados.isChecked())
            {
                seleccion = 1;
            }
            else
            {
                if (rbTodos.isChecked())
                {
                    seleccion = 2;
                }
            }
        }

        return seleccion;
    }

    public void spinnerDia()
    {
        controlSpinnerDia = findViewById(R.id.CtlSpinnerDia);
        controlSpinnerDia.setOnSpinnerDiaListener(new OnSpinnerDiaListener()
        {
            @Override
            public void Ejecutar()
            {

                Spinner spDia = findViewById(R.id.spDia);

                spDia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
                    {

                        diaSeleccionado = i + 1;
                        lvClientes(i, ordenRutaDeVentaSeleccionado);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView)
                    {
                    }
                });
            }
        });

        diaSeleccionado = inteldev.android.negocios.diaDeLaSemana.getDayOfTheWeek(new Date());
        controlSpinnerDia.spDia.setSelection(diaSeleccionado - 1);
        lvClientes(diaSeleccionado - 1, OrdenRutaDeVenta.Recorrido);
    }

    public void lvClientes(int diaAConsultarSegunArray, OrdenRutaDeVenta ordenRutaDeVenta)
    {
        if (searchViewMenu != null && searchView != null)
        {
            searchViewMenu.collapseActionView();
        }
        int seleccionVisitado = obtenerSeleccionrgVisitados();

        ArrayList<Cliente> listaClientes = controladorRutaDeVenta.obtenerClientesRutaDeVenta(loginUsuario, diaAConsultarSegunArray, seleccionVisitado, ordenRutaDeVenta);

        ListView lvClientes = findViewById(R.id.lvClientes);
        adapter = new AdaptadorRutaVentaClientes(this, R.layout.activity_lista_clientes_ruta_de_venta, listaClientes)
        {
            @Override
            public void onEntrada(Object entrada, View view)
            {
                TextView tvCodigo = view.findViewById(R.id.tvCodigoCliente);
                TextView tvNombre = view.findViewById(R.id.tvNombre);
                TextView tvDomicilio = view.findViewById(R.id.tvDomicilio);
                TextView tvTelefono = view.findViewById(R.id.tvTelefono);
                TextView tvCuit = view.findViewById(R.id.tvCuit);


                //                String nombreCliente = ((Cliente) entrada).getNombre() + " (" + ((Cliente) entrada).idCliente + ")";
                tvCodigo.setText(((Cliente) entrada).getIdCliente());
                tvNombre.setText(((Cliente) entrada).getNombre());
                tvDomicilio.setText(((Cliente) entrada).getDomicilio());
                tvTelefono.setText(((Cliente) entrada).getTelefono());
                tvCuit.setText(((Cliente) entrada).getCuit());
            }
        };
        lvClientes.setAdapter(adapter);

        lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (!FabricaNegocios.leerEstadoParaUsoApp(getApplicationContext()))
                {
                    FabricaMensaje.dialogoOk(RutaDeVenta.this, "No se puede continuar", "El GPS está desactivado", new DialogoAlertaNeutral()
                    {
                        @Override
                        public void Neutral()
                        {

                        }
                    }).show();
                }
                else
                {
                    Cliente elegido = (Cliente) adapterView.getItemAtPosition(i);

                    PosicionesGPS posicionesGPS = new PosicionesGPS();
                    posicionesGPS.estado = EstadoGPS.CHECKIN_CLIENTE;
                    posicionesGPS.motivoNoCompra = MotivoNoCompra.COMPRADOR;
                    posicionesGPS.idCliente = elegido.idCliente;
                    posicionesGPS.usuario = loginUsuario;
                    enviarPosicion(posicionesGPS);

                    //                    Intent intentPedidos = new Intent(getApplicationContext(), PedidosMayorista.class);
                    Intent intentPedidos = new Intent(getApplicationContext(), ClienteCollection.class);
                    intentPedidos.putExtra(ID_CLIENTE_SELECCIONADO, elegido.getIdCliente());
                    intentPedidos.putExtra(USUARIO_KEY, loginUsuario);
                    intentPedidos.putExtra(NOMBRE_CLIENTE_KEY, elegido.getNombre());
                    startActivityForResult(intentPedidos, 0000);
                }
            }
        });

    }

    private void enviarPosicion(PosicionesGPS posicionesGPS)
    {
        Intent mServiceIntent = new Intent(this, GPSIntentService.class);
        mServiceIntent.putExtra(POSICION_GPS_KEY, posicionesGPS);
        this.startService(mServiceIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ruta_de_venta, menu);
        configuraBuscador(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        //        int id = item.getItemId();
        //        if (id == R.id.action_settings)
        //        {
        //            return true;
        //        }
        switch (item.getItemId())
        {

            //            menu.getItem(0).getSubMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            //            {
            //                @Override
            //                public boolean onMenuItemClick(MenuItem item)
            //                {
            //                    ordenRutaDeVentaSeleccionado = OrdenRutaDeVenta.Nombre;
            //                    lvClientes(diaSeleccionado - 1, ordenRutaDeVentaSeleccionado);
            //                    return false;
            //                }
            //            });
            //
            //            menu.getItem(0).getSubMenu().getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            //            {
            //                @Override
            //                public boolean onMenuItemClick(MenuItem item)
            //                {
            //                    ordenRutaDeVentaSeleccionado = OrdenRutaDeVenta.Domicilio;
            //                    lvClientes(diaSeleccionado - 1, ordenRutaDeVentaSeleccionado);
            //                    return false;
            //                }
            //            });
            //
            //            menu.getItem(0).getSubMenu().getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            //            {
            //                @Override
            //                public boolean onMenuItemClick(MenuItem item)
            //                {
            //                    ordenRutaDeVentaSeleccionado = OrdenRutaDeVenta.Cuit;
            //                    lvClientes(diaSeleccionado - 1, ordenRutaDeVentaSeleccionado);
            //                    return false;
            //                }
            //            });
            //
            //            menu.getItem(0).getSubMenu().getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            //            {
            //                @Override
            //                public boolean onMenuItemClick(MenuItem item)
            //                {
            //                    ordenRutaDeVentaSeleccionado = OrdenRutaDeVenta.Codigo;
            //                    lvClientes(diaSeleccionado - 1, ordenRutaDeVentaSeleccionado);
            //                    return false;
            //                }
            //            });
            //
            //            menu.getItem(0).getSubMenu().getItem(4).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener()
            //            {
            //                @Override
            //                public boolean onMenuItemClick(MenuItem item)
            //                {
            //                    ordenRutaDeVentaSeleccionado = OrdenRutaDeVenta.Recorrido;
            //                    lvClientes(diaSeleccionado - 1, ordenRutaDeVentaSeleccionado);
            //                    return false;
            //                }
            //            });
            case R.id.OrdenarListaPorCodigo:
                ordenRutaDeVentaSeleccionado = OrdenRutaDeVenta.Codigo;
                item.setChecked(true);
                lvClientes(diaSeleccionado - 1, ordenRutaDeVentaSeleccionado);
                return true;
            case R.id.OrdenarListaPorCuit:
                ordenRutaDeVentaSeleccionado = OrdenRutaDeVenta.Cuit;
                item.setChecked(true);
                lvClientes(diaSeleccionado - 1, ordenRutaDeVentaSeleccionado);
                return true;
            case R.id.OrdenarListaPorDomicilio:
                ordenRutaDeVentaSeleccionado = OrdenRutaDeVenta.Domicilio;
                item.setChecked(true);
                lvClientes(diaSeleccionado - 1, ordenRutaDeVentaSeleccionado);
                return true;
            case R.id.OrdenarlistaPorNombre:
                ordenRutaDeVentaSeleccionado = OrdenRutaDeVenta.Nombre;
                item.setChecked(true);
                lvClientes(diaSeleccionado - 1, ordenRutaDeVentaSeleccionado);
                return true;
            case R.id.OrdenarListaPorRecorrido:
                ordenRutaDeVentaSeleccionado = OrdenRutaDeVenta.Recorrido;
                item.setChecked(true);
                lvClientes(diaSeleccionado - 1, ordenRutaDeVentaSeleccionado);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void configuraBuscador(Menu menu)
    {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null)
        {
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
            {
                @Override
                public boolean onQueryTextSubmit(String query)
                {
                    return false;
                }

                public boolean onQueryTextChange(String newText)
                {
                    if (TextUtils.isEmpty(newText))
                    {
                        resetSearch(); // reset

                    }
                    else
                    {
                        beginSearch(newText); // busqueda
                    }
                    return false;
                }
            };
            searchViewMenu = menu.findItem(R.id.menuSearch);
            searchView = (SearchView) searchViewMenu.getActionView();
            if (null != searchView)
            {
                searchView.setQueryHint(getResources().getString(R.string.buscar_clientes));
                searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
                searchView.setIconifiedByDefault(false);
                searchView.setOnQueryTextListener(queryTextListener);
            }
        }
    }

    public void beginSearch(String query)
    {
        Log.e("QueryFragment", query);
        if (adapter != null)
        {
            adapter.getFilter().filter(query);
        }
    }

    public void resetSearch()
    {
        if (adapter != null)
        {
            adapter.getFilter().filter("");
        }
    }
}

