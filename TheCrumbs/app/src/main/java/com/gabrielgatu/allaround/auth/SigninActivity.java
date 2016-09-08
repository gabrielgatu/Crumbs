package com.gabrielgatu.allaround.auth;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gabrielgatu.allaround.MainActivity;
import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.application.MainApplication;
import com.gabrielgatu.allaround.beans.Discussion;
import com.gabrielgatu.allaround.beans.User;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.gabrielgatu.allaround.utils.APISystemPreferences;
import com.gabrielgatu.allaround.utils.StorageManager;
import com.gabrielgatu.allaround.utils.Utils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public class SigninActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView mSignupLink;

    private EditText mUsername;
    private EditText mPassword;
    private Button mSignIn;
    private TextView mLostCredentialsLink;
    private MainApplication mApplication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Get the Application
        mApplication = (MainApplication) getApplication();

        // Before all, check if the user
        // is logged.
        // If true then redirect to home, without showing
        // the logging page.
        checkIfUserIsLogged();

        // Initialize UI components
        mSignupLink = (TextView) findViewById(R.id.sign_up_link);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mSignIn = (Button) findViewById(R.id.sign_in);
        mLostCredentialsLink = (TextView) findViewById(R.id.lost_credentials_link);

        // Add listeners
        mSignupLink.setOnClickListener(this);
        mLostCredentialsLink.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onResume();

        User user = mApplication.getUser();
        if (user != null) {
            finish();
        }
    }

    private void checkIfUserIsLogged() {
        User user = mApplication.getUser();

        if (user != null) {
            mApplication.setUser(user);

            redirectToHome();
        }
    }

    private void redirectToHome() {
        startActivity(new Intent(getBaseContext(), MainActivity.class));
    }

    private void signIn() {
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();

        RESTClient.signIn(username, password, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {

                    String responseString = new String(responseBody, "UTF-8");

                    // Saving the user in json format
                    mApplication.setUser(responseString);

                    redirectToHome();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getBaseContext(), "Error during the sign in!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
            case R.id.sign_in:
                signIn();
                break;
            case R.id.sign_up_link:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            case R.id.lost_credentials_link:
                break;
        }
    }
}
