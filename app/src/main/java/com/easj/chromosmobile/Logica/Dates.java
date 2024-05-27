package com.easj.chromosmobile.Logica;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Dates {
    public List<Integer> obtenerFechaActual() {
        List<Integer> hola = new ArrayList<>();
        long fechaActual = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(fechaActual);
        int year, month, day;
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hola.add(0, year);
        hola.add(1, month);
        hola.add(2, day);

        return hola;
    }
}
