package com.gabrielgatu.allaround.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.adapters.FriendsListAdapter;
import com.gabrielgatu.allaround.beans.User;

public class AllFriendsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView mList;
    private FriendsListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends, container, false);

        // Init UI components
        mList = (GridView) root.findViewById(R.id.list);

        // Init
        initListAdapter();

        return root;
    }

    private void initListAdapter() {
        mAdapter = new FriendsListAdapter(getActivity(), new User[0]);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}
