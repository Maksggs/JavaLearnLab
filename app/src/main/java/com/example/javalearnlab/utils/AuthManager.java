package com.example.javalearnlab.utils;

import android.content.Context;

public class AuthManager {

    private static final String PREF = "auth";

    public static void setLogged(Context ctx, boolean logged) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .putBoolean("logged", logged)
                .apply();
    }

    public static void setName(Context ctx, String name) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .putString("name", name)
                .apply();
    }

    public static void setEmail(Context ctx, String email) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .putString("email", email)
                .apply();
    }

    public static void login(Context ctx, String name, String email) {
        setLogged(ctx, true);
        setName(ctx, name);
        setEmail(ctx, email);
    }

    public static boolean isLogged(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getBoolean("logged", false);
    }

    public static String getName(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString("name", "Гость");
    }

    public static String getEmail(Context ctx) {
        return ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString("email", "");
    }

    /**
     * Полный выход: очистка данных аккаунта и локального прогресса.
     */
    public static void logout(Context ctx) {
        // Очищаем данные авторизации
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Очищаем локальный прогресс, чтобы не смешивать данные разных пользователей
        ProgressManager.clearLocalProgress(ctx);
    }
}