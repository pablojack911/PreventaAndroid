package inteldev.android.presentation.ViewPager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import inteldev.android.CONSTANTES;
import inteldev.android.R;

/**
 * Created by Pocho on 31/03/2017.
 */

public class FragmentOfertas extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_ofertas, container, false);
        Bundle args = getArguments();
        ((TextView) rootView.findViewById(R.id.of_oferta)).setText(args.getString(CONSTANTES.CODIGO_OFERTA));
        return rootView;
    }
}
