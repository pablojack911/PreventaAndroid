package inteldev.android.presentation.DescuentosConvenios;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import inteldev.android.R;
import inteldev.android.negocios.SharedPreferencesManager;

import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;

/**
 * Created by Pocho on 03/04/2017.
 */

public class DescuentosConveniosCollection extends FragmentActivity
{
    DescuentosConveniosPagerAdapter mDescuentosConveniosPagerAdapter;
    String loginUsuario;
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtos_convs);
        loginUsuario = SharedPreferencesManager.getLoginUsuario(DescuentosConveniosCollection.this);
        String clienteSeleccionado;
        if (getIntent().hasExtra(ID_CLIENTE_SELECCIONADO))
        {
            clienteSeleccionado = getIntent().getStringExtra(ID_CLIENTE_SELECCIONADO);
        }
        else
        {
            clienteSeleccionado = null;
        }
        mDescuentosConveniosPagerAdapter = new DescuentosConveniosPagerAdapter(DescuentosConveniosCollection.this,getSupportFragmentManager());
        mDescuentosConveniosPagerAdapter.idClienteSeleccionado = clienteSeleccionado;
        mViewPager = (ViewPager) findViewById(R.id.dtos_convs_pager);
        mViewPager.setAdapter(mDescuentosConveniosPagerAdapter);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }
}
