package inteldev.android.presentation.DescuentosConvenios;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import inteldev.android.CONSTANTES;
import inteldev.android.presentation.LoginObservable;

import static inteldev.android.CONSTANTES.CONVENIOS_SIN_CARGO_VIEW;
import static inteldev.android.CONSTANTES.DESCUENTOS_ARTICULOS_VIEW;
import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;

/**
 * Created by Pocho on 03/04/2017.
 */

public class DescuentosConveniosPagerAdapter extends FragmentStatePagerAdapter
{
    String loginUsuario;
    String idClienteSeleccionado;

    public DescuentosConveniosPagerAdapter(FragmentManager fm)
    {
        super(fm);
        loginUsuario = LoginObservable.getInstancia().getLoginUsuario();
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (position)
        {
            case DESCUENTOS_ARTICULOS_VIEW:
                fragment = new FragmentDescuentos();
                args.putString(CONSTANTES.ID_CLIENTE_SELECCIONADO, idClienteSeleccionado);
                fragment.setArguments(args);
                break;
            case CONVENIOS_SIN_CARGO_VIEW:
                fragment = new FragmentConveniosSinCargo();
                args.putString(ID_CLIENTE_SELECCIONADO, idClienteSeleccionado);
                fragment.setArguments(args);
                break;
            default:
                break;
        }
        return fragment;

    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        CharSequence titulo = "";
        switch (position)
        {
            case DESCUENTOS_ARTICULOS_VIEW:
                titulo = "Descuentos";
                break;
            case CONVENIOS_SIN_CARGO_VIEW:
                titulo = "Sin cargo";
                break;
            default:
                break;
        }
        return titulo;
    }

    @Override
    public int getCount()
    {
        return CONSTANTES.CANTIDAD_VIEWS_DTOS_CONVS;
    }
}
