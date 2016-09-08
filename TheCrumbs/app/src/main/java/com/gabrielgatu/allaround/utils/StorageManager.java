package com.gabrielgatu.allaround.utils;

import android.content.Context;

import com.gabrielgatu.allaround.beans.User;
import com.google.gson.Gson;

/**
 * Created by gabrielgatu on 27/05/15.
 */
public class StorageManager {

    public static User getUser(Context context) {

        String userJson = APISystemPreferences.get(context, Utils.TAG_USER);
        return new Gson().fromJson(userJson, User.class);
    }

    public static void saveUser(Context context, String userJson) {
        APISystemPreferences.save(context, Utils.TAG_USER, userJson);
    }

    public static void deleteUser(Context context) {
        APISystemPreferences.delete(context, Utils.TAG_USER);
    }
}
