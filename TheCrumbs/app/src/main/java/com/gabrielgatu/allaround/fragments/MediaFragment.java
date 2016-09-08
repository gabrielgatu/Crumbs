package com.gabrielgatu.allaround.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.adapters.PhotoListAdapter;
import com.gabrielgatu.allaround.beans.Image;
import com.gabrielgatu.allaround.beans.User;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.gabrielgatu.allaround.utils.GPSTracker;
import com.gabrielgatu.allaround.utils.ImageUtils;
import com.gabrielgatu.allaround.utils.StorageManager;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.melnykov.fab.FloatingActionButton;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaFragment extends Fragment implements AdapterView.OnItemClickListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int CAMERA_REQUEST = 1888;

    private SwipeRefreshLayout mSwipeLayout;
    private FloatingActionButton mFab;
    private GridView mMediaGrid;
    private User mUser;
    private Image[] mImages;

    public MediaFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_media, container, false);

        // Get the user from storage
        mUser = StorageManager.getUser(getActivity());

        // Init components
        mSwipeLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_layout);
        mFab = (FloatingActionButton) root.findViewById(R.id.fab);
        mMediaGrid = (GridView) root.findViewById(R.id.grid);

        // Basic init
        mFab.attachToListView(mMediaGrid);

        // Init the swipe layout
        mSwipeLayout.setColorSchemeResources(R.color.default_primary, R.color.default_secondary);

        // Setting listeners
        mSwipeLayout.setOnRefreshListener(this);
        mFab.setOnClickListener(this);
        mMediaGrid.setOnItemClickListener(this);

        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        downloadImages();
    }

    private void refreshList() {
        Bitmap[] imagesBitmap = new Bitmap[mImages.length];
        for (int i = 0; i < imagesBitmap.length; i++) {
            Image image = mImages[i];
            imagesBitmap[i] = ImageUtils.fromBase64(image.getImage());
        }

        PhotoListAdapter adapter = new PhotoListAdapter(getActivity(), imagesBitmap);
        mMediaGrid.setAdapter(adapter);
        mSwipeLayout.setRefreshing(false);
    }

    private void downloadImages() {
        GPSTracker gps = new GPSTracker(getActivity());
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        RESTClient.getImages(latitude, longitude, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String responseString = new String(responseBody, "UTF-8");

                    Image[] images = new Gson().fromJson(responseString, Image[].class);
                    mImages = images;

                    refreshList();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void captureImage() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            uploadImage(image);
        }
    }

    private void uploadImage(Bitmap image) {
        GPSTracker gps = new GPSTracker(getActivity());
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        int width = image.getWidth();
        int height = image.getHeight();

        int newWidth = width;
        int newHeight = height;

        while(newWidth > 300 && newHeight > 300) {
            newWidth /= 2;
            newHeight /= 2;
        }

        final Bitmap imageResized = ImageUtils.resizeImage(image, newWidth, newHeight);
        final String encodedImage = ImageUtils.toBase64(imageResized);

        RESTClient.createImage(mUser.getId(), latitude, longitude, encodedImage, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getActivity(), "Image uploaded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Cannot upload the image!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        View imageView = getActivity().getLayoutInflater().inflate(R.layout.partial_fragment_media_zoom_image
                , null);
        ImageView image = (ImageView) imageView.findViewById(R.id.image);
        image.setImageBitmap(ImageUtils.fromBase64(mImages[i].getImage()));

        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(imageView);
        dialog.show();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fab:
                captureImage();
                break;
        }
    }

    @Override
    public void onRefresh() {
        downloadImages();
    }
}
