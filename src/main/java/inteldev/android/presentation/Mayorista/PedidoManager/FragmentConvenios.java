package inteldev.android.presentation.Mayorista.PedidoManager;

import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class FragmentConvenios extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_convenios, container, false);
        Bundle args = getArguments();
        ((TextView) rootView.findViewById(R.id.cf_convenio)).setText(args.getString(CONSTANTES.CODIGO_CONVENIO));
        return rootView;
    }
}
