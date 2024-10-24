package com.easj.chromosmobile.DeviceProcess;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HourlyAlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        // Aquí pones la acción que se debe realizar cada hora
        Log.i("EJECUTAO", "FINASO");
//        Swal.info(context, "FINO", "SONANDO PE MI KING", 5000);3
        // Ejecutar en el UI thread
        AlarmSetup.OnTime onTimeCallback = AlarmSetup.getOnTimeCallback();
        if (onTimeCallback != null) {
            Log.i("EJECUTAR ALARMA", "FINO");
            onTimeCallback.onTime();
        } else {
            Log.w("HourlyAlarmReceiver", "No hay callback definido");
        }
    }
}
