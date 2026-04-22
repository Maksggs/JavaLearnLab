package com.example.javalearnlab.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.javalearnlab.theory.model.Topic;

import java.util.List;

public class ProgressManager {

    private static final String PREF = "progress";

    public static void markCompleted(Context context, int topicId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        prefs.edit().putBoolean("topic_" + topicId, true).apply();
    }

    public static boolean isCompleted(Context context, int topicId) {
        SharedPreferences prefs = context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        return prefs.getBoolean("topic_" + topicId, false);
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

    public static void markTestPassed(Context context, int topicId) {
        SharedPreferences prefs = context.getSharedPreferences("progress", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("test_" + topicId, true).apply();
    }

    public static boolean isTestPassed(Context context, int topicId) {
        SharedPreferences prefs = context.getSharedPreferences("progress", Context.MODE_PRIVATE);
        return prefs.getBoolean("test_" + topicId, false);
    }

    //Для отладки
    public static void resetAll(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("progress", Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }


}