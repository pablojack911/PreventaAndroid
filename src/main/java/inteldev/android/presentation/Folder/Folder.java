package inteldev.android.presentation.Folder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import inteldev.android.R;
import inteldev.android.modelo.Articulo;
import inteldev.android.negocios.ControladorArticulo;
import inteldev.android.negocios.FabricaNegocios;

import static inteldev.android.CONSTANTES.ARTICULO_SELECCIONADO;

public class Folder extends AppCompatActivity
{

    private ControladorArticulo controladorArticulos;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        controladorArticulos = FabricaNegocios.obtenerControladorArticulo(Folder.this);
        configuraRvFolder();
    }

    private void configuraRvFolder()
    {
        RecyclerView rvFolder = findViewById(R.id.rvFolder);
        setTitle(R.string.folder);
        rvFolder.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvFolder.setLayoutManager(mLayoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(controladorArticulos.obtenerFolder(), new RecyclerViewAdapter.OnItemClickListener()
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
}
