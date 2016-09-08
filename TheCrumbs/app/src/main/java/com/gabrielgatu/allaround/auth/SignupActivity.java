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

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

public class SignupActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView mSigninLink;

    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    private Button mSignupButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI components
        mSigninLink = (TextView) findViewById(R.id.sign_in_link);
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mEmail = (EditText) findViewById(R.id.email);
        mSignupButton = (Button) findViewById(R.id.sign_up);

        // Add listeners
        mSigninLink.setOnClickListener(this);
        mSignupButton.setOnClickListener(this);
    }

    private void signUp() {
        String username = mUsername.getText().toString().trim();
        String password = mPassword.getText().toString().trim();
        String email = mEmail.getText().toString().trim();

        RESTClient.signUp(username, password, email, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Intent intent = new Intent(getBaseContext(), SigninActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("USER", "NOT REGISTERED");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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
            case R.id.sign_up:
                signUp();
                break;
            case R.id.sign_in_link:
                startActivity(new Intent(this, SigninActivity.class));
                break;
        }
    }
}
