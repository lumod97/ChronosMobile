package com.easj.chromosmobile.Logica;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.easj.chromosmobile.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
public class SwalDates {
    public interface onDateSelected {
        void onDateSelected(String dateSelected, String fechaSeleccionada, SweetAlertDialog sweetAlertDialog);
    }
    public static void SpinnerDate(Context ctx, String fechaSeleccionada, SwalDates.onDateSelected callback) {
        Dates dates = new Dates();
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(ctx);
        View custom = LayoutInflater.from(ctx).inflate(R.layout.custom_calendar_spinner, null);
        sweetAlertDialog.setCustomView(custom);

        NumberPicker pickerYear = custom.findViewById(R.id.pickerYear);
        NumberPicker pickerMonth = custom.findViewById(R.id.pickerMonth);
        NumberPicker pickerDay = custom.findViewById(R.id.pickerDay);

        pickerYear.setMinValue(2020);
        pickerYear.setMaxValue(2030);
        pickerYear.setValue( dates.obtenerFechaActual().get(0));

        pickerMonth.setMinValue(1);
        pickerMonth.setMaxValue(12);
        pickerMonth.setValue(dates.obtenerFechaActual().get(1) + 1);

        pickerDay.setMinValue(1);
        pickerDay.setMaxValue(calcularDiaMaximo(String.valueOf(pickerYear.getValue()), String.valueOf(pickerMonth.getValue())));
        pickerDay.setValue(dates.obtenerFechaActual().get(2));

        pickerYear.setOnValueChangedListener((numberPicker, i, i1) -> {
            pickerDay.setMaxValue(calcularDiaMaximo(String.valueOf(pickerYear.getValue()), String.valueOf(pickerMonth.getValue())));
        });

        pickerMonth.setOnValueChangedListener((numberPicker, i, i1) -> {
            pickerDay.setMaxValue(calcularDiaMaximo(String.valueOf(pickerYear.getValue()), String.valueOf(pickerMonth.getValue())));
        });

        sweetAlertDialog.show();
        sweetAlertDialog.setConfirmClickListener(sweetAlertDialog1 -> {
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                // Concatenar los strings para formar una fecha en formato "yyyyMMdd"
                String dateString = String.valueOf(pickerYear.getValue()) + String.format("%02d",pickerMonth.getValue()) + String.format("%02d",pickerDay.getValue());

                // Formato de entrada: "yyyyMMdd"
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

                // Convertir el string de fecha a un objeto Date
                Date date = inputFormat.parse(dateString);

                String dateStringFormat =
                        String.valueOf(pickerYear.getValue()) + "-" +
                        String.valueOf(String.format("%02d", pickerMonth.getValue())) + "-" +
                        String.valueOf(String.format("%02d",pickerDay.getValue()));

                Log.i("DATE", outputFormat.format(date));
                if (callback != null) {
                    callback.onDateSelected(dateStringFormat, fechaSeleccionada,  sweetAlertDialog);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }finally {
                sweetAlertDialog.dismissWithAnimation();
            }

        });

        sweetAlertDialog.show();
    }

    private static int calcularDiaMaximo(String year, String month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(parseInt(year), parseInt(month) - 1, 1);
        int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        return maxDay;
    }

}
