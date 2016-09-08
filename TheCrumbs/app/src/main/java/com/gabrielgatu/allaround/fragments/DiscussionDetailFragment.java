package com.gabrielgatu.allaround.fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.beans.Discussion;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.gabrielgatu.allaround.utils.ImageUtils;
import com.gabrielgatu.allaround.utils.Utils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DiscussionDetailFragment extends Fragment {

    private ImageView mAuthorImage;
    private TextView mQuestion;
    private TextView mAuthorName;
    private TextView mDate;

    private Discussion mDiscussion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discussion_detail, container, false);

        // Initialize UI components
        mQuestion = (TextView) root.findViewById(R.id.text);
        mAuthorImage = (ImageView) root.findViewById(R.id.author_image);
        mAuthorName = (TextView) root.findViewById(R.id.author_name);
        mDate = (TextView) root.findViewById(R.id.date);

        // Get the discussion id from bundle and
        // start downloading the discussion details
        String discussionId = getArguments().getString(Utils.TAG_USER_ID);
        downloadDiscussionDetails(discussionId);

        return root;
    }

    private void downloadDiscussionDetails(String discussionID) {
        RESTClient.getDiscussionDetails(discussionID, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String jsonText = new String(responseBody, "UTF-8");

                    Gson gson = new Gson();
                    mDiscussion = gson.fromJson(jsonText, Discussion.class);

                    updateUI();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void updateUI() {
        Date date = new Date(Long.parseLong(mDiscussion.getDate()));
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateFormatted = formatter.format(date);

        String imageString = mDiscussion.getUser().getImage();
        Bitmap userImage = null;
        if (imageString != null) {
            userImage = ImageUtils.fromBase64(imageString);
        } else {
            userImage =  BitmapFactory.decodeResource(getResources(),
                    R.drawable.user_no_photo);
        }

        mQuestion.setText(mDiscussion.getText());
        mAuthorImage.setImageBitmap(userImage);
        mAuthorName.setText(mDiscussion.getUser().getUsername());
        mDate.setText(dateFormatted);
    }
}