package com.example.javalearnlab.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {

    private static final String PREF = "user";

    public static void saveUser(Context context, String nickname) {
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        prefs.edit().putString("nickname", nickname).apply();
    }

    public static String getUser(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return prefs.getString("nickname", null);
    }

    public static boolean isLogged(Context context) {
        return getUser(context) != null;
    }

    public static void logout(Context context) {
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
