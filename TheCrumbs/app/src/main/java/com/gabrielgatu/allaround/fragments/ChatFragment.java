package com.gabrielgatu.allaround.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.adapters.ChatListAdapter;
import com.gabrielgatu.allaround.beans.Discussion;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.gabrielgatu.allaround.rest.SocketIO;
import com.gabrielgatu.allaround.utils.APISystemPreferences;
import com.gabrielgatu.allaround.utils.GPSTracker;
import com.gabrielgatu.allaround.utils.Utils;
import com.gabrielgatu.allaround.beans.Message;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatFragment extends Fragment implements View.OnClickListener, Emitter.Listener {

    private ListView mChatList;
    private ChatListAdapter mChatListAdapter;
    private ImageButton mSendButton;
    private EditText mMessageField;

    private SocketIO mSocket;
    private ArrayList<Message> mMessages = new ArrayList<>();

    public ChatFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_chat, container, false);

        // Initialize UI components
        mChatList = (ListView) root.findViewById(R.id.list);
        mSendButton = (ImageButton) root.findViewById(R.id.send);
        mMessageField = (EditText) root.findViewById(R.id.message);

        // Setting adapters
        mChatListAdapter = new ChatListAdapter(getActivity(), mMessages);
        mChatList.setAdapter(mChatListAdapter);
        mChatList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        mChatList.setStackFromBottom(true);

        // Add listeners
        mSendButton.setOnClickListener(this);
        mMessageField.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mSendButton.performClick();
                    return true;
                }
                return false;
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GPSTracker gps = new GPSTracker(getActivity());
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        // Init socket
        mSocket = SocketIO.getInstance(getActivity());
        mSocket.addListener(Utils.RECEIVED_MESSAGE, this);

        RESTClient.getMessages(latitude, longitude, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String responseString = new String(responseBody, "UTF-8");

                    Gson gson = new Gson();
                    Message[] messages = gson.fromJson(responseString, Message[].class);
                    reloadList(messages);

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("USER", "FAILED");
            }
        });
    }

    private void reloadList(Message[] messages) {
        mMessages = new ArrayList<>(Arrays.asList(messages));

        mChatListAdapter = new ChatListAdapter(getActivity(), mMessages);
        mChatList.setAdapter(mChatListAdapter);
    }

    private void sendMessage() {
        String message = mMessageField.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getActivity(), getString(R.string.error_message_send_empty_message), Toast.LENGTH_SHORT).show();
        } else {

            GPSTracker gps = new GPSTracker(getActivity());
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            mSocket.emit(Utils.SEND_MESSAGE, message, latitude, longitude);
            mMessageField.setText("");

            //triggerNotification("Message received", message);
        }
    }

    @Override
    public void call(final Object... args) {
        Log.d("USER", "CALL CALLED");

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String userName = args[0].toString();
                    String userId = args[1].toString();
                    String message = args[2].toString();

                    addMessageToList(new Message(userId, userName, message));
                }
            });
        }
        else {
            Log.d("USER", "IS NULL THE ACT.");
        }
    }

    private void addMessageToList(Message message) {
        mMessages.add(message);
        mChatListAdapter.notifyDataSetChanged();
    }

    private void triggerNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity())
                .setSmallIcon(R.drawable.user_no_photo)
                .setContentTitle(title)
                .setContentText(message)
                .setSound(soundUri);
        notificationManager.notify(0, mBuilder.build());
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.send:
                sendMessage();
                break;
        }
    }
}