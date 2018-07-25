package inteldev.android.presentation.Mayorista.Folder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import inteldev.android.R;
import inteldev.android.modelo.Articulo;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable
{
    private final OnItemClickListener listener;
    private ArrayList<Articulo> mItems;
    private ArrayList<Articulo> mItemsFiltrados;
    private RecyclerViewAdapter.ValueFilter valueFilter;


    RecyclerViewAdapter(Context context, ArrayList<Articulo> mItems, OnItemClickListener listener)
    {
        this.mItems = mItems;
        this.mItemsFiltrados = mItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View llArticulo = LayoutInflater.from(parent.getContext()).inflate(R.layout.articulo, parent, false);
        return new ViewHolder(llArticulo);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position)
    {
        holder.bind(mItems.get(position), listener);
    }

    @Override
    public int getItemCount()
    {
        if (mItems != null)
        {
            return mItems.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter()
    {
        if (valueFilter == null)
        {
            valueFilter = new RecyclerViewAdapter.ValueFilter();
        }
        return valueFilter;
    }

    public interface OnItemClickListener
    {
        void onItemClick(Articulo item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvCodigoArticulo;
        public TextView tvArticulo;

        ViewHolder(View itemView)
        {
            super(itemView);
            tvCodigoArticulo = itemView.findViewById(R.id.tvCodigoArticulo);
            tvArticulo = itemView.findViewById(R.id.tvArticulo);
        }

        void bind(final Articulo articulo, final OnItemClickListener listener)
        {
            tvCodigoArticulo.setText(articulo.idArticulo);
            tvArticulo.setText(articulo.nombre);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    listener.onItemClick(articulo);
                }
            });
        }
    }

    public class ValueFilter extends Filter
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            String str = constraint.toString().toUpperCase();
            Log.e("constraint", str);
            FilterResults results = new FilterResults();

            if (constraint.length() >= 3)
            {
                ArrayList<Articulo> filterList = new ArrayList<>();
                //                Dao dao = new Dao(context);
                //                Cursor cursor = dao.ejecutarConsultaSql("select articulos.* from articulos inner join detlistas on Articulos.idArticulo = detListas.articulo where detListas.folder>0 and articulos.nombre like '%" + constraint + "%' order by Articulos.nombre");
                //                Mapeador<Articulo> mapeador = new Mapeador<>(new Articulo());
                //                filterList = mapeador.cursorToList(cursor);
                for (int i = 0; i < mItemsFiltrados.size(); i++)
                {
                    if ((mItemsFiltrados.get(i).nombre.toUpperCase()).contains(constraint.toString().toUpperCase()) || (mItemsFiltrados.get(i).idArticulo.toUpperCase().contains(constraint.toString().toUpperCase())))
                    {
                        Articulo bean_contacts = mItemsFiltrados.get(i);
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
            mItems = (ArrayList<Articulo>) results.values;
            notifyDataSetChanged();
        }
    }
}
