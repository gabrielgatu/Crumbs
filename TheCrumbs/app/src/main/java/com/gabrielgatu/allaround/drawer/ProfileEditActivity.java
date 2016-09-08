package com.gabrielgatu.allaround.drawer;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.application.MainApplication;
import com.gabrielgatu.allaround.beans.User;
import com.gabrielgatu.allaround.libraries.ActionBarCustomActivity;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.gabrielgatu.allaround.utils.ImageUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.melnykov.fab.FloatingActionButton;

import org.apache.http.Header;

public class ProfileEditActivity extends ActionBarCustomActivity implements View.OnClickListener {

    // this is the action code we use in our intent,
    // this way we know we're looking at the response from our own action
    private static final int SELECT_PICTURE = 1;

    private ImageView mUserImage;
    private TextView mUserName;
    private TextView mUserEmail;

    private FloatingActionButton mFab;
    private MainApplication mApplication;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        // Get the application
        mApplication = (MainApplication) getApplication();
        mUser = mApplication.getUser();

        // Init actionbar
        initActionBar("Edit Profile");
        setToolbarBackButtonEnabled(true);

        // Init components
        mUserImage = (ImageView) findViewById(R.id.user_image);
        mUserName = (TextView) findViewById(R.id.user_name);
        mUserEmail = (TextView) findViewById(R.id.user_email);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        // init
        initViewsWithData();

        // Setting listeners
        mUserImage.setOnClickListener(this);
        mFab.setOnClickListener(this);
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
        mUserEmail.setText(mUser.getEmail());
    }

    private void saveUserProfileData() {
        final String image = mUser.getImage();
        final String username = mUserName.getText().toString().trim();
        final String email = mUserEmail.getText().toString().trim();

        // UPDATE IMAGE
        RESTClient.updateUserImage(mUser.getId(), image, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {}

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });

        // UPDATE USERNAME
        RESTClient.updateUserName(mUser.getId(), username, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {}

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });

        // UPDATE EMAIL
        RESTClient.updateUserEmail(mUser.getId(), email, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {}

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {}
        });

        // UPDATE USER AND SAVE IN APP
        mUser.setUsername(username);
        mUser.setEmail(email);
        mApplication.setUser(mUser);

        // Close the activity
        finish();
    }

    private void newUserImage(final Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();

        int newWidth = width;
        int newHeight = height;

        while(newWidth > 250 && newHeight > 250) {
            newWidth /= 2;
            newHeight /= 2;
        }

        final Bitmap imageResized = ImageUtils.resizeImage(image, newWidth, newHeight);
        final String imageString = ImageUtils.toBase64(imageResized);
        mUser.setImage(imageString);
        mUserImage.setImageBitmap(imageResized);
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                String selectedImagePath = getPath(selectedImageUri);

                if(selectedImagePath!=null){
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    newUserImage(bitmap);
                }
                else{
                    loadPicasaImageFromGallery(selectedImageUri);
                }
            }
        }
    }

    private void loadPicasaImageFromGallery(final Uri uri) {
        String[] projection = {  MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
            if (columnIndex != -1) {
                new Thread(new Runnable() {
                    // NEW THREAD BECAUSE NETWORK REQUEST WILL BE MADE THAT WILL BE A LONG PROCESS & BLOCK UI
                    // IF CALLED IN UI THREAD
                    public void run() {
                        try {
                            final Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    newUserImage(bitmap);
                                }
                            });
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }).start();
            }
        }
        cursor.close();
    }


    public String getPath(Uri uri) {
        String[] projection = {  MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if(cursor != null) {
            //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        }
        else
            return uri.getPath();               // FOR OI/ASTRO/Dropbox etc
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.user_image:
                openImagePicker();
                break;
            case R.id.fab:
                saveUserProfileData();
                break;
        }
    }
}
