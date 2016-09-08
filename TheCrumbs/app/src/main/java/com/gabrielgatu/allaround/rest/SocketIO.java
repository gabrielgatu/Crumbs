package com.gabrielgatu.allaround.rest;

import android.content.Context;
import android.util.Log;

import com.gabrielgatu.allaround.utils.APISystemPreferences;
import com.gabrielgatu.allaround.utils.StorageManager;
import com.gabrielgatu.allaround.utils.Utils;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

/**
 * Created by gabrielgatu on 22/05/15.
 */
public class SocketIO {

    private static SocketIO instance;
    private Socket mSocket;

    private SocketIO(Context context) {

        // Initialize socket
        try {
            String userID = StorageManager.getUser(context).getId();

            IO.Options options = new IO.Options();
            options.query = "user_id=" + userID;

            mSocket = IO.socket(Utils.SERVER_ROOT_URL, options);

        } catch (URISyntaxException e) {
            Log.d("USER", "Error on socket connection: " + e.getMessage());
        }

        connect();
    }

    public static SocketIO getInstance(Context context) {
        if (instance == null)
            instance = new SocketIO(context);
        return instance;
    }

    public void addListener(String event, Emitter.Listener listener) {
        if (! mSocket.hasListeners(event)) {
            mSocket.on(event, listener);
        }
    }

    public void emit(String event, Object... params) {
        mSocket.emit(event, params);
    }

    private void connect() {
        mSocket.connect();
    }
}
