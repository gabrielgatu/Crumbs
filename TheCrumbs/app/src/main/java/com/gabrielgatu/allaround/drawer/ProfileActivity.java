package com.gabrielgatu.allaround.drawer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.application.MainApplication;
import com.gabrielgatu.allaround.beans.User;
import com.gabrielgatu.allaround.libraries.ActionBarCustomActivity;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.gabrielgatu.allaround.utils.ImageUtils;
import com.gabrielgatu.allaround.utils.Utils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public class ProfileActivity extends ActionBarCustomActivity {

    private MainApplication mApplication;
    private User mUser;

    private ImageView mUserImage;
    private TextView mUserName;

    private TextView mPhotosNumber;
    private TextView mFriendsNumber;
    private TextView mMessagesNumber;
    private Menu mMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get the application
        mApplication = (MainApplication) getApplication();

        // Init UI components
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserImage = (ImageView) findViewById(R.id.user_image);
        mPhotosNumber = (TextView) findViewById(R.id.photos_number);
        mFriendsNumber = (TextView) findViewById(R.id.friends_number);
        mMessagesNumber = (TextView) findViewById(R.id.message_number);

        // Init actionbar
        initActionBar("Profile");
        setToolbarBackButtonEnabled(true);

        // This view will represent the main information about
        // the user passed with the intent
        String userId = getIntent().getStringExtra(Utils.TAG_USER_ID);

        // If the user is the user logged then set the image and the name
        // To show immediately some information
        showPartialContentIfUserIsLogged(userId);

        // In any case start downloading the user profile
        // information about the statistics of the user
        downloadUserProfileInformation(userId);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        User userRefreshed = mApplication.getUser();
        String imageString = userRefreshed.getImage();
        Bitmap userImage = null;

        if (imageString != null) {
            userImage = ImageUtils.fromBase64(imageString);
        } else {
            userImage =  BitmapFactory.decodeResource(getResources(),
                    R.drawable.user_no_photo);
        }

        mUserImage.setImageBitmap(userImage);
        mUserName.setText(userRefreshed.getUsername());
    }

    private void showPartialContentIfUserIsLogged(String userId) {
        User userLogged = mApplication.getUser();
        String userLoggedId = userLogged.getId();

        if (userLoggedId.equals(userId)) {
            String imageString = userLogged.getImage();
            Bitmap userImage = null;

            if (imageString != null) {
                userImage = ImageUtils.fromBase64(imageString);
            } else {
                userImage =  BitmapFactory.decodeResource(getResources(),
                        R.drawable.user_no_photo);
            }
            mUserImage.setImageBitmap(userImage);
            mUserName.setText(userLogged.getUsername());
        }
    }

    private void initViewsWithData() {
        String imageString = mUser.getImage();
        Bitmap userImage = null;

        if (imageString != null) {
            userImage = ImageUtils.fromBase64(imageString);
        } else {
            userImage =  BitmapFactory.decodeResource(getResources(),
                    R.drawable.user_no_photo);
        }

        mUserImage.setImageBitmap(userImage);
        mUserName.setText(mUser.getUsername());
        mPhotosNumber.setText(mUser.getNumImages());
        mFriendsNumber.setText(mUser.getNumFriends());
        mMessagesNumber.setText(mUser.getNumMessages());

        changeOptionMenuItemIconBasedOnUserType();
    }

    private void downloadUserProfileInformation(String userId) {
        RESTClient.getUserProfile(userId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    String responseString = new String(responseBody, "UTF-8");

                    mUser = new Gson().fromJson(responseString, User.class);
                    initViewsWithData();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getBaseContext(), "Cannot download the user profile", Toast.LENGTH_SHORT).show();
            }
        });

        // Check friendship exists
    }

    private void editProfile() {
        startActivity(new Intent(this, ProfileEditActivity.class));
    }

    private void addToFriends() {
        String user = mApplication.getUser().getId();
        String friend = mUser.getId();

        RESTClient.createFriendship(user, friend, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getBaseContext(), mUser.getUsername() + " has been added to friends", Toast.LENGTH_SHORT).show();
                changeOptionMenuItemIconBasedOnUserType();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getBaseContext(), "Cannot add " + mUser.getUsername() + " to friends", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeOptionMenuItemIconBasedOnUserType() {
        User userLogged = mApplication.getUser();
        int icon = R.drawable.ic_save;

        if (userLogged.getId().equals(mUser.getId())) {
            icon = R.drawable.ic_toolbar_edit;
        }

        mMenu.findItem(R.id.action_edit_or_save).setIcon(icon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        mMenu = menu;
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_edit_or_save:
                if (mUser.getId().equals(mApplication.getUser().getId())) {
                    editProfile();
                } else {
                    addToFriends();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}