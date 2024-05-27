package com.easj.chromosmobile.DeviceProcess;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;

public class MacAddressHelper {
    public static String getMacAddress(Context context) {
            // Intenta obtener el Android ID
            String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return androidId.substring(0, 12);
    }
    public static String getIpAddress(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connManager != null) {
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    return getWifiIpAddress(context);
                }
            }
        }

        return null;
    }

    private static String getWifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();

            // Formatea la dirección IP en formato legible
            return String.format("%d.%d.%d.%d",
                    (ipAddress & 0xff),
                    (ipAddress >> 8 & 0xff),
                    (ipAddress >> 16 & 0xff),
                    (ipAddress >> 24 & 0xff));
        }

        return null;
    }
}
