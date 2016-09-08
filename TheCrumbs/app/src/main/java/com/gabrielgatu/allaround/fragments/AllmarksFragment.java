package com.gabrielgatu.allaround.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.gabrielgatu.allaround.NewDiscussionActivity;
import com.gabrielgatu.allaround.R;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class AllmarksFragment extends Fragment implements View.OnClickListener {

    private AsymmetricGridView mList;
    private AsymmetricGridViewAdapter mAdapter;
    private FloatingActionButton mFab;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_allmarks, container, false);

        // Initialize UI components
        mList = (AsymmetricGridView) root.findViewById(R.id.list);
        mFab = (FloatingActionButton) root.findViewById(R.id.fab);

        // All Init
        initListAdapter();

        // Add listeners
        mFab.setOnClickListener(this);

        return root;
    }

    private void initListAdapter() {

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fab:
                Intent intent = new Intent(getActivity(), NewDiscussionActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class AllmarksAdapter extends BaseAdapter {

        private Context context;
        private int[] images;
        private String[] texts;

        public AllmarksAdapter(Context context, int[] images, String[] texts) {
            this.context = context;
            this.images = images;
            this.texts = texts;
        }

        @Override
        public int getCount() {
            return images.length + texts.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (position < images.length) {

                ImageView image = new ImageView(context);
                image.setImageResource(images[position]);

                view = image;

            } else {

                TextView text = new TextView(context);
                text.setText(texts[position]);

                view = text;
            }

            return view;
        }
    }
}