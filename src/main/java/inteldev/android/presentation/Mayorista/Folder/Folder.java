package inteldev.android.presentation.Mayorista.Folder;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SearchView;

import inteldev.android.R;
import inteldev.android.modelo.Articulo;
import inteldev.android.negocios.ControladorArticulo;
import inteldev.android.negocios.FabricaNegocios;

import static inteldev.android.CONSTANTES.ARTICULO_SELECCIONADO;

public class Folder extends AppCompatActivity
{
    private SearchView searchView;
    private ControladorArticulo controladorArticulos;
    private RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        searchView = findViewById(R.id.buscador_articulo);
        controladorArticulos = FabricaNegocios.obtenerControladorArticulo(Folder.this);
        configuraRvFolder();
        configuraBuscador();
    }

    private void configuraRvFolder()
    {
        RecyclerView rvFolder = findViewById(R.id.rvFolder);
        setTitle(R.string.folder);
        rvFolder.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvFolder.setLayoutManager(mLayoutManager);
        adapter = new RecyclerViewAdapter(Folder.this, controladorArticulos.obtenerFolder(), new RecyclerViewAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(Articulo item)
            {
                Intent intent = new Intent();
                intent.putExtra(ARTICULO_SELECCIONADO, item);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        rvFolder.setAdapter(adapter);
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
