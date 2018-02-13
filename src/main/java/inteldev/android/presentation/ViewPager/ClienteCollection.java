package inteldev.android.presentation.ViewPager;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import inteldev.android.R;
import inteldev.android.presentation.LoginObservable;

import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;

/**
 * Created by Pocho on 31/03/2017.
 */

public class ClienteCollection extends FragmentActivity
{
    ClienteCollectionPagerAdapter mClienteCollectionPagerAdapter;
    String loginUsuario;
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        loginUsuario = LoginObservable.getInstancia().getLoginUsuario();
        String clienteSeleccionado;
        if (getIntent().hasExtra(ID_CLIENTE_SELECCIONADO))
        {
            clienteSeleccionado = getIntent().getStringExtra(ID_CLIENTE_SELECCIONADO);
        }
        else
        {
            clienteSeleccionado = null;
        }

        mClienteCollectionPagerAdapter = new ClienteCollectionPagerAdapter(getSupportFragmentManager());
        mClienteCollectionPagerAdapter.clienteSeleccionado = clienteSeleccionado;
        mViewPager = (ViewPager) findViewById(R.id.cliente_pager);
        mViewPager.setAdapter(mClienteCollectionPagerAdapter);

        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(mViewPager);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }
}
