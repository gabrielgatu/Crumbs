package com.gabrielgatu.allaround.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gabrielgatu on 22/05/15.
 */
public class APISystemPreferences {

    public static final String TAG_PREFERENCES = "com.gabrielgatu.buzz.PREFERENCES" ;

    public static void save(Context context, String tag, String value) {
        SharedPreferences pref = context.getSharedPreferences(TAG_PREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString(tag, value);
        editor.commit();
    }

    public static String get(Context context, String tag) {
        SharedPreferences pref = context.getSharedPreferences(TAG_PREFERENCES, 0);
        SharedPreferences.Editor editor = pref.edit();

        return pref.getString(tag, null);
    }

    public static void delete(Context context, String tag) {
        save(context, tag, null);
    }
}
