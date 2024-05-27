package com.easj.chromosmobile.Adapters.PuntosControl;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easj.chromosmobile.R;

import java.util.List;

public class AdapterPuntosControl extends BaseAdapter {

    private Context context;
    private List<Pair<Integer, String>> listaItems;

    public AdapterPuntosControl(Context context, List<Pair<Integer, String>> listaItems) {
        this.context = context;
        this.listaItems = listaItems;
    }

    @Override
    public int getCount() {
        return listaItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listaItems.get(position);
    }

    public int getPositionItemAt(int id){
        for (int i = 0; i < listaItems.size(); i++) {
            Pair<Integer, String> item = listaItems.get(i);
            if (item.first.equals(id)) {
                // El valor buscado está en la lista
                return i; // Devuelve la posición
            }
        }
        return -1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textId = convertView.findViewById(R.id.text1);
            viewHolder.textLabel = convertView.findViewById(R.id.text2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

            viewHolder.textId.setText(listaItems.get(position).first.toString());
            viewHolder.textLabel.setText(listaItems.get(position).second);

//        for(Pair<Integer, String> item: listaItems.listIterator()){
//        // Configurar el texto en la vista
//            viewHolder.textId.setText(item.first.toString());
//            viewHolder.textLabel.setText(item.second.toString());
//        }

        return convertView;
    }

    // Clase para contener vistas reutilizables
    private static class ViewHolder {
        TextView textId;
        TextView textLabel;
    }
}