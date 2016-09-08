package com.gabrielgatu.allaround.libraries;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gabrielgatu.allaround.R;
import com.gabrielgatu.allaround.drawer.FriendsActivity;
import com.gabrielgatu.allaround.drawer.ProfileActivity;

/**
 * Created by gabrielgatu on 25/05/15.
 */
public class ActionBarCustomActivity extends ActionBarActivity {

    private Toolbar mToolbar;
    private TextView mTitleTextView;

    public void initActionBar(String title) {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

        mTitleTextView = (TextView) mToolbar.findViewById(R.id.text);
        setTitle(title);
    }

    public void setToolbarBackButtonEnabled(boolean condition) {
        if (condition) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    public Toolbar getToolbar() {
        return mToolbar;
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
}
