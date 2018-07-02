package inteldev.android.presentation.Folder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import inteldev.android.R;
import inteldev.android.modelo.Articulo;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{
    private final OnItemClickListener listener;
    private ArrayList<Articulo> mItems;

    RecyclerViewAdapter(ArrayList<Articulo> mItems, OnItemClickListener listener)
    {
        this.mItems = mItems;
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
}
