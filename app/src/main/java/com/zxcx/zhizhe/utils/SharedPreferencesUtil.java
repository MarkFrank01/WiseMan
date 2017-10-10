package com.zxcx.zhizhe.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zxcx.zhizhe.App;

/**
 * Created by WuXiaolong
 * on 2016/3/31.
 */
public class SharedPreferencesUtil {

    static Context sContext = App.getContext();

    public static String getString(String key,
                                   final String defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(sContext);
        return settings.getString(key, defaultValue);
    }

    public static void saveData(final String key,
                                 final String value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(sContext);
        settings.edit().putString(key, value).apply();
    }

    public static boolean getBoolean(final String key,
                                     final boolean defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(sContext);
        return settings.getBoolean(key, defaultValue);
    }

    public static boolean hasKey(final String key) {
        return PreferenceManager.getDefaultSharedPreferences(sContext).contains(key);
    }

    public static void saveData(final String key,
                                  final boolean value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(sContext);
        settings.edit().putBoolean(key, value).apply();
    }

    public static void saveData(final String key,
                              final int value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(sContext);
        settings.edit().putInt(key, value).apply();
    }

    public static int getInt(final String key,
                             final int defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(sContext);
        return settings.getInt(key, defaultValue);
    }

    public static void saveData(final String key,
                                final float value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(sContext);
        settings.edit().putFloat(key, value).apply();
    }

    public static float getFloat(final String key,
                                 final float defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(sContext);
        return settings.getFloat(key, defaultValue);
    }

    public static void saveData(final String key,
                               final long value) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(sContext);
        settings.edit().putLong(key, value).apply();
    }

    public static long getLong(final String key,
                               final long defaultValue) {
        final SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(sContext);
        return settings.getLong(key, defaultValue);
    }

    public static void clear() {
        SharedPreferences p = PreferenceManager.getDefaultSharedPreferences( sContext);
        SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.apply();
    }
}
