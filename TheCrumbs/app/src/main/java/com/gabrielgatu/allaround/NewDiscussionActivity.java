package com.gabrielgatu.allaround;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielgatu.allaround.libraries.ActionBarCustomActivity;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.gabrielgatu.allaround.utils.APISystemPreferences;
import com.gabrielgatu.allaround.utils.GPSTracker;
import com.gabrielgatu.allaround.utils.StorageManager;
import com.gabrielgatu.allaround.utils.Utils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.melnykov.fab.FloatingActionButton;

import org.apache.http.Header;


public class NewDiscussionActivity extends ActionBarCustomActivity implements View.OnClickListener {

    private EditText mDiscussionTitle;
    private EditText mDiscussionText;
    private FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_discussion);

        // Init actionbar
        initActionBar("New Discussion");
        setToolbarBackButtonEnabled(true);

        // Init UI components
        mDiscussionTitle = (EditText) findViewById(R.id.title);
        mDiscussionText = (EditText) findViewById(R.id.content);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        // Init components
        mDiscussionText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    mFab.performClick();
                    return true;
                }
                return false;
            }
        });

        // Set listeners
        mFab.setOnClickListener(this);
    }

    private void createDiscussion() {
        String discussionTitle = mDiscussionTitle.getText().toString().trim();
        String discussionText = mDiscussionText.getText().toString().trim();

        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        boolean discussionTextIsLongEnough = discussionText.length() > 0;

        if (discussionTextIsLongEnough) {
            String userID = StorageManager.getUser(getBaseContext()).getId();

            RESTClient.createDiscussion(userID, discussionTitle, discussionText, latitude, longitude, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    Toast.makeText(getBaseContext(), getString(R.string.discussion_created_with_success), Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Toast.makeText(getBaseContext(), getString(R.string.error_discussion_creation), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else {
            Toast.makeText(getBaseContext(), "Discussion's text is too short.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_discussion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.fab:
                createDiscussion();
                break;
        }
    }
}
