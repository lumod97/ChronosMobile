package com.easj.chromosmobile.SQLProcess;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.easj.chromosmobile.Logica.Swal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SQLRegisterTerminal {
    public interface VolleyCallback {
        void registroFallido(VolleyError error);

        void registroExitoso(JSONArray put) throws JSONException;
    }


    public void registrarDispositivo(Context context, VolleyCallback callback, JSONObject params){
//        CREAMOS EL REQUEST
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        DEFINIMOS EL ENDPOINT
        Log.i("PARAMS", params.toString());
        String url = "http://192.168.30.94:8080/api/configuracion/registrar_terminal";

//        GENERAMOS EL CONSUMO DE LA API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                response -> {
                    try {
                        callback.registroExitoso(new JSONArray().put(response));
                    } catch (JSONException e) {
                        Swal.error(context, "Oops!", e.toString());
                    }
                },
                error -> {
                    callback.registroFallido(error);
                    Log.i("RESPONSE", error.toString());
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}
