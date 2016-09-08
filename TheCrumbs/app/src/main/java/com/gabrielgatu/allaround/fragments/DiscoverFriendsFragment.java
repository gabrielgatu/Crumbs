package com.gabrielgatu.allaround.fragments;


import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.adapters.FriendsListAdapter;
import com.gabrielgatu.allaround.application.MainApplication;
import com.gabrielgatu.allaround.beans.User;
import com.gabrielgatu.allaround.drawer.ProfileActivity;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.gabrielgatu.allaround.utils.GPSTracker;
import com.gabrielgatu.allaround.utils.Utils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public class DiscoverFriendsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private MainApplication mApplication;

    private GridView mList;
    private FriendsListAdapter mAdapter;
    private User[] mUsers;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends, container, false);

        // Init application
        mApplication = (MainApplication) getActivity().getApplication();

        // Init UI components
        mList = (GridView) root.findViewById(R.id.list);

        // Init
        downloadUsersInArea();

        // Set listeners
        mList.setOnItemClickListener(this);

        return root;
    }

    private void downloadUsersInArea() {
        GPSTracker gps = new GPSTracker(getActivity());
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        RESTClient.getUsersInArea(latitude, longitude, false, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String responseString = new String(responseBody, "UTF-8");

                    User[] usersAround = new Gson().fromJson(responseString, User[].class);
                    User[] usersAroundWithoutUserLogged = new User[usersAround.length - 1];
                    User userLogged = mApplication.getUser();

                    int indexInUsersAroundWithoutUserLogged = 0;
                    for (int i = 0; i < usersAround.length; i++) {
                        User user = usersAround[i];
                        if (! user.getId().equals(userLogged.getId())) {
                            usersAroundWithoutUserLogged[indexInUsersAroundWithoutUserLogged] = user;
                            indexInUsersAroundWithoutUserLogged++;
                        }
                    }

                    mUsers = usersAroundWithoutUserLogged;
                    initListAdapter();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void initListAdapter() {
        mAdapter = new FriendsListAdapter(getActivity(), mUsers);
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(Utils.TAG_USER_ID, mUsers[i].getId());
        startActivity(intent);
    }
}