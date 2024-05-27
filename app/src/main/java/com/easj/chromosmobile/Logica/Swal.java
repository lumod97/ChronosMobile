package com.easj.chromosmobile.Logica;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easj.chromosmobile.Adapters.TipoMarcacion.AdapterTipoMarcacion;
import com.easj.chromosmobile.R;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Swal {
//    ERROR
    public static void error(Context ctx, String title, String body, Number delay) {
        SweetAlertDialog sd = new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE);
        sd
                .setTitleText(title)
                .setContentText(body);
        sd.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sd.hide();
            }
        }, delay.longValue());
    }

    public static void error(Context ctx, String title, String body) {
        SweetAlertDialog sd = new SweetAlertDialog(ctx, SweetAlertDialog.ERROR_TYPE);
        sd
                .setTitleText(title)
                .setContentText(body);
        sd.show();
    }

//    SUCCESS
    public static void success(Context ctx, String title, String body, Number delay) {
        SweetAlertDialog sd = new SweetAlertDialog(ctx, SweetAlertDialog.SUCCESS_TYPE);
        sd
                .setTitleText(title)
                .setContentText(body);
        sd.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sd.hide();
            }
        }, delay.longValue());
    }

//    INFO
    public static void info(Context ctx, String title, String body, Number delay) {
        SweetAlertDialog sd = new SweetAlertDialog(ctx, SweetAlertDialog.NORMAL_TYPE);
        sd
                .setTitleText(title)
                .setContentText(body);
        sd.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sd.hide();
            }
        }, delay.longValue());
    }

//    WARNING
    public static void warning(Context ctx, String title, String body, Number delay) {
        SweetAlertDialog sd = new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE);
        sd
                .setTitleText(title)
                .setContentText(body);
        sd.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sd.hide();
            }
        }, delay.longValue());
    }

    public static void warning(Context ctx, String title, String body) {
        SweetAlertDialog sd = new SweetAlertDialog(ctx, SweetAlertDialog.WARNING_TYPE);
        sd
                .setTitleText(title)
                .setContentText(body);
        sd.show();
        Handler handler = new Handler();
    }

    public static SweetAlertDialog confirm(Context ctx, String title, String body) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog
                .setTitleText(title)
                .setContentText(body)
                .setConfirmText("Si")
                .setCancelText("No")
                .show();
        return sweetAlertDialog;
    }

    public static SweetAlertDialog edit(Context ctx, List<String> listaItems, AdapterTipoMarcacion.Callback callback) {
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
                    callback.onItemSelected(tipoMarcacion); // Llamar al callback con el valor de tipoMarcacion
                    sweetAlertDialog.dismissWithAnimation();
                }
            });
            recyclerView.setAdapter(adaptador);

            // Configurar el administrador de diseño del RecyclerView (ajusta esto según tus necesidades)
            recyclerView.setLayoutManager(new LinearLayoutManager(ctx));

        });
        return sweetAlertDialog;
    }

    public interface PasswordValidationCallback {
        void onPasswordValidated(boolean isValid, SweetAlertDialog sweetAlertDialog);
    }


    public static void settingsPassword(Context ctx, PasswordValidationCallback callback) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ctx, SweetAlertDialog.CUSTOM_IMAGE_TYPE);
        View custom = LayoutInflater.from(ctx).inflate(R.layout.dialog_settings_password, null);
        sweetAlertDialog.setCustomView(custom);

        sweetAlertDialog.hideConfirmButton();
        custom.findViewById(R.id.btnAceptar).setOnClickListener(view -> {
            EditText inputPassword = custom.findViewById(R.id.inputPassword);
            String enteredPassword = inputPassword.getText().toString();

            boolean isValid = enteredPassword.equals("1598753");

            if (callback != null) {
                callback.onPasswordValidated(isValid, sweetAlertDialog);
            }

        });

        sweetAlertDialog.show();
    }
}
