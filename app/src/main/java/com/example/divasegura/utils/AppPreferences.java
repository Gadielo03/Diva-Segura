package com.example.divasegura.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String PREFS_NAME = "AppPreferences";
    private static final String FIRST_RUN_KEY = "first_run";

    private static final String AUTOMATIC_TAKE_PICTURE_KEY = "automatic_take_picture";

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

    public static boolean isAutomaticTakePictureEnabled(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(AUTOMATIC_TAKE_PICTURE_KEY, false);
    }

    public static void setAutomaticTakePictureEnabled(Context context, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(AUTOMATIC_TAKE_PICTURE_KEY, value).apply();
    }
}