package com.example.javalearnlab.utils;

import android.content.Context;

public class ServerManager {

    private static final String PREF = "server_settings";
    private static final String KEY_IP = "server_ip";

    public static void saveIP(Context context, String ip) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_IP, ip)
                .apply();
    }

    public static String getBaseUrl(Context context) {
        String ip = context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString(KEY_IP, "10.0.2.2:8080");

        if (!ip.startsWith("http://") && !ip.startsWith("https://")) {
            ip = "http://" + ip;
        }
        return ip;
    }

    // Для отображения в настройках (без http://)
    public static String getRawIp(Context context) {
        return context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString(KEY_IP, "10.0.2.2:8080");
    }
}