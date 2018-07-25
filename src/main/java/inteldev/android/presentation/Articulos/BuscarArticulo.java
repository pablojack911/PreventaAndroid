package inteldev.android.presentation.Articulos;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import inteldev.android.R;
import inteldev.android.modelo.Articulo;

import static inteldev.android.CONSTANTES.ARTICULO_SELECCIONADO;

public class BuscarArticulo extends AppCompatActivity
{
    ArticulosAdapter adapter;
    ListView lvArticulosFiltrados;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_articulo);
        setTitle(getString(R.string.busca_artic));
        searchView = findViewById(R.id.buscador_articulo);
        lvArticulosFiltrados = findViewById(R.id.lvArticulosFiltrados);
        adapter = new ArticulosAdapter(R.layout.articulo, BuscarArticulo.this)
        {
            @Override
            public void onItemAdded(Articulo articulo, View view)
            {
                TextView tvCodigoArticulo = view.findViewById(R.id.tvCodigoArticulo);
                TextView tvArticulo = view.findViewById(R.id.tvArticulo);
                tvCodigoArticulo.setText(articulo.idArticulo);
                tvArticulo.setText(articulo.nombre);
            }
        };
        lvArticulosFiltrados.setAdapter(adapter);
        lvArticulosFiltrados.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent();
                intent.putExtra(ARTICULO_SELECCIONADO, (Articulo) adapter.getItem(position));
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        configuraBuscador();
        searchView.requestFocus();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    private void configuraBuscador()
    {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (null != searchView)
        {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
            {
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
                    Log.e("Text", newText);
                    return false;
                }

                public boolean onQueryTextSubmit(String query)
                {
                    return false;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
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
