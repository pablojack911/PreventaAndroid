package inteldev.android.presentation.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import inteldev.android.CONSTANTES;

import static inteldev.android.CONSTANTES.ARTICULOS_VIEW;
import static inteldev.android.CONSTANTES.CLAVE_UNICA_CABOPER;
//import static inteldev.android.CONSTANTES.CONVENIOS_VIEW;
import static inteldev.android.CONSTANTES.DETALLE_PEDIDO_VIEW;
import static inteldev.android.CONSTANTES.ID_CLIENTE_SELECCIONADO;
import static inteldev.android.CONSTANTES.MOTIVO_NO_COMPRA_VIEW;
import static inteldev.android.CONSTANTES.OFERTAS_VIEW;

/**
 * Created by Pocho on 31/03/2017.
 */

public class ClienteCollectionPagerAdapter extends FragmentStatePagerAdapter
{
    String clienteSeleccionado;
    String claveUnicaCabOper;

    ClienteCollectionPagerAdapter(Context context, FragmentManager fm)
    {
        super(fm);
    }

    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (position)
        {
            case ARTICULOS_VIEW:
                fragment = new FragmentArticulos();
                args.putString(ID_CLIENTE_SELECCIONADO, clienteSeleccionado);
                args.putString(CLAVE_UNICA_CABOPER, claveUnicaCabOper);
                fragment.setArguments(args);
                break;
            case CONSTANTES.OFERTAS_VIEW:
                fragment = new FragmentOfertas();
                args.putString(ID_CLIENTE_SELECCIONADO, clienteSeleccionado);
                fragment.setArguments(args);
                break;
//            case CONSTANTES.CONVENIOS_VIEW:
//                fragment = new FragmentConvenios();
//                args.putString(ID_CLIENTE_SELECCIONADO, clienteSeleccionado);
//                fragment.setArguments(args);
//                break;
            case CONSTANTES.DETALLE_PEDIDO_VIEW:
                fragment = new FragmentDetallePedido();
                args.putString(ID_CLIENTE_SELECCIONADO, clienteSeleccionado);
                fragment.setArguments(args);
                break;
            case CONSTANTES.MOTIVO_NO_COMPRA_VIEW:
                fragment = new FragmentMotivoNoCompra();
                args.putString(ID_CLIENTE_SELECCIONADO, clienteSeleccionado);
                fragment.setArguments(args);
                break;
            default:
                break;
        }
        return fragment;
    }

    @Override
    public int getCount()
    {
        return CONSTANTES.CANTIDAD_VIEWS_CLIENTE;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        CharSequence titulo = "";
        switch (position)
        {
            case ARTICULOS_VIEW:
                titulo = "ARTICULOS";
                break;
            case OFERTAS_VIEW:
                titulo = "OFERTAS";
                break;
//            case CONVENIOS_VIEW:
//                titulo = "CONVENIOS";
//                break;
            case DETALLE_PEDIDO_VIEW:
                titulo = "DETALLE PEDIDO";
                break;
            case MOTIVO_NO_COMPRA_VIEW:
                titulo = "MOTIVO NO COMPRA";
                break;
            default:
                break;
        }
        return titulo;
    }
}
