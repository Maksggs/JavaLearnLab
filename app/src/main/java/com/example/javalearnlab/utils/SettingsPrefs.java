package com.example.javalearnlab.utils;

import android.content.Context;

public class SettingsPrefs {
    private static final String PREF_NAME = "app_settings";
    private static final String KEY_REMINDER_ENABLED = "reminder_enabled";
    private static final String KEY_REMINDER_HOUR = "reminder_hour";
    private static final String KEY_REMINDER_MINUTE = "reminder_minute";

    public static void setReminderEnabled(Context context, boolean enabled) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit().putBoolean(KEY_REMINDER_ENABLED, enabled).apply();
    }

    public static boolean isReminderEnabled(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_REMINDER_ENABLED, false);
    }

    public static void setReminderTime(Context context, int hour, int minute) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit().putInt(KEY_REMINDER_HOUR, hour)
                .putInt(KEY_REMINDER_MINUTE, minute).apply();
    }

    public static int getReminderHour(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_REMINDER_HOUR, 18); // default 18:00
    }

    public static int getReminderMinute(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .getInt(KEY_REMINDER_MINUTE, 0);
    }
}
