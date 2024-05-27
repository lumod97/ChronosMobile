package com.easj.chromosmobile.Logica;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easj.chromosmobile.Adapters.TipoMarcacion.AdapterTipoMarcacion;
import com.easj.chromosmobile.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SwalEstadoTransferido {
    public interface onEstadoSelected {
        void onEstadoSelected(String estadoSeleccionado);
    }

    public static void ListEstadoTransferido(Context ctx, SwalEstadoTransferido.onEstadoSelected callback) {

        List<String> listaItems = new ArrayList<>();
        listaItems.add(0, "TODOS");
        listaItems.add(1, "TRANSFERIDOS");
        listaItems.add(2, "SIN TRANSFERIR");

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        View custom = LayoutInflater.from(ctx).inflate(R.layout.dialog_list_selection, null);
        sweetAlertDialog.setCustomView(custom);
        sweetAlertDialog.hideConfirmButton();
        sweetAlertDialog.setOnShowListener(dialogInterface -> {

            // Obtener la referencia al RecyclerView
            RecyclerView recyclerView = custom.findViewById(R.id.rvItemListSelection);

            // Crear el adaptador y configurarlo en el RecyclerView
            AdapterTipoMarcacion adaptador = new AdapterTipoMarcacion(ctx, listaItems, new AdapterTipoMarcacion.OnItemClickListener() {
                @Override
                public void onItemClick(int position, String tipoMarcacion) {

                    callback.onEstadoSelected(tipoMarcacion); // Llamar al callback con el valor de tipoMarcacion
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            recyclerView.setAdapter(adaptador);
            // Configurar el administrador de diseño del RecyclerView (ajusta esto según tus necesidades)
            recyclerView.setLayoutManager(new LinearLayoutManager(ctx));

        });
        sweetAlertDialog.show();
    }

}
