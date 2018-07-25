package inteldev.android.presentation.Articulos;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;

import inteldev.android.accesoadatos.Dao;
import inteldev.android.modelo.Articulo;
import inteldev.android.negocios.Mapeador;

/**
 * Created by pinsua on 26/03/2018.
 *
 */

public abstract class ArticulosAdapter extends BaseAdapter implements Filterable
{
    private final int R_layout_Id_View;
    private final Context context;
    private ArrayList<Articulo> mItems;
    private ValueFilter valueFilter;

    public ArticulosAdapter(int r_layout_Id_View, Context context)
    {
        mItems = new ArrayList<>();
        R_layout_Id_View = r_layout_Id_View;
        this.context = context;
    }

    @Override
    public int getCount()
    {
        if (this.mItems != null)
        {
            return this.mItems.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position)
    {
        return mItems.get(position);
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
        onItemAdded(mItems.get(position), convertView);
        return convertView;
    }

    public abstract void onItemAdded(Articulo articulo, View convertView);


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

            if (constraint != null && constraint.length() >= 3)
            {
                ArrayList<Articulo> filterList;
                Dao dao = new Dao(context);
                Cursor cursor = dao.ejecutarConsultaSql("select * from articulos where nombre like '%" + constraint + "%' order by nombre");
                Mapeador<Articulo> mapeador = new Mapeador<>(new Articulo());
                filterList = mapeador.cursorToList(cursor);
                results.count = filterList.size();
                results.values = filterList;
            }
            else
            {
                results.count = mItems.size();
                results.values = mItems;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            mItems = (ArrayList<Articulo>) results.values;
            notifyDataSetChanged();
        }
    }
}
