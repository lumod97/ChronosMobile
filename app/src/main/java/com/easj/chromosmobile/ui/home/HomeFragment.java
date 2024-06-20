package com.easj.chromosmobile.ui.home;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.VolleyError;
import com.easj.chromosmobile.Adapters.TipoMarcacion.AdapterTipoMarcacion;
import com.easj.chromosmobile.AppDatabase;
import com.easj.chromosmobile.ChronosMobile;
import com.easj.chromosmobile.Entitys.Logs;
import com.easj.chromosmobile.Entitys.Marcas;
import com.easj.chromosmobile.Entitys.Personas;
import com.easj.chromosmobile.Interfaces.DAO.LogsDAO;
import com.easj.chromosmobile.Interfaces.DAO.MarcasDAO;
import com.easj.chromosmobile.Interfaces.DAO.PersonasDAO;
import com.easj.chromosmobile.Interfaces.DAO.PuertasDAO;
import com.easj.chromosmobile.Logica.CryptorSJ;
import com.easj.chromosmobile.Logica.Swal;
import com.easj.chromosmobile.MainActivity;
import com.easj.chromosmobile.R;
import com.easj.chromosmobile.SQLProcess.SQLConnection;
import com.easj.chromosmobile.SQLProcess.SQLLogs;
import com.easj.chromosmobile.databinding.FragmentHomeBinding;
import com.easj.chromosmobile.ui.scanner.ScannerViewModel;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements AdapterTipoMarcacion.Callback, SQLConnection.VolleyCallback, SQLLogs.VolleyCallback {

    SharedPreferences sharedPreferences;
    private FragmentHomeBinding binding;
    private Handler handler;
    private Handler handlerConnection;
    MarcasDAO marcasDAO;
    PersonasDAO personasDAO;
    LogsDAO logsDAO;
    NavigationView navigationView;
    String ultimaTransferencia, ultimaDescarga;
    Thread threadConnection;
    boolean ejecutarPing;
    private String horaInicio;
    String tipoMarcacion = "T";
    private Context ctx;
    private SharedPreferences.Editor editor;
    private PuertasDAO puertasDAO;

    private ScannerViewModel scannerViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        scannerViewModel = new ViewModelProvider(requireActivity()).get(ScannerViewModel.class);

        scannerViewModel.getScannedCode().observe(getViewLifecycleOwner(), code -> {
            // Maneja el código escaneado aquí
            if (code != null) {
                // Hacer algo con el código escaneado
                try {
                    String dni = CryptorSJ.desencriptarCadena(code);
                    evaluarMarca(dni);
                    Toast.makeText(requireContext(), "Código escaneado: " + dni, Toast.LENGTH_LONG).show();
                    scannerViewModel.setScannedCode(null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        });


        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(root.getWindowToken(), 0);

        handler = new Handler();

        handlerConnection = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // Aquí puedes manejar la respuesta del ping
                if (msg.what == 1) {
                    boolean isReachable = msg.getData().getBoolean("isReachable");

                }
            }
        };

        binding.inputDNI.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });


        binding.inputDNI.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(binding != null){
                    binding.inputDNI.post(new Runnable() {
                        @Override
                        public void run() {
                            if(binding != null){
                                binding.inputDNI.requestFocus();
                                if(binding.inputDNI.getText().toString().length() >= 8){
                                    tipoMarcacion = "L";
                                    binding.buttonCheck.callOnClick();
                                }
                            }
                        }
                    });
                }
            }
        });

//        PROBAMOS LA CONEXION PARA MOSTRAR EL ICONO DE WIFI
        pingIP();
//        INICIALIZAMOS LA DB
        inicializarDB();
//        INICIALIZAMOS ULTIMA DESCARGA
        ultimaDescarga = logsDAO.obtenerMaximoLog("descargar");
        binding.textLastDownload.setText(ultimaDescarga);
//        INICIALIZAMOS ULTIMA TRANSFERENCIA
        ultimaTransferencia = logsDAO.obtenerMaximoLog("transferir");
        binding.textlastTransfer.setText(ultimaTransferencia);
//        INICIALIZAMOS LAS MARCAS SIN PROCESAR
        obtenerMarcasSinProcesar();
//        INICIALIZAMOS EL RELOJ A MOSTRAR
        iniciarThreadReloj();
        iniciarPingConnection();
//        INICIALIZAMOS LA FECHA A MOSTRAR
        actualizarFecha();
//        INICIALIZAMOS EL PUNTO DE CONTROL
        inicializarPuntoControl();
//        INICIALIZAR ID DISPOSITIVO
        inicializarIdDispositivo();
//        INICIALIZAMOS ITEMS PARA MODAL DE TIPO DE MARCACION
        List<String> listaItems = Arrays.asList("INGRESO", "REFRIGERIO", "RETORNO REFRIGERIO", "SALIDA", "SALIDA AYER");
//        INICIALIZAMOS PERMISOS DE PREFIJO

//        INSERTAR FOTO EN LOADER

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            AnimatedImageDrawable animatedImageDrawable = (AnimatedImageDrawable) getResources().getDrawable(R.drawable.mobile_sync, null);
            // Establece el GIF en el ImageView
            binding.imageSync.setImageDrawable(animatedImageDrawable);
            animatedImageDrawable.start();
        }


        // ABRIR MODAL PARA SELECCIONAR TIPO DE MARCACIÓN
        binding.inputTipoMarcacion.setOnClickListener(view -> {
//            ENVIAMOS LA LISTA DE ITEMS QUE QUEREMOS QUE APAREZCA EN EL MODAL
            Swal.edit(ctx, listaItems, tipoMarcacion -> {
                binding.inputTipoMarcacion.setText(tipoMarcacion);
            }).show();
        });

//        SETEAMOS LOS CLICKS PARA EL BOTÓN DE APROBAR Y ELIMINAR

//        ELIMINAR CARACTERES
        binding.buttonX.setOnClickListener(view -> {
            String textoActual = binding.inputDNI.getText().toString();
            if(textoActual.length() > 0){
                textoActual = textoActual.substring(0, textoActual.length() - 1);
                binding.inputDNI.setText(textoActual);
            }
        });
//        ELIMINAR CARACTERES COMPLETOS MANTENIENDO PRESIONADO EL BOTÓN
        binding.buttonX.setOnLongClickListener(view -> {
            binding.inputDNI.setText("");
            return false;
        });

//        INTENTAR MARCACIÓN
        binding.buttonCheck.setOnClickListener(view -> {
            evaluarMarca(binding.inputDNI.getText().toString());
        });

        binding.btnProcesarMarcas.setOnLongClickListener(view -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_scanner);
            return false;
        });
//        ABRIR MENÚ
        binding.btnProcesarMarcas.setOnClickListener(view -> {

//            ABRIR DRAWER
            MainActivity mainActivity = (MainActivity) getActivity();
            if(mainActivity.obtenerDrawer() != null){
                DrawerLayout dl = mainActivity.obtenerDrawer();
                dl.openDrawer(GravityCompat.START);

                navigationView = mainActivity.obtenerNavigationView();
                MenuItem itemTransferir = navigationView.getMenu().findItem(R.id.nav_transferir);
                MenuItem itemDescargar = navigationView.getMenu().findItem(R.id.nav_descargar);
                MenuItem itemConfiguracion = navigationView.getMenu().findItem(R.id.nav_configuracion);
                MenuItem itemInicio = navigationView.getMenu().findItem(R.id.nav_home);

//                itemInicio.setOnMenuItemClickListener(menuItem -> {
//                    NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
//                    navController.navigate(R.id.nav_home);
//                    return false;
//                });
//                ACCION PARA TRANSFERIR
                itemTransferir.setOnMenuItemClickListener(menuItem -> {
                    if(!binding.tvMarcasSinProcesar.getText().toString().equals("0")) {
                        try {
                            List<Marcas> marcasSinProcesar = marcasDAO.obtenerMarcasSinProcesar();
                            JSONArray marcas = new JSONArray();

                            for(Marcas marca: marcasSinProcesar){
                                JSONObject elemento = new JSONObject();
                                elemento.put("IdMarcacion", marca.getIdMarcacion());
                                elemento.put("Dni", marca.getDni());
                                elemento.put("Fecha", marca.getFecha());
                                elemento.put("Hora", marca.getHora());
                                elemento.put("Accion", marca.getAccion());
                                elemento.put("IdDispositivo", marca.getIdDispositivo());
                                elemento.put("Procesada", marca.getProcesada());
                                elemento.put("IdPuntoControl", marca.getIdPuntoControl());
                                elemento.put("FechaEnvio", marca.getFechaEnvio());
                                elemento.put("TipoMarcacion", marca.getTipoMarcacion());
                                marcas.put(elemento);
                            }
                            JSONObject params = new JSONObject();
                            try {
                                params.put("marcas", marcas);
                                SQLConnection sqlConnection = new SQLConnection();
                                sqlConnection.transferirMarcas(ctx, this, params);
                            } catch (JSONException e) {
                                Log.e("RESPONSE", e.getMessage());
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            Swal.error(ctx, "Oops!", "No se han podido procesar las marcas", 5000);
                        } finally {
                            dl.closeDrawer(GravityCompat.START);
                        }
                    }else {
                        Swal.info(ctx, "Todo está bien.", "No hay marcas para procesar en estos momentos", 5000);
                    }

                    return false;
                });
//                ACCION PARA DESCARGAR
                itemDescargar.setOnMenuItemClickListener(menuItem -> {
                    horaInicio = obtenerFechaHora();
                    mostrarLoaders(true);
                    dl.closeDrawer(GravityCompat.START);
                    obtenerPersonas();
                    return false;
                });
//                ACCION PARA CONFIGURACIO
                itemConfiguracion.setOnMenuItemClickListener(menuItem -> {
//                    if(sharedPreferences.getBoolean("MODO_PACKING", false)){
//                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
//                        navController.navigate(R.id.nav_configuracion);
//                    }else{
//                        Swal.settingsPassword(ctx, (isValid, sweetAlertDialog) -> {
//                            if (isValid) {
//                                sweetAlertDialog.dismissWithAnimation();
//                                dl.closeDrawer(GravityCompat.START);
//                                // Manualmente realiza la navegación al destino nav_configuracion
//                                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
//                                navController.navigate(R.id.nav_configuracion1);
//                            } else {
//                                Swal.warning(ctx, "PROHIBIDO", "La contraseña no es correcta");
//                            }
//                        });
//                    }
                    return false;
                });

            }
        });
        return root;
    }

    private void inicializarIdDispositivo() {
        String idDispositivo = String.valueOf(sharedPreferences.getInt("DEVICE_ID", 0));
        binding.textIdDispositivo.setText(idDispositivo.equals("0") ? "NO CONFIGURADO" : idDispositivo);
    }

    private void inicializarPuntoControl() {
        int idPuntoControl = sharedPreferences.getInt("ID_PUNTO_CONTROL_SELECTED", 0);
        String puntoControl = "0";
        if(idPuntoControl != 0){
            puntoControl = puertasDAO.obtenerDescripcionPuerta(idPuntoControl);
        }
        binding.textPuntoControl.setText(puntoControl.equals("0") ? "NO CONFIGURADO" : puntoControl);
    }

    private void evaluarMarca(String dni){
        int idPuntoControl = sharedPreferences.getInt("ID_PUNTO_CONTROL_SELECTED", 0);
        if(dni.length() == 0){
            Swal.warning(ctx, "Alerta!", "Debe digitar un DNI");
        }else if(binding.inputTipoMarcacion.getText().toString().length() == 0){
            Swal.warning(ctx, "Alerta!", "Debes seleccionar un tipo de marcación antes de realizar una marcación.");
        }else if (idPuntoControl == 0) {
            Swal.warning(ctx, "Advertencia!","NO SE PUEDEN REALIZAR MARCAS POR QUE NO SE HA CONFIGURADO EL PUNTO DE CONTROL, COMUNIQUESE CON EL ÁREA DE SISTEMAS");
        }else {
            String dniMarcado = dni;
            //RETIRAMOS EL SJ DE LA CADENA DE TEXTO A ANALIZAR
//                        OBTENEMOS EL REGISTRO DE LA PERSONA
            String dniBusqueda = dniMarcado;
//                    if(dniBusqueda.substring(0,2).equals("SJ")){
//                        dniBusqueda = dniBusqueda.substring(2);
//                    }
            List<Personas> personasEncontradas = personasDAO.buscarPersona(dniBusqueda);
//                        VALIDAMOS PERMISOS DE LA MARCA A REALIZAR
            switch (validarPermisoMarca(dniMarcado,personasEncontradas)) {
                case 0:
                    Swal.error(ctx, "No permitido", "No está permitido este tipo de marcación");
                    binding.inputDNI.setText("");
                    dni = "";
                    break;
                case 1:
                    binding.tvError.setText("ACCESO PERMITIDO");
                    binding.tvError.setTextColor(getResources().getColor(R.color.success));
                    registrarMarca(dniMarcado, personasEncontradas);
                    break;
                case 2:
                    binding.textNombres.setText(personasEncontradas.get(0).getNombres());
                    binding.textApellidos.setText(personasEncontradas.get(0).getPaterno() + " " + personasEncontradas.get(0).getMaterno());
                    binding.inputDNI.setText("");
                    dni = "";
                    binding.tvError.setText("ACCESO PERMITIDO CON OBSERVACIÓN: " + personasEncontradas.get(0).getObservacion());
                    binding.tvError.setTextColor(getResources().getColor(R.color.warning));
                    registrarMarca(dniMarcado, personasEncontradas);
                    break;
                case 3:
                    String nombre = personasEncontradas.size() > 0 ? personasEncontradas.get(0).getNombres() : "DESCONOCIDO";
                    String apellido = personasEncontradas.size() > 0 ? personasEncontradas.get(0).getPaterno() + " " + personasEncontradas.get(0).getMaterno() : "DESCONOCIDO";
                    String observacion = personasEncontradas.size() > 0 ? personasEncontradas.get(0).getObservacion() : "PERSONAL NO ENCONTRADO, MARCA REALIZADA";
                    binding.textNombres.setText(nombre);
                    binding.textApellidos.setText(apellido);
                    binding.inputDNI.setText("");
                    dni = "";
                    binding.tvError.setText(observacion);
                    binding.tvError.setTextColor(getResources().getColor(R.color.warning));
                    registrarMarca(dniMarcado, personasEncontradas);
                    break;
                case 4:
                    Swal.error(ctx, "Oops!","DEBE INGRESAR UN DNI");
                    break;
                case 5:
                    Swal.error(ctx, "Oops!","FALTAN CARACTERES");
                    break;
                case 6:
                    Swal.error(ctx, "No permitido", "No está permitida la marcación con prefijo");
                default:
                    Swal.error(ctx, "Alerta!", "Ha ocurrido un error desconocido al procesar la marca.");
                    break;
            }
//
            obtenerMarcasSinProcesar();
        }
        tipoMarcacion = "T";
    }
    private void registrarMarca(String dniMarcado, List<Personas> personasEncontradas) {

        String dniBuscar = dniMarcado.substring(0,2).equals("SJ") ? dniMarcado.substring(2) : dniMarcado;
        int accion = obtenerAccion(binding.inputTipoMarcacion.getText().toString());

        boolean marcaExiste = marcasDAO.verificarExistenciaMarca(dniBuscar, obtenerFecha(), accion);

        if(marcaExiste){
            binding.textNombres.setText("");
            binding.textApellidos.setText("");
            binding.tvError.setText("YA HAY UN REGISTRO PARA ESTE TRABAJADOR CON ESTA ACCIÓN.");
            binding.tvError.setTextColor(getResources().getColor(R.color.warning));
        }else{
            int idPuntoControl = sharedPreferences.getInt("ID_PUNTO_CONTROL_SELECTED", 0);
            MediaPlayer mp = obtenerSonido();
            String nombre = personasEncontradas.size() > 0 ? personasEncontradas.get(0).getNombres() : "DESCONOCIDO";
            String apellido = personasEncontradas.size() > 0 ? personasEncontradas.get(0).getPaterno() + " "+ personasEncontradas.get(0).getMaterno() : "DESCONOCIDO";
            binding.textNombres.setText(nombre);
            binding.textApellidos.setText(apellido);

            String tipoMarca = obtenerTipoMarca();
            Marcas nuevaMarca = new Marcas();
            nuevaMarca.setIdMarcacion(sharedPreferences.getInt("DEVICE_ID", 0) + obtenerFechaHoraParaId());
            nuevaMarca.setDni(dniBuscar);
            nuevaMarca.setFecha(obtenerFecha());
            nuevaMarca.setHora(obtenerFechaHora());
            nuevaMarca.setAccion(accion);
            nuevaMarca.setIdDispositivo(String.valueOf(sharedPreferences.getInt("DEVICE_ID", 0)));
            nuevaMarca.setProcesada(0);
            nuevaMarca.setIdPuntoControl(String.valueOf(idPuntoControl));
            nuevaMarca.setFechaEnvio(obtenerFechaHora());
            nuevaMarca.setTipoMarcacion(tipoMarca);
            marcasDAO.insertarMarca(nuevaMarca);

            mp.start();
        }
        binding.inputDNI.setText("");
    }

    private int obtenerAccion(String accionText) {
        int accion = 0;
        switch(accionText){
            case "INGRESO":
                return 1;
            case "REFRIGERIO":
                return 2;
            case "RETORNO REFRIGERIO":
                return 3;
            case "SALIDA":
                return 6;
            case "SALIDA AYER":
                return 0;
        }
        return accion;
    }

    private int validarPermisoMarca(String dniMarcado ,List<Personas> persona) {
//        0 no pasa
//        1 pasa cuando está habilitado
//        2 pasa tod -- PERMITIR TOD
//        3 permitir inactivos
//        4 no hay dni digitado
//        5 faltan caracteres
//        6 contiene prefijo

        //        SE OBTIENE EL TEXTO DEL INPUT PARA SU POSTERIOR PROCESAMIENTO
        boolean permitirPrefijo = sharedPreferences.getBoolean("PERMITIR_PREFIJO", false);
        boolean permitirSinPrefijo = sharedPreferences.getBoolean("PERMITIR_SIN_PREFIJO", false);

        String textDNI = binding.inputDNI.getText().toString();
//        SE EVALÚA LA CANTIDAD DE CARACTERES DE ACUERDO AL VALOR GUARDADO EN SHARED PREFERENCES
//                VARIABLE: ACCEPT_PREFIX

        boolean permitirInactivos = sharedPreferences.getBoolean("PERMITIR_INACTIVOS", false);
        boolean permitirObservados = sharedPreferences.getBoolean("PERMITIR_OBSERVADOS", false);

        int trabajadorStatus = 0;

        if(dniMarcado.length() < 8){
            return 5;
        }
        if(dniMarcado.equals("")){
            return 4;
        }
        if(dniMarcado.substring(0,2).equals("SJ")){
            return 6;
        }


        String observacion = persona.size() > 0 ? persona.get(0).getObservacion() : "PERSONAL NUEVO";
        Log.i("OBS", observacion);
        if(permitirInactivos && !permitirObservados){
            if(observacion.equals("HABILITADO")){
                trabajadorStatus = 1;
            } else if (observacion.equals("PERSONAL NUEVO")) {
                trabajadorStatus = 3;
            }
        } else if(!permitirInactivos && permitirObservados && !observacion.equals("PERSONAL NUEVO")){
            if(observacion.equals("HABILITADO")){
                trabajadorStatus = 1;
            } else if (!observacion.equals("HABILITADO")) {
                trabajadorStatus = 2;
            }
        } else if (!permitirInactivos && observacion.equals("HABILITADO") && persona.size() > 0) {
            trabajadorStatus = 1;
        } else if (permitirInactivos && permitirObservados){
            if(observacion.equals("HABILITADO")){
                trabajadorStatus = 1;
            } else if (observacion.equals("PERSONAL NUEVO")) {
                trabajadorStatus = 3;
            } else if (!observacion.equals("HABILITADO")) {
                trabajadorStatus = 2;
            }
        }

        switch (trabajadorStatus){
            case 1 :
                if(permitirPrefijo && permitirSinPrefijo){
                    if(dniMarcado.length() > 8 && dniMarcado.substring(0,2).equals("SJ")){
                        return 1;
                    }else if(dniMarcado.length() == 8) {
                        return 1;
                    }
                    return 0;
                }else if(dniMarcado.length() > 8 && !dniMarcado.substring(0,2).equals("SJ")){
                    return 0;
                } else if (!permitirPrefijo && permitirSinPrefijo && dniMarcado.length() == 8) {
                    return 1;
                } else if (permitirPrefijo && !permitirSinPrefijo && dniMarcado.substring(0,2).equals("SJ")) {
                    return 1;
                } else  {
                    return 0;
                }
            case 2:
                if(permitirPrefijo && permitirSinPrefijo){
                    if(dniMarcado.length() > 8 && dniMarcado.substring(0,2).equals("SJ")){
                        return 2;
                    }else if(dniMarcado.length() == 8) {
                        return 2;
                    }
                    return 0;
                }else if(dniMarcado.length() > 8 && !dniMarcado.substring(0,2).equals("SJ")){
                    return 0;
                } else if (!permitirPrefijo && permitirSinPrefijo && dniMarcado.length() == 8) {
                    return 2;
                } else if (permitirPrefijo && !permitirSinPrefijo && dniMarcado.substring(0,2).equals("SJ")) {
                    return 2;
                } else  {
                    return 0;
                }
            case 3:
                if(permitirPrefijo && permitirSinPrefijo){
                    if(dniMarcado.length() > 8 && dniMarcado.substring(0,2).equals("SJ")){
                        return 3;
                    }else if(dniMarcado.length() == 8) {
                        return 3;
                    }
                    return 0;
                }else if(dniMarcado.length() > 8 && !dniMarcado.substring(0,2).equals("SJ")){
                    return 0;
                } else if (!permitirPrefijo && permitirSinPrefijo && dniMarcado.length() == 8) {
                    return 3;
                } else if (permitirPrefijo && !permitirSinPrefijo && dniMarcado.substring(0,2).equals("SJ")) {
                    return 3;
                } else  {
                    return 0;
                }
        }
        return 0;
    }

    private MediaPlayer obtenerSonido() {

        MediaPlayer mediaPlayer;
        if(sharedPreferences.getBoolean("VOZ_INFORMACION", false) == false){
            mediaPlayer = MediaPlayer.create(ctx, R.raw.coin);
            return mediaPlayer;
        }

        if(sharedPreferences.getBoolean("MODO_AMIGABLE", false) == true){
            switch (binding.inputTipoMarcacion.getText().toString()){
                case "SALIDA AYER":
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.que_descanses);
                    break;
                case "INGRESO":
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.wellcome);
                    break;
                case "REFRIGERIO":
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.buen_provecho);
                    break;
                case "RETORNO REFRIGERIO":
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.bienvenido_devuelta);
                    break;
                case "SALIDA":
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.que_descanses);
                    break;
                default:
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.coin);
                    break;
            }
        }else {
            switch (binding.inputTipoMarcacion.getText().toString()){
                case "SALIDA AYER":
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.salida);
                    break;
                case "INGRESO":
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.ingreso);
                    break;
                case "REFRIGERIO":
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.refrigerio);
                    break;
                case "RETORNO REFRIGERIO":
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.retorno);
                    break;
                case "SALIDA":
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.salida);
                    break;
                default:
                    mediaPlayer = MediaPlayer.create(ctx, R.raw.coin);
                    break;
            }
        }

        return mediaPlayer;
    }

    private String obtenerTipoMarca() {
        String tipoMarca="";
        if(binding.checkBoxCena.isChecked()){
            tipoMarca = "C";
        }else {
            tipoMarca = tipoMarcacion;
        }
        return tipoMarca;
    }

    private void iniciarPingConnection() {
        ejecutarPing = true;
        threadConnection.start();
    }

    private void insertarLog(String accion, String horaInicio, String condicion, JSONArray logParams) {

        int idPuerta = sharedPreferences.getInt("ID_PUNTO_CONTROL_SELECTED", 0);
        int idDevice = sharedPreferences.getInt("DEVICE_ID", 0);
        String fechaActual = obtenerFechaHora();
        //            INSERTAMOS UN LOG
        Logs logsInsert = new Logs();
        logsInsert.setAccion(accion);
        logsInsert.setDispositivo(idDevice);
        logsInsert.setFecha(fechaActual);
        logsInsert.setPuerta(idPuerta);
        logsInsert.setUsuario("Chronos");
        logsInsert.setDemora(obtenerDiferenciaEntreHoras(horaInicio, obtenerFechaHora()) + " segundos.");
        logsInsert.setCondicion(condicion);
        logsInsert.setTransferido(0);
        logsInsert.setParametros(logParams == null ? "" : logParams.toString());
        logsDAO.insertarLog(logsInsert);

        transferirLogs();
    }

    private String obtenerDiferenciaEntreHoras(String horaInicio, String horaFin){
        String diferencia = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try{
            Date horaFInicio = sdf.parse(horaInicio);
            Date horaFFin = sdf.parse(horaFin);
            long diferenciaSeconds = horaFFin.getTime() - horaFInicio.getTime();
            diferencia = String.valueOf(diferenciaSeconds);
        }catch (ParseException e){
            Log.e("PARSE ERROR", e.toString());
            e.printStackTrace();
        }
        return diferencia;
    }

    private String obtenerFechaHora() {
        //            OBTENEMOS LA FECHA
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
        String formattedDateTime = dateFormat.format(currentDate);
        return formattedDateTime;
    }

    private String obtenerFechaHoraParaId() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
        String formattedDateTime = dateFormat.format(currentDate);
        return formattedDateTime;
    }

    private void mostrarLoaders(boolean b) {
        if(b == true){
            binding.textLastDownload.setVisibility(View.INVISIBLE);
            binding.imageSync.setVisibility(View.VISIBLE);
            binding.progressSync.setVisibility(View.VISIBLE);
        }else{

            String ultimaDescarga = logsDAO.obtenerMaximoLog("descargar");
            binding.textLastDownload.setText(ultimaDescarga);
            binding.textLastDownload.setVisibility(View.VISIBLE);
            binding.imageSync.setVisibility(View.INVISIBLE);
            binding.progressSync.setVisibility(View.INVISIBLE);

        }
    }

    private void obtenerPersonas() {
        try {
//            OBTENEMOS LAS PERSONAS DEL SERVIDOR
            SQLConnection sqlConnection = new SQLConnection();
            sqlConnection.obtenerPersonas(ctx, this);
        }catch (Exception e){
        }
    }

    private boolean eliminarPersonas() {
        try {
    //        ELIMINAMOS LOS REGISTROS DE PERSONAS PARA POBLARLOS CON LO NUEVO
            List<Personas> personas = personasDAO.obtenerPersonas();
            personasDAO.eliminarPersonas(personas);
            return true;
        }catch (Exception e){
            Swal.error(ctx, "Oops!", "Ha ocurrido un error al eliminar las personas, no se puede continuar con la sincronización.");
            return false;
        }
    }

    private void obtenerMarcasSinProcesar() {
        List<Marcas> marcas = marcasDAO.obtenerMarcasSinProcesar();
        String cantidadMarcasSinProcesar = String.valueOf(marcas.size());
        binding.tvMarcasSinProcesar.setText(cantidadMarcasSinProcesar);
    }

    private void inicializarDB() {
        // Obtener la instancia de la base de datos
        AppDatabase db = ChronosMobile.getAppDatabase();
        marcasDAO = db.marcasDAO();
        personasDAO = db.personasDAO();
        logsDAO = db.logsDAO();
        puertasDAO = db.puertasDAO();
    }



//    USAMOS UN FOR EN EL EVENTO ONVIEWCREATED PARA OBTENER EL VALOR DE CADA BOTÓN Y EVITAR HACER METODOS COMPLEJOS BOTÓN POR BOTÓN
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance){
        for(int i = 0; i <= 9; i++){
            int buttonId = getResources().getIdentifier("button_"+i, "id",requireContext().getPackageName());
            Button button = view.findViewById(buttonId);
            button.setOnClickListener(view1 -> {
                if(sharedPreferences.getBoolean("TECLADO_ACTIVADO", false) == false){
                    alertaTecladoInhabilitado();
                }else {
                    tipoMarcacion = "T";
                    agregarNumero(view1);
                }
            });
        }
    }

    private void alertaTecladoInhabilitado() {
        for(int i = 0; i <= 9; i++){
            int buttonId = getResources().getIdentifier("button_"+i, "id",requireContext().getPackageName());
            Button button = getView().findViewById(buttonId);
            button.setBackgroundColor(getResources().getColor(R.color.keyboard_inactive));
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // Cambiar el color de fondo de nuevo después de 1 segundo
                button.setBackground(getResources().getDrawable(R.drawable.button_keyboard));
            }, 100); // 1000 milisegundos = 1 segundo
        }
    }

    public void agregarNumero(View view) {
        Button btnPressed = (Button) view;
        String textoAntiguo = binding.inputDNI.getText().toString();
        String textoNuevo = btnPressed.getText().toString();
        binding.inputDNI.setText(textoAntiguo + textoNuevo);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Actualiza el contenido del TextView aquí
            actualizarReloj();

            // Repite la tarea cada segundo
            handler.postDelayed(this, 1000);
        }
    };

//    METODOS PARA RELOJ
    private String obtenerHora(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(calendar.getTime());
        return currentTime;
    }
    private void actualizarReloj() {
        binding.textClock.setText(obtenerHora());
    }
    private void iniciarThreadReloj() {
        runnable.run();
    }
    private void detenerThreadReloj() {
        handler.removeCallbacks(runnable);
    }

//    METODOS PARA FECHA
    private String obtenerFechaTexto() {
        // Obtiene la fecha actual
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());
        currentDate = currentDate.substring(0, 1).toUpperCase() + currentDate.substring(1).toLowerCase();
        // Actualiza el TextView con la fecha actual
        return currentDate;
    }

    private String obtenerFecha() {
        // Obtiene la fecha actual
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());
        currentDate = currentDate.substring(0, 1).toUpperCase() + currentDate.substring(1).toLowerCase();
        return currentDate;
    }

    private void actualizarFecha(){
        binding.textDate.setText(obtenerFechaTexto());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ctx = context;
        sharedPreferences = ctx.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        detenerThreadPingConnection();
        detenerThreadReloj();
        binding = null;
    }

    private void detenerThreadPingConnection() {
        ejecutarPing = false;
    }

    @Override
    public void onItemSelected(String tipoMarcacion) {
    }

    @Override
    public void onSuccess(JSONArray result) throws JSONException {
        eliminarPersonas();
        JSONArray jsonArray;
        JSONArray logParams = new JSONArray();
        // Manejar el éxito aquí
        jsonArray = result.getJSONObject(0).getJSONArray("response");
//        for( runner ; condicion de ejecucion ; hace mientras no se cumpla la condicion)

        for(int i = 0 ; i < jsonArray.length() ; i++ ){
//            CAPTURAMOS DNI Y ESTADO DE LA PERSONA PARA LOS LOGS
            JSONObject jsonPersonasParams = new JSONObject();
            Personas persona = new Personas();
                persona.setDni(jsonArray.getJSONObject(i).getString("Dni"));
                persona.setNombres(jsonArray.getJSONObject(i).getString("Nombres"));
                persona.setPaterno(jsonArray.getJSONObject(i).getString("Paterno"));
                persona.setMaterno(jsonArray.getJSONObject(i).getString("Materno"));
                persona.setPlanilla(jsonArray.getJSONObject(i).getString("IDPLANILLA"));
                persona.setObservacion(jsonArray.getJSONObject(i).getString("OBSERVACION"));
                personasDAO.insertarPersonas(persona);

//                AGREGAMOS LOS ELEMENTOS AL OBJETO DE LOGS SOLO LOS QUE ESTÁN CON OBSERVACIÓN
                if(!jsonArray.getJSONObject(i).getString("OBSERVACION").equals("HABILITADO")){
                    jsonPersonasParams.put("Dni", jsonArray.getJSONObject(i).getString("Dni"));
//                AGREGAMOS EL OBJETO AL ARRAY
                    logParams.put(jsonPersonasParams);
                }
        }

        Log.i("AEA", String.valueOf(logParams.toString().length()));

        if(jsonArray.length() == personasDAO.obtenerCantidadPersonas()){
            Swal.success(ctx, "CORRECTO!", "Se han insertado todos los registros obtenidos!", 5000);
            mostrarLoaders(false);
            insertarLog("descargar personas", obtenerFechaHora(), "exito", logParams);
        } else if (personasDAO.obtenerCantidadPersonas() == 0) {
            Swal.error(ctx, "Oops!", "NO SE HAN INSERTADO REGISTROS, VUELVA A INTENTAR!");
            insertarLog("descargar personas", obtenerFechaHora(), "error", logParams);
        } else {
            Swal.warning(ctx, "AVISO!", "Se han insertado:"+personasDAO.obtenerCantidadPersonas()+ " registros de: "+jsonArray.length()+".");
            insertarLog("descargar personas", obtenerFechaHora(), "faltantes", logParams);
        }
    }

    @Override
    public void onError(Exception error) {
        // Manejar el error aquí
        Log.i("onError", String.valueOf(error));
        mostrarLoaders(false);
        insertarLog("descargar personas", obtenerFechaHora(), "error", new JSONArray());
        Swal.error(ctx, "Oops!", "NO SE HAN INSERTADO REGISTROS, ERROR DE SERVIDOR");
    }

    @Override
    public void transferenciaCorrecta(JSONArray result) throws JSONException {
        marcasDAO.procesarMarcas();
        Swal.success(ctx, "Perfecto!", "Las marcas se han procesado correctamente", 5000);
        insertarLog("transferir marcas", obtenerFechaHora(), "exito", new JSONArray());
        String ultimaTransferencia = logsDAO.obtenerMaximoLog("transferir");
        binding.textlastTransfer.setText(ultimaTransferencia);
        obtenerMarcasSinProcesar();
    }

    private void transferirLogs() {
        List<Logs> logsObtenidos = logsDAO.obtenerLogsSinProcesar();
        JSONArray logs = new JSONArray();

        for(Logs log: logsObtenidos){
            JSONObject elemento = new JSONObject();
            try {
                elemento.put("accion", log.getAccion());
                elemento.put("fecha", log.getFecha());
                elemento.put("usuario", log.getUsuario());
                elemento.put("puerta", log.getPuerta());
                elemento.put("dispositivo", log.getDispositivo());
                elemento.put("demora", log.getDemora());
                elemento.put("condicion", log.getCondicion());

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            logs.put(elemento);
        }

        JSONObject params = new JSONObject();
        try {
            params.put("logs", logs);
            SQLLogs sqlLogs = new SQLLogs();
            sqlLogs.transferirLogs(ctx, this, params);
        } catch (JSONException e) {
            Log.e("RESPONSE", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void transferenciaFallida(Exception error) {
        insertarLog("transferir marcas", obtenerFechaHora(), "error", new JSONArray());
        Swal.error(ctx, "Oops!", "La transferencia no se ha realizado.");
        Log.e("ERROR LOGS", error.toString());
    }

    @Override
    public void configuracionInicialObtenida(JSONArray result) throws JSONException {

    }

    @Override
    public void configuracionInicialFallida(Exception error) {

    }

    private void pingIP() {
        threadConnection = new Thread(new Runnable() {
            @Override
            public void run() {
                while (ejecutarPing) {
                    try {
                        if(binding != null){
                            InetAddress address = InetAddress.getByName("192.168.30.99");
                            boolean isReachable = address.isReachable(3000);  // Timeout de 3 segundos

                            // Envía el resultado al handler en el hilo principal
                            Message message = new Message();
                            message.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isReachable", isReachable);
                            message.setData(bundle);
                            handlerConnection.sendMessage(message);
                            if(isReachable == true){
                                Drawable drawable = ContextCompat.getDrawable(ctx,R.drawable.ic_wifi);
                                drawable = DrawableCompat.wrap(drawable).mutate();
                                DrawableCompat.setTint(drawable, getResources().getColor(R.color.success));
                                binding.imageConnection.setImageDrawable(drawable);
                            }else{
                                Drawable drawable = ContextCompat.getDrawable(ctx,R.drawable.ic_no_wifi);
                                drawable = DrawableCompat.wrap(drawable).mutate();
                                DrawableCompat.setTint(drawable, getResources().getColor(R.color.error));
                                binding.imageConnection.setImageDrawable(drawable);
                            }
                            Thread.sleep(3000);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    } catch (Throwable e) {
                        Log.e("PING ERROR", e.toString());
                    }
                }
            }
        });
    }

    @Override
    public void transferenciaLogFallida(VolleyError error) {
        Log.e("ERROR LOGS", error.toString());
    }

    @Override
    public void transferenciaLogCorrecta(JSONArray response) throws JSONException {
        Log.i("RESPONSE LOGS", response.toString());
        logsDAO.procesarLogs();
    }
}