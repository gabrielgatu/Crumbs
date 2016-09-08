package com.gabrielgatu.allaround.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gabrielgatu.allaround.application.MainApplication;
import com.gabrielgatu.allaround.beans.User;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.gabrielgatu.allaround.utils.StorageManager;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

public class UploadUserLocationAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        User user = StorageManager.getUser(context);

        String userId = user.getId();
        double latitude = user.getLatitude();
        double longitude = user.getLongitude();

        RESTClient.updateUserLocation(userId, latitude, longitude, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d("USER", "SUCCESS UPDATED LOC");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("USER", "FAILED UPDATED LOC");
            }
        });
    }

}
