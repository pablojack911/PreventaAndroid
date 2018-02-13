package inteldev.android.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by Operador on 30/04/14.
 */
public abstract class AdaptadorListaGenerico extends BaseAdapter {

    private ArrayList<?> entradas;
    private int R_layout_Id_View;
    private Context contexto;

    public AdaptadorListaGenerico(Context contexto, int R_layout_IdView, ArrayList<?> entradas) {
        super();
        this.contexto = contexto;
        this.entradas = entradas;
        this.R_layout_Id_View= R_layout_IdView;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_Id_View, null);
        }
        onEntrada (entradas.get(posicion), view);
        return view;
    }

    @Override
    public int getCount() {
        return entradas.size();
    }

    @Override
    public Object getItem(int i) {
        return entradas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public abstract void onEntrada(Object entrada, View view);
}
