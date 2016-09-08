package com.gabrielgatu.allaround.application;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.beans.User;
import com.gabrielgatu.allaround.broadcasts.UpdateLocalUserLocationAlarmReceiver;
import com.gabrielgatu.allaround.broadcasts.UploadUserLocationAlarmReceiver;
import com.gabrielgatu.allaround.utils.StorageManager;
import com.google.gson.Gson;

/**
 * Created by gabrielgatu on 01/06/15.
 */
public class MainApplication extends Application {

    private PendingIntent mUpdatePendingIntent;
    private AlarmManager mUpdateManager;

    private PendingIntent mUploadPendingIntent;
    private AlarmManager mUploadManager;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    private void updatedUserLocationContinuously() {
        Intent alarmIntent = new Intent(this, UpdateLocalUserLocationAlarmReceiver.class);
        mUpdatePendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        mUpdateManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mUpdateManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                getResources().getInteger(R.integer.update_location_interval),
                mUpdatePendingIntent);
    }

    private void uploadUserLocationContinuously() {
        Intent alarmIntent = new Intent(this, UploadUserLocationAlarmReceiver.class);
        mUploadPendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        mUploadManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mUploadManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),
                getResources().getInteger(R.integer.uploadlocation_interval),
                mUploadPendingIntent);
    }

    private void cancelAlarms() {

        if (mUpdateManager != null) {
            mUpdateManager.cancel(mUpdatePendingIntent);
        }

        if (mUploadManager != null) {
            mUploadManager.cancel(mUploadPendingIntent);
        }
    }

    public void setUser(User user) {
        StorageManager.saveUser(getBaseContext(), new Gson().toJson(user));

        if (mUpdateManager == null) { updatedUserLocationContinuously(); }
        if (mUploadManager == null) { uploadUserLocationContinuously(); }
    }

    public void setUser(String userJson) {
        StorageManager.saveUser(getBaseContext(), userJson);
        setUser(getUser());
    }

    public User getUser() {
        return StorageManager.getUser(this);
    }

    public void removeUser() {
        StorageManager.deleteUser(this);
        cancelAlarms();
    }
}