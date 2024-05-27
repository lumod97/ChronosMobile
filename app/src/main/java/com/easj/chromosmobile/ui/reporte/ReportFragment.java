package com.easj.chromosmobile.ui.reporte;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easj.chromosmobile.Adapters.AdapterReporteMarcas.AdapterReporteMarcas;
import com.easj.chromosmobile.AppDatabase;
import com.easj.chromosmobile.ChronosMobile;
import com.easj.chromosmobile.Entitys.ReporteMarcas;
import com.easj.chromosmobile.Interfaces.DAO.MarcasDAO;
import com.easj.chromosmobile.Logica.Dates;
import com.easj.chromosmobile.Logica.Swal;
import com.easj.chromosmobile.Logica.SwalDates;
import com.easj.chromosmobile.Logica.SwalEstadoTransferido;
import com.easj.chromosmobile.R;
import com.easj.chromosmobile.databinding.FragmentReportBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ReportFragment extends Fragment implements SwalDates.onDateSelected, SwalEstadoTransferido.onEstadoSelected{
    private FragmentReportBinding binding;
    private MarcasDAO marcasDAO;
    private int idEstadoTransferido = 0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ReportViewModel reportViewModel =
                new ViewModelProvider(this).get(ReportViewModel.class);

        binding = FragmentReportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Dates dates = new Dates();

        binding.buttonBack.setOnClickListener(view -> {
            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
            navController.navigate(R.id.nav_home);
        });

        binding.textDiaSeleccionado.setOnClickListener(view -> {
            SwalDates.SpinnerDate(getContext(), "desde", this);
        });

//        SETEAR TEXTVIEW FECHA DESDE
        binding.textDiaSeleccionado.setText(dates.obtenerFechaActual().get(0) + "-" + String.format("%02d",dates.obtenerFechaActual().get(1) + 01) + "-" + String.format("%02d",dates.obtenerFechaActual().get(2)));
        setRecyclerView();

        binding.layoutBack.setOnTouchListener((view, motionEvent) -> {
            binding.buttonBack.callOnClick();
            return false;
        });

        binding.textBack.setOnTouchListener((view, motionEvent) -> {
            binding.buttonBack.callOnClick();
            return false;
        });

        binding.textDiaSeleccionado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setRecyclerView();
            }
        });

        binding.inputBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setRecyclerView();
            }
        });
        binding.layoutFechaDesde.setOnClickListener(view -> {
            binding.textDiaSeleccionado.callOnClick();
        });
        binding.textView8.setOnClickListener(view -> {
            binding.textDiaSeleccionado.callOnClick();
        });

        binding.textEstado.setOnClickListener(view -> {
            SwalEstadoTransferido.ListEstadoTransferido(getContext(), this);
        });
        setRecyclerView();
        return root;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        inicializarDAOS();
    }

    private void inicializarDAOS() {
        AppDatabase db = ChronosMobile.getAppDatabase();
        marcasDAO = db.marcasDAO();
    }

    private void setRecyclerView(){
        List<ReporteMarcas> reporteMarcasList = marcasDAO.obtenerReporteMarcas(binding.textDiaSeleccionado.getText().toString(), binding.inputBusqueda.getText().toString(), idEstadoTransferido);
        Log.i("CANTIDAD", String.valueOf(reporteMarcasList.size()));
        AdapterReporteMarcas adapterReporteMarcas = new AdapterReporteMarcas(getContext(), reporteMarcasList, new AdapterReporteMarcas.OnItemClickListener(){
            @Override
            public void onItemClick(int position) {
                Log.i("CANTIDAD", String.valueOf(position));
            }
        });
        binding.recyclerMarcas.setAdapter(adapterReporteMarcas);
        binding.recyclerMarcas.setLayoutManager(new LinearLayoutManager(getContext()));
        if(ChronosMobile.isPassPassed()){

        }
        adapterReporteMarcas.notifyDataSetChanged();
    }

    @Override
    public void onDateSelected(String dateSelected, String fechaSeleccionada, SweetAlertDialog sweetAlertDialog) {
        switch (fechaSeleccionada){
            case "desde":
                Log.i("DATE", dateSelected.toString());
                binding.textDiaSeleccionado.setText(dateSelected);
                break;
            case "hasta":
                break;
        }
    }

    @Override
    public void onEstadoSelected(String estadoSeleccionado) {
        idEstadoTransferido = obtenerIdEstado(estadoSeleccionado);
        binding.textEstado.setText(estadoSeleccionado);
        setRecyclerView();
    }

    private int obtenerIdEstado(String estadoSeleccionado) {
        switch (estadoSeleccionado){
            case "TODOS":
                return 2;
            case "TRANSFERIDOS":
                return 1;
            case "SIN TRANSFERIR":
                return 0;
        }
        return 0;
    }
}
