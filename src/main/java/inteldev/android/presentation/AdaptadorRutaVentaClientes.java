package inteldev.android.presentation;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import inteldev.android.modelo.Cliente;


public abstract class AdaptadorRutaVentaClientes extends BaseAdapter implements Filterable
{
    private ArrayList<Cliente> mItemsFiltrados;
    private ArrayList<Cliente> Items;
    private int R_layout_Id_View;
    private Context context;
    private ValueFilter valueFilter;

    public AdaptadorRutaVentaClientes(Context contexto, int R_layout_IdView, ArrayList<Cliente> entradas)
    {
        Items = entradas;
        mItemsFiltrados = entradas;
        R_layout_Id_View = R_layout_IdView;
        this.context = contexto;
    }

    @Override
    public int getCount()
    {
        if (this.Items != null)
        {
            return this.Items.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return this.Items.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R_layout_Id_View, null);
        }
        onEntrada(Items.get(position), convertView);
        return convertView;
    }

    public abstract void onEntrada(Object item, View view);

    @Override
    public Filter getFilter()
    {
        if (valueFilter == null)
        {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    public class ValueFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            String str = constraint.toString().toUpperCase();
            Log.e("constraint", str);
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0)
            {
                ArrayList<Cliente> filterList = new ArrayList<Cliente>();
                for (int i = 0; i < mItemsFiltrados.size(); i++)
                {
                    if ((mItemsFiltrados.get(i).getNombre().toUpperCase()).contains(constraint.toString().toUpperCase()) || (mItemsFiltrados.get(i).getIdCliente().toUpperCase().contains(constraint.toString().toUpperCase())))
                    {
                        Cliente bean_contacts = mItemsFiltrados.get(i);
                        filterList.add(bean_contacts);
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            }
            else
            {
                results.count = mItemsFiltrados.size();
                results.values = mItemsFiltrados;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            Items = (ArrayList<Cliente>) results.values;
            notifyDataSetChanged();
        }
    }
}
