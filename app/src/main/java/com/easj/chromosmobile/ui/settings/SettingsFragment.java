package com.easj.chromosmobile.ui.settings;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.VolleyError;
import com.easj.chromosmobile.Adapters.PuntosControl.AdapterPuntosControl;
import com.easj.chromosmobile.AppDatabase;
import com.easj.chromosmobile.ChronosMobile;
import com.easj.chromosmobile.DeviceProcess.MacAddressHelper;
import com.easj.chromosmobile.Entitys.Acciones;
import com.easj.chromosmobile.Entitys.Puertas;
import com.easj.chromosmobile.Entitys.Terminales;
import com.easj.chromosmobile.Interfaces.DAO.AccionesDAO;
import com.easj.chromosmobile.Interfaces.DAO.PuertasDAO;
import com.easj.chromosmobile.Interfaces.DAO.TerminalesDAO;
import com.easj.chromosmobile.Logica.Swal;
import com.easj.chromosmobile.R;
import com.easj.chromosmobile.SQLProcess.SQLConnection;
import com.easj.chromosmobile.SQLProcess.SQLRegisterTerminal;
import com.easj.chromosmobile.databinding.FragmentSettingsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment implements SQLConnection.VolleyCallback, SQLRegisterTerminal.VolleyCallback {

    private FragmentSettingsBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context ctx;
    private PuertasDAO puertasDao;
    private TerminalesDAO terminalesDao;
    private AccionesDAO accionesDAO;
    private DrawerLayout drawer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
//        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
//        final TextView textView = binding.textGallery;
//        settingsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View viewPrincipal, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(viewPrincipal, savedInstanceState);

        inicializarDAOS();
        if(!sharedPreferences.getBoolean("NUEVA_VERSION", false)){
            editor.remove("LONGITUD_DNI").apply();
            editor.putInt("LONGITUD_DNI", 8).apply();
            editor.putBoolean("NUEVA_VERSION", true).apply();
        }
        boolean dispositivoConfigurado = sharedPreferences.getBoolean("DISPOSITIVO_CONFIGURADO", false);
        setItemsPuntoControl();
        if(!!dispositivoConfigurado){
            setDeviceNameIfExists();
        }

//        ESTO SE VA A PONER PARA LA ACTUALIZACIÓN, YA QUE ANTERIORMENTE EL CAMPO SE ESTABA TRATANDO COMO UN STRING PERO AHORA ES UN
//                ENTERO, POR ELLO SE OBTIENE EL VALOR, SE BORRA Y LUEGO SE SETEA EL VALOR COMO UN ENTERO
        inicializarSwitchs();

        binding.clConfiguracionInicial.setOnTouchListener((view, motionEvent) -> {
            view.setBackgroundColor(getResources().getColor(R.color.selected_row_color));
            sincronizarData();

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // Cambiar el color de fondo de nuevo después de 1 segundo
                view.setBackgroundColor(getResources().getColor(cn.pedant.SweetAlert.R.color.float_transparent));
            }, 300); // 1000 milisegundos = 1 segundo


            return false;
        });

        binding.spinnerPuntoControl.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(binding.spinnerPuntoControl.getSelectedView() != null){
                    TextView textView = binding.spinnerPuntoControl.getSelectedView().findViewById(R.id.text1);
                    int idPuntoControlSelected = parseInt(textView.getText().toString());
                    sharedPreferences.edit().putInt("ID_PUNTO_CONTROL_SELECTED", idPuntoControlSelected).apply();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        boolean productionDB = sharedPreferences.getBoolean("USE_PRODUCTION_DB", false);
        binding.rbProduccion.setChecked(!productionDB);
        binding.rbPruebas.setChecked(productionDB);

        if(binding.rgDB.getCheckedRadioButtonId() == R.id.rbProduccion){
            editor.putBoolean("USE_PRODUCTION_DB", true).apply();
        } else if (binding.rgDB.getCheckedRadioButtonId() == R.id.rbPruebas) {
            editor.putBoolean("USE_PRODUCTION_DB", false).apply();
        }

        binding.buttonDetallesDispositivo.setOnClickListener(view -> {
            String deviceName = binding.inputDetallesDispositivo.getText().toString();
            if(deviceName.equals("")){
                Swal.warning(ctx, "Cuidado!", "El campo está vacio.");
            }else {
                editor.putString("DEVICE_NAME", binding.inputDetallesDispositivo.getText().toString()).apply();
                try {
                    registrarDispositivo();
                } catch (JSONException e) {
                    Swal.error(ctx, "Oops!", "Error al registrar el dispositivo");
                }
            }
        });

        binding.rgDB.setOnCheckedChangeListener((radioGroup, i) -> {
            if(binding.rgDB.getCheckedRadioButtonId() == R.id.rbProduccion){
                editor.putBoolean("USE_PRODUCTION_DB", true).apply();
            } else if (binding.rgDB.getCheckedRadioButtonId() == R.id.rbPruebas) {
                editor.putBoolean("USE_PRODUCTION_DB", false).apply();
            }
        });

        binding.buttonBack.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_home);
        });

        binding.textBack.setOnTouchListener((view, motionEvent) -> {
            binding.buttonBack.callOnClick();
            return false;
        });

        binding.layoutBack.setOnTouchListener((view, motionEvent) -> {
            binding.buttonBack.callOnClick();
            return false;
        });

        binding.switchVoz.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("VOZ_INFORMACION", b).apply();
        });
        binding.switchTeclado.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("TECLADO_ACTIVADO", b).apply();
        });
        binding.switchPermitirEncriptado.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("PERMITIR_ENCRIPTADO", b).apply();
        });
        binding.switchMostrarScanner.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("MOSTRAR_SCANNER", b).apply();
        });
        binding.switchAlternarCamara.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("USAR_CAMARA_FRONTAL", b).apply();
        });
        binding.switchPermitirPrefijo.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("PERMITIR_PREFIJO", b).apply();
        });
        binding.switchPermitirSinPrefijo.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("PERMITIR_SIN_PREFIJO", b).apply();
        });
        binding.switchInactivos.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("PERMITIR_INACTIVOS", b).apply();
        });
        binding.switchObservados.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("PERMITIR_OBSERVADOS", b).apply();
        });
        binding.switchModoPacking.setOnCheckedChangeListener((compoundButton, b) -> {
            editor.putBoolean("MODO_PACKING", b).apply();
        });
        binding.inputLongitudDNI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String texto = binding.inputLongitudDNI.getText().toString();
                if(!TextUtils.isEmpty(texto)){
                    editor.putInt("LONGITUD_DNI", Integer.parseInt(texto)).apply();
                }
            }
        });
    }

    private void inicializarSwitchs() {
        binding.switchVoz.setChecked(sharedPreferences.getBoolean("VOZ_INFORMACION",false));
        binding.switchTeclado.setChecked(sharedPreferences.getBoolean("TECLADO_ACTIVADO",false));
        binding.switchPermitirEncriptado.setChecked(sharedPreferences.getBoolean("PERMITIR_ENCRIPTADO", false));
        binding.switchPermitirPrefijo.setChecked(sharedPreferences.getBoolean("PERMITIR_PREFIJO",false));
        binding.switchPermitirSinPrefijo.setChecked(sharedPreferences.getBoolean("PERMITIR_SIN_PREFIJO",false));
        binding.switchInactivos.setChecked(sharedPreferences.getBoolean("PERMITIR_INACTIVOS",false));
        binding.switchObservados.setChecked(sharedPreferences.getBoolean("PERMITIR_OBSERVADOS",false));
        binding.switchModoPacking.setChecked(sharedPreferences.getBoolean("MODO_PACKING",false));
        binding.inputLongitudDNI.setText(String.valueOf(sharedPreferences.getInt("LONGITUD_DNI", 8)));
        binding.switchMostrarScanner.setChecked(sharedPreferences.getBoolean("MOSTRAR_SCANNER", false));
        binding.switchAlternarCamara.setChecked(sharedPreferences.getBoolean("USAR_CAMARA_FRONTAL", false));
    }

    private void registrarDispositivo() throws JSONException {
        JSONObject params = new JSONObject();
        params.put("mac", MacAddressHelper.getMacAddress(ctx));
        params.put("ip", MacAddressHelper.getIpAddress(ctx));
        params.put("descripcion", sharedPreferences.getString("DEVICE_NAME","NO_NAME"));
        params.put("id_puerta", sharedPreferences.getInt("ID_PUNTO_CONTROL_SELECTED", 0));
        params.put("tipo", "TBL");

        SQLRegisterTerminal sqlRegisterTerminal = new SQLRegisterTerminal();
        sqlRegisterTerminal.registrarDispositivo(ctx, this, params);
    }

    private void sincronizarData() {
        JSONObject params = new JSONObject();
        SQLConnection sqlConnection = new SQLConnection();
        sqlConnection.sincronizacionInicial(ctx, this, params);
    }

    private void inicializarDAOS() {
        AppDatabase db = ChronosMobile.getAppDatabase();
        puertasDao = db.puertasDAO();
        terminalesDao = db.terminalesDAO();
        accionesDAO = db.accionesDAO();
    }

    public List<Pair<Integer, String>> obtenerPuntosControl() {
        List<Pair<Integer, String>> listKeyValue = new ArrayList<>();
        List<Puertas> puertas = puertasDao.obtenerPuertas();
        for (Puertas puerta : puertas) {
            Pair<Integer, String> puertaKeyValue = new Pair<>(puerta.getId(), puerta.getDescripcion());
            listKeyValue.add(puertaKeyValue);
            Log.i("PUERTA", puertaKeyValue.toString());
        }
        return listKeyValue;
    }

    public void setItemsPuntoControl(){
        AdapterPuntosControl adapterPuntosControl = new AdapterPuntosControl(getContext(), obtenerPuntosControl());
        binding.spinnerPuntoControl.setAdapter(adapterPuntosControl);
        if(adapterPuntosControl.getCount() > 0){
            binding.spinnerPuntoControl.setSelection(adapterPuntosControl.getPositionItemAt(sharedPreferences.getInt("ID_PUNTO_CONTROL_SELECTED", 0)));
        }
    }

    private void setDeviceNameIfExists(){
        String deviceName = sharedPreferences.getString("DEVICE_NAME", "!DEVICE_NAME");
        if(deviceName.equals("!DEVICE_NAME")){
            String deviceNameSQL = terminalesDao.obtenerDeviceName(MacAddressHelper.getMacAddress(ctx));
            if(deviceNameSQL != null) {
                editor.putString("DEVICE_NAME", deviceNameSQL).apply();
                binding.inputDetallesDispositivo.setText(deviceNameSQL);
            }
        }else{
            binding.inputDetallesDispositivo.setText(deviceName);
        }
    }
    private void setDeviceIdIfExists(){
        int deviceId = terminalesDao.obtenerDeviceId(MacAddressHelper.getMacAddress(ctx));
        if(deviceId != 0){
            editor.putInt("DEVICE_ID", deviceId).apply();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        SETEAMOS EN FALSE CUANDO SE DESTRUYE EL FRAGMENT PARA QUE VUELVA A PEDIR LA CONTRASEÑA
        ChronosMobile.passPassed = false;
        binding = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
        sharedPreferences = ctx.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @Override
    public void onSuccess(JSONArray result) throws JSONException {}

    @Override
    public void onError(Exception error) {}

    @Override
    public void transferenciaCorrecta(JSONArray result) throws JSONException {}

    @Override
    public void transferenciaFallida(Exception error) {}

    @Override
    public void configuracionInicialObtenida(JSONArray result) throws JSONException {

        JSONObject response = result.getJSONObject(0).getJSONObject("response");

        JSONArray dataPuertas = response.getJSONArray("data_puertas");
        JSONArray dataTerminales = response.getJSONArray("data_terminales");
        JSONArray dataAcciones = response.getJSONArray("data_acciones");

        if(dataPuertas.length() > 0){
            puertasDao.eliminarPuertas(puertasDao.obtenerPuertas());
        }
        if(dataTerminales.length() > 0){
            terminalesDao.eliminarTerminales(terminalesDao.obtenerTerminales());
        }
        if(dataAcciones.length() > 0){
            accionesDAO.eliminarAcciones(accionesDAO.obtenerAcciones());
        }
//        INSERTAMOS PUERTAS
        for(int i = 0 ; i < dataPuertas.length() ; i++ ){
//            INSTANCIAMOS UNA NUEVA PUERTA
            Puertas puerta = new Puertas();
//            INSTANCIAMOS UN OBJETO PARA RECORRER LOS RESULTADOS OBTENIDOS DE SQL
            JSONObject jsonObject = dataPuertas.getJSONObject(i);
//            SETEAMOS CARACTERÍSTICAS
            puerta.setId(jsonObject.getInt("Id"));
            puerta.setDescripcion(jsonObject.getString("Descripcion"));
            puerta.setIdEstado(jsonObject.getString("IdEstado"));
            puerta.setDniUsuarioCrea(jsonObject.getString("DniUsuarioCrea"));
            puerta.setFechaHoraCreacion(jsonObject.getString("FechaHoraCreacion"));
            puerta.setDniUsuarioActualiza(jsonObject.getString("DniUsuarioActualiza"));
            puerta.setFechaHoraActualizacion(jsonObject.getString("FechaHoraActualizacion"));
//            INSERTAMOS PUERTA
            puertasDao.insertarPuerta(puerta);
            Log.i("PUERTAS","OK");
        }

//        INSERTAMOS TERMINALES
        for(int i = 0 ; i < dataTerminales.length() ; i++ ){
//            INSTANCIAMOS UN NUEVO TERMINAL
            Terminales terminal = new Terminales();
//            INSTANCIAMOS UN OBJETO PARA RECORRER LOS RESULTADOS OBTENIDOS DE SQL
            JSONObject jsonObject = dataTerminales.getJSONObject(i);
//            SETEAMOS CARACTERÍSTICAS
            terminal.setId(jsonObject.getInt("Id"));
            terminal.setMac(jsonObject.getString("Mac"));
            terminal.setIp(jsonObject.getString("Ip"));
            terminal.setDescripcion(jsonObject.getString("Descripcion"));
            terminal.setIdPuerta(jsonObject.getInt("IdPuerta"));
            terminal.setFechaRegistro(jsonObject.getString("FechaRegistro"));
            terminal.setFechaActualiza(jsonObject.getString("FechaActualiza"));
            terminal.setIdEstado(jsonObject.getString("IdEstado"));
            terminal.setTipo(jsonObject.getString("Tipo"));
//            INSERTAMOS TERMINAL
            terminalesDao.insertarTerminales(terminal);
            Log.i("TERMINALES","OK");
        }

//        INSERTAMOS ACCIONES
        for(int i = 0 ; i < dataAcciones.length() ; i ++){
//            INSTANCIAMOS UNA NUEVA ACCION
            Acciones accion = new Acciones();
//            INSTANCIAMOS UN OBJETO PARA RECORRER LOS RESULTADOS OBTENIDOS DE SQL
            JSONObject jsonObject = dataAcciones.getJSONObject(i);
//            SETEAMOS CARACTERISTICAS
            accion.setId(jsonObject.getInt("Id"));
            accion.setDescripcion(jsonObject.getString("Dex"));
//            INSERTAMOS ACCION
            accionesDAO.insertarAccion(accion);
            Log.i("ACCIONES","OK");
        }

        if(dataPuertas.length() != puertasDao.obtenerCantidadPuertas()){
            Swal.warning(ctx, "Alerta!", "No se ha insertado todas las puertas obtenidas");
        } else if(dataTerminales.length() != terminalesDao.obtenerCantidadTerminales()){
            Swal.warning(ctx, "Alerta!", "No se ha insertado todos los terminales obtenidos");
            setItemsPuntoControl();
        } else if(dataPuertas.length() == puertasDao.obtenerCantidadPuertas() && dataTerminales.length() == terminalesDao.obtenerCantidadTerminales()){
            Swal.success(ctx, "Éxito!", "Se han insertado todos los registros obtenidos", 5000);
            editor.putBoolean("DISPOSITIVO_CONFIGURADO", true).apply();
            setItemsPuntoControl();
            setDeviceNameIfExists();
            setDeviceIdIfExists();
        }
    }

    @Override
    public void configuracionInicialFallida(Exception error) {
        Log.e("ERROR REGISTRO", error.toString());
    }

    @Override
    public void registroFallido(VolleyError error) {
        Log.e("ERROR", error.toString());
        Swal.error(ctx, "Oops!", error.getMessage());
    }


    @Override
    public void registroExitoso(JSONArray result) throws JSONException {
            JSONObject response = result.getJSONObject(0).getJSONObject("response");
            int responseCode = result.getJSONObject(0).getInt("code");
        if(responseCode == 208){
            Swal.warning(ctx,"Alerta", response.getString("message"));
            editor.putInt("DEVICE_ID", parseInt(response.getString("deviceId"))).apply();
        }else{
            Swal.success(ctx, "Éxito!", response.getString("message"), 5000);
    //        ALMACENAMOS EL ID OBTENIDO DESDE EL SERVIDOR
            editor.putInt("ID_TERMINAL", response.getInt("idInserted")).apply();
        }
    }
}