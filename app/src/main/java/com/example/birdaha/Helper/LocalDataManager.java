package com.example.birdaha.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class LocalDataManager {

    public static void setSharedPreference(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static void setSharedPreference(Context context, String key, Boolean value, String sharedPrefName)
    {
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static Boolean getSharedPreference(Context context, String key, String sharedPrefName, Boolean defValue) {
        return context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static String getSharedPreference(Context context, String key, String defaultValue) {
        return context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE).getString(key, defaultValue);
    }

    public static void clearSharedPreference(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.clear();
        edit.apply();
    }

    public static void removeSharedPreference(Context context, String key){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.remove(key);
        edit.apply();
    }

}