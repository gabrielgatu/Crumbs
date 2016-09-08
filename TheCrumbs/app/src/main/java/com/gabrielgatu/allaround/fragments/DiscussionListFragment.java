package com.gabrielgatu.allaround.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielgatu.allaround.DiscussionDetailActivity;
import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.application.MainApplication;
import com.gabrielgatu.allaround.beans.Discussion;
import com.gabrielgatu.allaround.beans.User;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.gabrielgatu.allaround.NewDiscussionActivity;
import com.gabrielgatu.allaround.utils.GPSTracker;
import com.gabrielgatu.allaround.utils.ImageUtils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.melnykov.fab.FloatingActionButton;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiscussionListFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG_DISCUSSION_ID = "com.gabrielgatu.buzz.DISCUSSION_ID";

    private MainApplication mApplication;
    private User mUser;

    private SwipeRefreshLayout mSwipeLayout;
    private FloatingActionButton mFab;
    private ListView mDiscussionList;

    private DiscussionListAdapter mDiscussionListAdapter;
    private Discussion[] mDiscussions = new Discussion[]{};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discussion_list, container, false);

        // Get the application
        mApplication = (MainApplication) getActivity().getApplication();
        mUser = mApplication.getUser();

        // Initialize UI components
        mSwipeLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_layout);
        mFab = (FloatingActionButton) root.findViewById(R.id.fab);
        mDiscussionList = (ListView) root.findViewById(R.id.list);

        // Setting adapters
        mDiscussionListAdapter = new DiscussionListAdapter(getActivity(), mDiscussions);
        mDiscussionList.setAdapter(mDiscussionListAdapter);

        // Attach FAB button to the list
        mFab.attachToListView(mDiscussionList);

        // Init the swipe layout
        mSwipeLayout.setColorSchemeResources(R.color.default_primary, R.color.default_secondary);

        // Add listeners
        mSwipeLayout.setOnRefreshListener(this);
        mDiscussionList.setOnItemClickListener(this);
        mFab.setOnClickListener(this);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        downloadDiscussions();
    }

    private void reloadList(Discussion[] discussions) {
        mDiscussions = discussions;

        mDiscussionListAdapter = new DiscussionListAdapter(getActivity(), mDiscussions);
        mDiscussionList.setAdapter(mDiscussionListAdapter);
        mSwipeLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        downloadDiscussions();
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String discussionID = mDiscussions[i].getId();

        Intent intent = new Intent(getActivity(), DiscussionDetailActivity.class);
        intent.putExtra(TAG_DISCUSSION_ID, discussionID);
        startActivity(intent);
    }

    private void downloadDiscussions() {
        GPSTracker gps = new GPSTracker(getActivity());
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        RESTClient.getDiscussions(latitude, longitude, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String responseString = new String(responseBody, "UTF-8");

                    Gson gson = new Gson();
                    Discussion[] discussions = gson.fromJson(responseString, Discussion[].class);
                    reloadList(discussions);


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private class DiscussionListAdapter extends BaseAdapter {

        private Context context;
        private Discussion[] discussions;

        public DiscussionListAdapter(Context context, Discussion[] discussions) {
            this.context = context;
            this.discussions = discussions;
        }

        @Override
        public int getCount() {
            return discussions.length;
        }

        @Override
        public Object getItem(int position) {
            return discussions[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;

            Discussion discussion = discussions[position];

            if (view == null) {
                LayoutInflater inflater = (LayoutInflater)
                        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                view = inflater.inflate(R.layout.partial_list_discussion, parent, false);

                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            else {
                holder = (ViewHolder) view.getTag();
            }

            String imageString = null;
            Bitmap userImage = null;
            if (imageString != null) {
                userImage = ImageUtils.fromBase64(imageString);
            } else {
                userImage = BitmapFactory.decodeResource(getResources(),
                        R.drawable.user_no_photo);
            }

            Date date = new Date(Long.parseLong(discussion.getDate()));
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateFormatted = formatter.format(date);

            holder.authorImage.setImageBitmap(userImage);
            holder.title.setText(discussion.getTitle());
            holder.subtitle.setText("Discussion: " + discussion.getText());
            holder.date.setText(dateFormatted);

            return view;
        }

        class ViewHolder {
            ImageView authorImage;
            TextView title;
            TextView subtitle;
            TextView date;

            ViewHolder(View root) {
                authorImage = (ImageView) root.findViewById(R.id.author_image);
                title = (TextView) root.findViewById(R.id.title);
                subtitle = (TextView) root.findViewById(R.id.subtitle);
                date = (TextView) root.findViewById(R.id.date);
            }
        }
    }
}