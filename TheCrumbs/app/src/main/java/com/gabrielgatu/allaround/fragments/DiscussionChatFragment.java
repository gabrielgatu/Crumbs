package com.gabrielgatu.allaround.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.adapters.ChatListAdapter;
import com.gabrielgatu.allaround.beans.Message;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.gabrielgatu.allaround.rest.SocketIO;
import com.gabrielgatu.allaround.utils.GPSTracker;
import com.gabrielgatu.allaround.utils.Utils;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

public class DiscussionChatFragment extends Fragment implements View.OnClickListener, Emitter.Listener {

    private ListView mMessagesList;
    private ChatListAdapter mMessagesListAdapter;
    private EditText mMessageField;
    private ImageButton mSendButton;
    private String mDiscussionID;

    private SocketIO mSocket;


    private ArrayList<Message> mMessages = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discussion_chat, container, false);

        mMessagesList = (ListView) root.findViewById(R.id.list);
        mMessageField = (EditText) root.findViewById(R.id.message);
        mSendButton = (ImageButton) root.findViewById(R.id.send);

        // Setting adapters
        mMessagesListAdapter = new ChatListAdapter(getActivity(), mMessages);
        mMessagesList.setAdapter(mMessagesListAdapter);
        mMessagesList.setTranscriptMode(ListView.TRANSCRIPT_MODE_NORMAL);
        mMessagesList.setStackFromBottom(true);

        // Setting listeners
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

        // Start downloading the details of the discussion
        // to populate the view.
        mDiscussionID = getArguments().getString(Utils.TAG_USER_ID);
        downloadDiscussionMessages(mDiscussionID);

        // Setting up the socket for communication with the server
        // Used for the messages.
        initSocket();

        return root;
    }

    private void downloadDiscussionMessages(String discussionId) {

        GPSTracker gps = new GPSTracker(getActivity());
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        RESTClient.getMessages(latitude, longitude, discussionId, new AsyncHttpResponseHandler() {
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

        mMessagesListAdapter = new ChatListAdapter(getActivity(), mMessages);
        mMessagesList.setAdapter(mMessagesListAdapter);
    }

    private void initSocket() {
        String listenerName = Utils.RECEIVED_MESSAGE + "." + mDiscussionID;

        // Init socket
        mSocket = SocketIO.getInstance(getActivity());
        mSocket.addListener(listenerName, this);
    }

    private void sendMessage() {
        String message = mMessageField.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getActivity(), getString(R.string.error_message_send_empty_message), Toast.LENGTH_SHORT).show();
        } else {

            GPSTracker gps = new GPSTracker(getActivity());
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            mSocket.emit(Utils.SEND_MESSAGE, message, latitude, longitude, mDiscussionID);
            mMessageField.setText("");
        }
    }

    @Override
    public void call(final Object... args) {
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

    private void addMessageToList(Message message) {
        mMessages.add(message);
        mMessagesListAdapter.notifyDataSetChanged();
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
