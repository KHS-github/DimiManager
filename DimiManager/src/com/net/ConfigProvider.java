package com.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by User on 2014-05-30.
 */
public class ConfigProvider {
    public static void initialize(Context context)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.remove("Initkey");
        editor.commit();
    }
    public static void putStringData(Context context, String key, String data)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString(key, data);
        editor.commit();
    }

    public static void putBooleanData(Context context, String key, boolean data)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean(key, data);
        editor.commit();
    }

    public static void putIntData(Context context, String key, int data)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt(key, data);
        editor.commit();
    }

    public static String getStringData(Context context, String key, String defData)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getString(key, defData);
    }

    public static int getIntData(Context context, String key, int defData)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        return pref.getInt(key, defData);
    }
}
