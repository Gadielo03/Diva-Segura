package com.example.divasegura;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String PREFS_NAME = "AppPreferences";
    private static final String FIRST_RUN_KEY = "first_run";

    public static boolean isFirstRun(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(FIRST_RUN_KEY, true);
    }

    public static void setFirstRun(Context context, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(FIRST_RUN_KEY, value).apply();
    }

    public static void resetFirstRun(Context context) {
        setFirstRun(context, true);
    }
}