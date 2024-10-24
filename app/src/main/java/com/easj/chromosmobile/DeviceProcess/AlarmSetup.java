package com.easj.chromosmobile.DeviceProcess;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class AlarmSetup {

    private static OnTime onTimeCallback;

    public interface OnTime {
        void onTime();
    }

    public static void scheduleHourlyAlarm(Context context, OnTime onTime) {
        // Configura el AlarmManager
        onTimeCallback = onTime;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, HourlyAlarmReceiver.class);

        // Usa FLAG_IMMUTABLE para Android 31+
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Configura el inicio para la próxima hora exacta
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        // Si ya pasó el minuto 0, ajusta para la próxima hora
        if (calendar.get(Calendar.MINUTE) != 0 || calendar.get(Calendar.SECOND) != 0) {
            // Configura el calendario para la próxima hora exacta
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.MINUTE, 0);  // Minuto 0 de la próxima hora
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }

        // Log para saber cuándo se programa la alarma
        Log.i("AlarmSetup", "La alarma se programará a las: " + calendar.getTime());

        // Repetir cada 1 hora (3600000 ms)
        long intervalMillis = AlarmManager.INTERVAL_HOUR;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pendingIntent);
    }

    // Método para acceder al callback desde el HourlyAlarmReceiver
    public static OnTime getOnTimeCallback() {
        return onTimeCallback;
    }
}

//
////ESTO ES PARA PROBAR CADA 2 MINUTOS
//
//package com.easj.chromosmobile.DeviceProcess;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//import java.util.Calendar;
//
//public class AlarmSetup {
//
//    private static OnTime onTimeCallback;
//
//    public interface OnTime {
//        void onTime();
//    }
//
//    public static void scheduleHourlyAlarm(Context context, OnTime onTime) {
//        // Configura el AlarmManager
//        onTimeCallback = onTime;
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, HourlyAlarmReceiver.class);
//
//        // Usa FLAG_IMMUTABLE para Android 31+
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                context,
//                0,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
//        );
//
//        // Configura el inicio para el próximo minuto exacto
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//
//        // Si ya pasó el minuto 0, ajusta para el próximo minuto
//        if (calendar.get(Calendar.SECOND) != 0 || calendar.get(Calendar.MILLISECOND) != 0) {
//            // Configura el calendario para el próximo minuto exacto
//            calendar.add(Calendar.MINUTE, 1);
//            calendar.set(Calendar.SECOND, 0);  // Segundo 0 del próximo minuto
//            calendar.set(Calendar.MILLISECOND, 0);
//        }
//
//        // Log para saber cuándo se programa la alarma
//        Log.i("AlarmSetup", "La alarma se programará a las: " + calendar.getTime());
//
//        // Repetir cada 2 minutos (120000 ms)
//        long intervalMillis = 2 * 60 * 1000; // 2 minutos en milisegundos
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), intervalMillis, pendingIntent);
//    }
//
//    // Método para acceder al callback desde el TwoMinuteAlarmReceiver
//    public static OnTime getOnTimeCallback() {
//        return onTimeCallback;
//    }
//}
