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

public class SQLLogs {
    public interface VolleyCallback {
        void transferenciaLogFallida(VolleyError error);

        void  transferenciaLogCorrecta(JSONArray response) throws JSONException;
    }

    public void transferirLogs(Context context, SQLLogs.VolleyCallback callback, JSONObject params){
//        CREAMOS EL REQUEST
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        DEFINIMOS EL ENDPOINT
        Log.i("LOG PARAMS", params.toString());
        String url = "http://192.168.30.94:8080/api/logs/transferir_logs";

//        GENERAMOS EL CONSUMO DE LA API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                response -> {
                    try {
                        callback.transferenciaLogCorrecta(new JSONArray().put(response));
                    } catch (JSONException e) {
                        Swal.error(context, "Oops!", e.toString());
                    }
                },
                error -> {
                    callback.transferenciaLogFallida(error);
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

}
