package com.easj.chromosmobile.Adapters.AdapterReporteMarcas;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.easj.chromosmobile.Adapters.TipoMarcacion.AdapterTipoMarcacion;
import com.easj.chromosmobile.Entitys.ReporteMarcas;
import com.easj.chromosmobile.R;

import java.util.List;

public class AdapterReporteMarcas extends RecyclerView.Adapter<AdapterReporteMarcas.ViewHolder> {

    private Context context;
    private List<ReporteMarcas> reporteMarcasList;
    private AdapterReporteMarcas.OnItemClickListener onItemClickListener; // Agregamos la interfaz como miembro

//    CONSTRUCTOR
    public AdapterReporteMarcas(Context context, List<ReporteMarcas> reporteMarcasList, OnItemClickListener onItemClickListener){
        this.context = context;
        this.reporteMarcasList = reporteMarcasList;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public AdapterReporteMarcas.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.report_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterReporteMarcas.ViewHolder holder, int position) {

        String trabajador = reporteMarcasList.get(position).getTrabajador() != null ?
                reporteMarcasList.get(position).getTrabajador() : "TRABAJADOR DESCONOCIDO";
        Drawable drawableTipoLectura = reporteMarcasList.get(position).getTipoMarcacion().equals("T") ?
                context.getDrawable(R.drawable.ic_touch_color) :
                context.getDrawable(R.drawable.ic_scanner_color);
        Log.i("CCC", reporteMarcasList.get(position).getTipoMarcacion());
        Drawable drawableTipoMarcacion =
                reporteMarcasList.get(position).getDescripcion().equals("INGRESO") ||
                reporteMarcasList.get(position).getDescripcion().equals("REFRIGERIO") ?
                    context.getDrawable(R.drawable.ic_in) :
                    context.getDrawable(R.drawable.ic_out);


        holder.imageTipoLectura.setImageDrawable(drawableTipoLectura);
        holder.imageTipoMarca.setImageDrawable(drawableTipoMarcacion);
        holder.imageTransfer.setVisibility(reporteMarcasList.get(position).getProcesada().equals("0") ? View.INVISIBLE : View.VISIBLE);

        holder.textoTrabajador.setText(trabajador);
        holder.textoFecha.setText(reporteMarcasList.get(position).getFecha());
        holder.textoDni.setText(reporteMarcasList.get(position).getDni());
    }

    @Override
    public int getItemCount() {
        return reporteMarcasList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageTipoMarca, imageTipoLectura, imageTransfer;
        TextView textoTrabajador, textoFecha, textoDni;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageTipoMarca = itemView.findViewById(R.id.imageTipoMarca);
            imageTipoLectura = itemView.findViewById(R.id.imageTipoLectura);
            imageTransfer = itemView.findViewById(R.id.imageTransfer);
            textoTrabajador = itemView.findViewById(R.id.textTrabajador);
            textoFecha = itemView.findViewById(R.id.textFechaHora);
            textoDni = itemView.findViewById(R.id.textDNI);
        }
    }
}
