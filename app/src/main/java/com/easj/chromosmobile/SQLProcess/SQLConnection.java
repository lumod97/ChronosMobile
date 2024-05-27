package com.easj.chromosmobile.SQLProcess;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.easj.chromosmobile.Logica.Swal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SQLConnection {
    public interface VolleyCallback {
        void onSuccess(JSONArray result) throws JSONException;
        void onError(Exception error);
        void transferenciaCorrecta(JSONArray result) throws JSONException;

        void transferenciaFallida(Exception error);

        void configuracionInicialObtenida(JSONArray result) throws JSONException;

        void configuracionInicialFallida(Exception error);
    }

    public void obtenerPersonas(Context ctx, VolleyCallback callback){
        Log.i("ENTRO","PADENTRO");
//        CREAMOS EL REQUEST
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
//        DEFINIMOS EL ENDPOINT
        String url =  "http://192.168.30.94:8080/api/personas/obtener_personas_con_observacion";

//        GENERAMOS EL CONSUMO DE LA API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        callback.onSuccess(new JSONArray().put(response));
                    } catch (JSONException e) {
                        Swal.error(ctx, "Oops!", "Error al obtener el personal: "+e);
                    }
                },
                error -> {
                    callback.onError(error);
                    Log.i("RESPONSE", error.getMessage());
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void transferirMarcas(Context ctx, VolleyCallback callback, JSONObject params){
        Log.i("ENTRO","PADENTRO");
//        CREAMOS EL REQUEST
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
//        DEFINIMOS EL ENDPOINT
        String url = "http://192.168.30.94:8080/api/marcas/transferir_marcas";
        Log.i("params", params.toString());

//        GENERAMOS EL CONSUMO DE LA API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                response -> {
                    try {
                        callback.transferenciaCorrecta(new JSONArray().put(response));
                    } catch (JSONException e) {
                        Log.e("RESPONSE", e.getMessage());
                    }
                },
                error -> callback.transferenciaFallida(error)
        );

        requestQueue.add(jsonObjectRequest);
    }

    public void sincronizacionInicial(Context context, VolleyCallback callback, JSONObject params){
//        CREAMOS EL REQUEST
        RequestQueue requestQueue = Volley.newRequestQueue(context);
//        DEFINIMOS EL ENDPOINT
        String url = "http://192.168.30.94:8080/api/configuracion/obtener_data_inicial";

//        GENERAMOS EL CONSUMO DE LA API
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, params,
                response -> {
                    try {
                        callback.configuracionInicialObtenida(new JSONArray().put(response));
                    } catch (JSONException e) {
                        Swal.error(context, "Oops!", "Error al realizar la configuración inicial: "+ e);
                    }
                },
                error -> {
                    callback.configuracionInicialFallida(error);
                    Log.i("RESPONSE", error.toString());
                }
        );

        requestQueue.add(jsonObjectRequest);
    }
}
