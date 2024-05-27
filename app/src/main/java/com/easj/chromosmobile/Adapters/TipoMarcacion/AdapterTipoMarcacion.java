package com.easj.chromosmobile.Adapters.TipoMarcacion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easj.chromosmobile.R;

import java.util.List;

public class AdapterTipoMarcacion extends RecyclerView.Adapter<AdapterTipoMarcacion.ViewHolder> {
    private List<String> listaItems;
    private Context context;
    private OnItemClickListener onItemClickListener; // Agregamos la interfaz como miembro

    public AdapterTipoMarcacion(Context context, List<String> listaItems, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.listaItems = listaItems;
        this.onItemClickListener = onItemClickListener;
    }

    // Método que infla el diseño de cada elemento de la lista
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_tipo_marcacion, parent, false);
        return new ViewHolder(view);
    }

    // Método que establece los datos en cada elemento de la lista
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = listaItems.get(position);
        holder.textViewItem.setText(item);

        // Agregar el clic al elemento
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int adapterPosition = holder.getAdapterPosition();
                String itemText = holder.textViewItem.getText().toString();
                if (adapterPosition != RecyclerView.NO_POSITION && onItemClickListener != null) {
                    onItemClickListener.onItemClick(adapterPosition,itemText);
                }
            }
        });
    }

    // Método que devuelve la cantidad de elementos en la lista
    @Override
    public int getItemCount() {
        return listaItems.size();
    }

    public interface Callback {
        void onItemSelected(String tipoMarcacion);
    }

    // Clase ViewHolder para contener las vistas de cada elemento de la lista
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewItem = itemView.findViewById(R.id.textViewItem);
        }
    }

    // Interfaz de escucha para el clic en los elementos
    public interface OnItemClickListener {
        void onItemClick(int position, String tipoMarcacion);
    }
}
