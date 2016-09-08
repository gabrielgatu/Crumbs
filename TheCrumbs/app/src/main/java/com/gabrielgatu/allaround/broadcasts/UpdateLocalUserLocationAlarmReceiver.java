package com.gabrielgatu.allaround.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gabrielgatu.allaround.beans.User;
import com.gabrielgatu.allaround.utils.GPSTracker;
import com.gabrielgatu.allaround.utils.StorageManager;
import com.google.gson.Gson;

public class UpdateLocalUserLocationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        GPSTracker gps = new GPSTracker(context);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        User user = StorageManager.getUser(context);
        user.setLatitude(latitude);
        user.setLongitude(longitude);

        StorageManager.saveUser(context, new Gson().toJson(user));
    }
}