package com.example.javalearnlab.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.example.javalearnlab.theory.model.Topic;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProgressManager {

    private static final String TAG = "ProgressManager";
    private static final String PREF_LOCAL = "progress_local";
    private static final String PREF_PENDING = "progress_pending";
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    // --- Сохранение прогресса (локально + попытка отправить на сервер) ---
    public static void setProgress(Context context, String key, boolean value) {
        // Локальное сохранение (всегда)
        SharedPreferences prefs = context.getSharedPreferences(PREF_LOCAL, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(key, value).apply();

        // Попытка отправить на сервер, если пользователь авторизован
        if (AuthManager.isLogged(context)) {
            sendProgressToServer(context, key, value);
        }
    }

    private static void sendProgressToServer(Context context, String key, boolean value) {
        executor.execute(() -> {
            if (!isNetworkAvailable(context)) {
                // Нет сети – добавляем в очередь ожидания
                addToPending(context, key, value);
                return;
            }

            try {
                JSONObject body = new JSONObject();
                body.put("email", AuthManager.getEmail(context));
                body.put("key", key);
                body.put("value", String.valueOf(value));

                String response = ApiClient.post(ServerManager.getBaseUrl(context) + "/api/progress/save", body);
                if (response != null) {
                    // Успешно – удаляем из очереди, если был там
                    removeFromPending(context, key);
                    Log.d(TAG, "Progress synced: " + key);
                } else {
                    // Ошибка сервера или сети – в очередь
                    addToPending(context, key, value);
                    Log.w(TAG, "Server error, queued: " + key);
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception sending progress", e);
                addToPending(context, key, value);
            }
        });
    }

    // --- Очередь ожидающих синхронизации ключей ---
    private static void addToPending(Context context, String key, boolean value) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_PENDING, Context.MODE_PRIVATE);
        // Сохраняем ключ в Set<String>
        Set<String> pending = new HashSet<>(prefs.getStringSet("keys", new HashSet<>()));
        pending.add(key);
        prefs.edit().putStringSet("keys", pending).apply();
        // Сохраняем значение отдельно (т.к. Set<String> хранит только ключи)
        prefs.edit().putBoolean(key, value).apply();
    }

    private static void removeFromPending(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_PENDING, Context.MODE_PRIVATE);
        Set<String> pending = new HashSet<>(prefs.getStringSet("keys", new HashSet<>()));
        if (pending.remove(key)) {
            prefs.edit().putStringSet("keys", pending).apply();
            prefs.edit().remove(key).apply();
        }
    }

    // --- Синхронизация всех накопленных ключей (вызывается при восстановлении сети) ---
    public static void syncPendingProgress(Context context) {
        if (!AuthManager.isLogged(context)) return;

        executor.execute(() -> {
            SharedPreferences prefs = context.getSharedPreferences(PREF_PENDING, Context.MODE_PRIVATE);
            Set<String> pending = new HashSet<>(prefs.getStringSet("keys", new HashSet<>()));
            if (pending.isEmpty()) return;

            Log.d(TAG, "Syncing pending keys: " + pending.size());

            for (String key : pending) {
                boolean value = prefs.getBoolean(key, false);
                try {
                    JSONObject body = new JSONObject();
                    body.put("email", AuthManager.getEmail(context));
                    body.put("key", key);
                    body.put("value", String.valueOf(value));

                    String response = ApiClient.post(ServerManager.getBaseUrl(context) + "/api/progress/save", body);
                    if (response != null) {
                        removeFromPending(context, key);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Failed to sync key: " + key, e);
                }
            }
        });
    }

    // --- Вспомогательный метод проверки сети ---
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    // --- Получение прогресса (из локального кэша) ---
    public static boolean getProgress(Context context, String key, boolean defaultValue) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_LOCAL, Context.MODE_PRIVATE);
        return prefs.getBoolean(key, defaultValue);
    }

    // --- Очистка локального прогресса (при выходе) ---
    public static void clearLocalProgress(Context context) {
        context.getSharedPreferences(PREF_LOCAL, Context.MODE_PRIVATE).edit().clear().apply();
        context.getSharedPreferences(PREF_PENDING, Context.MODE_PRIVATE).edit().clear().apply();
    }

    // --- Синхронизация с сервера при входе (загружает прогресс и заменяет локальный) ---
    public static void syncFromServer(Context context, Runnable onComplete) {
        if (!AuthManager.isLogged(context)) {
            if (onComplete != null) onComplete.run();
            return;
        }
        executor.execute(() -> {
            try {
                JSONObject body = new JSONObject();
                body.put("email", AuthManager.getEmail(context));
                String response = ApiClient.post(ServerManager.getBaseUrl(context) + "/api/progress/load", body);
                if (response != null) {
                    JSONObject json = new JSONObject(response);
                    SharedPreferences prefs = context.getSharedPreferences(PREF_LOCAL, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear(); // очищаем старый прогресс перед загрузкой нового
                    for (java.util.Iterator<String> it = json.keys(); it.hasNext(); ) {
                        String key = it.next();
                        String value = json.getString(key);
                        editor.putBoolean(key, Boolean.parseBoolean(value));
                    }
                    editor.apply();
                }
            } catch (Exception e) {
                Log.e(TAG, "Failed to sync from server", e);
            } finally {
                if (onComplete != null) {
                    context.getMainExecutor().execute(onComplete);
                }
            }
        });
    }

    // --- Удобные обёртки для ключей прогресса ---
    public static void markCompleted(Context context, int topicId) {
        setProgress(context, "topic_" + topicId, true);
    }

    public static boolean isCompleted(Context context, int topicId) {
        return getProgress(context, "topic_" + topicId, false);
    }

    public static void markTestPassed(Context context, int topicId) {
        setProgress(context, "test_" + topicId, true);
    }

    public static boolean isTestPassed(Context context, int topicId) {
        return getProgress(context, "test_" + topicId, false);
    }

    public static void markDay(Context context) {
        String today = new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date());
        setProgress(context, "day_" + today, true);
    }

    public static boolean wasActive(Context context, String dayKey) {
        return getProgress(context, "day_" + dayKey, false);
    }

    public static int getCompletedCount(Context context, List<Topic> topics) {
        int count = 0;
        for (Topic t : topics) {
            if (isCompleted(context, t.id)) count++;
        }
        return count;
    }

    public static boolean isUnlocked(Context context, List<Topic> topics, int position) {
        if (position == 0) return true;
        Topic prev = topics.get(position - 1);
        return isTestPassed(context, prev.id);
    }

    public static void resetAll(Context context) {
        clearLocalProgress(context);
    }
}