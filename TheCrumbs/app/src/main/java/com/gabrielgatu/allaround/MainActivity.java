package com.gabrielgatu.allaround;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.gabrielgatu.allaround.application.MainApplication;
import com.gabrielgatu.allaround.auth.SigninActivity;
import com.gabrielgatu.allaround.beans.User;
import com.gabrielgatu.allaround.drawer.FriendsActivity;
import com.gabrielgatu.allaround.drawer.ProfileActivity;
import com.gabrielgatu.allaround.fragments.ChatFragment;
import com.gabrielgatu.allaround.fragments.DiscussionListFragment;
import com.gabrielgatu.allaround.fragments.MediaFragment;
import com.gabrielgatu.allaround.libraries.ActionBarCustomActivity;
import com.gabrielgatu.allaround.rest.RESTClient;
import com.gabrielgatu.allaround.utils.GPSTracker;
import com.gabrielgatu.allaround.utils.ImageUtils;
import com.gabrielgatu.allaround.utils.StorageManager;
import com.gabrielgatu.allaround.utils.Utils;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;


public class MainActivity extends ActionBarCustomActivity implements View.OnClickListener {

    private static final int DRAWER_LIST_ITEM_HOME = 0;
    private static final int DRAWER_LIST_ITEM_PROFILE = 1;
    private static final int DRAWER_LIST_ITEM_FRIENDS = 2;
    private static final int DRAWER_LIST_ITEM_ALCHALLENGE = 3;
    private static final int DRAWER_LIST_ITEM_SETTINGS = 4;

    private static final int PAGER_CRUMBS = 0;
    private static final int PAGER_CHAT = 1;
    private static final int PAGER_DISCUSSIONS = 2;

    private MainApplication mApplication;
    private User mUser;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    private TextView mToolbarSubtext;
    private ViewPager mPager;
    private PagerSlidingTabStrip mTabs;

    private ImageView mUserImageFront;
    private ImageView mUserImageBack;
    private TextView mUserEmail;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the user from storage
        mApplication = (MainApplication) getApplication();
        mUser = mApplication.getUser();

        // Init components
        mToolbarSubtext = (TextView) findViewById(R.id.subtext);
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.pager);

        // Add listeners

        // Init components
        initActionBar(getString(R.string.app_name));
        initDrawerLayout();
        initPager();
        initToolbarSubtext();

        // Set the chat as the home page
        mPager.setCurrentItem(PAGER_CHAT);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mUser = mApplication.getUser();
        String imageString = mUser.getImage();
        Bitmap userImage = null;

        if (imageString != null) {
            userImage = ImageUtils.fromBase64(imageString);
        } else {
            userImage =  BitmapFactory.decodeResource(getResources(),
                    R.drawable.user_no_photo);
        }

        mUserImageFront.setImageBitmap(userImage);
        mUserImageBack.setImageBitmap(userImage);
        mUserEmail.setText(mUser.getEmail());
    }

    public void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);;
        initNavigationDrawer();
    }

    private void initNavigationDrawer() {
        String imageString = mUser.getImage();
        Bitmap userImage = null;

        if (imageString != null) {
            userImage = ImageUtils.fromBase64(imageString);
        } else {
            userImage =  BitmapFactory.decodeResource(getResources(),
                    R.drawable.user_no_photo);
        }

        FrameLayout slidingMenuContainer = (FrameLayout) findViewById(R.id.sliding_menu_container);
        View slidingMenuView = getLayoutInflater().inflate(R.layout.sliding_menu, slidingMenuContainer, false);

        /* Find Views inside the drawer */

        // User name
        TextView userName = (TextView) slidingMenuView.findViewById(R.id.user_name);
        userName.setText( mUser.getUsername() );

        // User email
        mUserEmail = (TextView) slidingMenuView.findViewById(R.id.user_email);
        mUserEmail.setText( mUser.getEmail() );

        // User image
        mUserImageFront = (ImageView) slidingMenuView.findViewById(R.id.user_image);
        mUserImageFront.setImageBitmap(userImage);

        // User image back
        mUserImageBack = (ImageView) slidingMenuView.findViewById(R.id.user_image_back);
        mUserImageBack.setImageBitmap(userImage);

        // Logout button
        Button mLogout = (Button) slidingMenuView.findViewById(R.id.logout);
        mLogout.setOnClickListener(this);

        View headerDrawer = slidingMenuView.findViewById(R.id.drawer_header);
        headerDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewProfile();
            }
        });

        ListView slidingMenuList = (ListView) slidingMenuView.findViewById(R.id.sliding_menu_list);
        SlidingMenuListAdapter slidingMenuListAdapter = new SlidingMenuListAdapter();
        slidingMenuList.setAdapter(slidingMenuListAdapter);
        slidingMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case DRAWER_LIST_ITEM_HOME:
                        break;
                    case DRAWER_LIST_ITEM_PROFILE:
                        viewProfile();
                        break;
                    case DRAWER_LIST_ITEM_FRIENDS:
                        startActivity(new Intent(getBaseContext(), FriendsActivity.class));
                        break;
                    case DRAWER_LIST_ITEM_ALCHALLENGE:
                        break;
                    case DRAWER_LIST_ITEM_SETTINGS:
                        break;
                }
            }
        });

        slidingMenuContainer.addView(slidingMenuView);

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                getToolbar(),
                R.string.drawer_open,
                R.string.drawer_close){
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
                invalidateOptionsMenu();
                syncState();
            }
            public void onDrawerOpened(View v){
                super.onDrawerOpened(v);
                invalidateOptionsMenu();
                syncState();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    private void initToolbarSubtext() {
        GPSTracker gps = new GPSTracker(this);
        double latitude = gps.getLatitude();
        double longitude = gps.getLongitude();

        RESTClient.getUsersInArea(latitude, longitude, true, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String responseString = new String(responseBody, "UTF-8");

                    String numOfUsers = new Gson().fromJson(responseString, String.class);
                    mToolbarSubtext.setText(numOfUsers + " People connected in this area");

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void viewProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(Utils.TAG_USER_ID, mUser.getId());
        startActivity(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        if (mDrawerLayout != null) {
            mDrawerToggle.syncState();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);

        if (mDrawerLayout != null) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    private void initPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mTabs.setViewPager(mPager);
    }

    private void logout() {
        MainApplication application = (MainApplication) getApplication();
        application.removeUser();

        Intent intent = new Intent(this, SigninActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.logout:
                logout();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;

            switch(position) {
                case 0:
                    fragment = new MediaFragment();
                    break;
                case 1:
                    fragment = new ChatFragment();
                    break;
                case 2:
                    fragment = new DiscussionListFragment();
                    break;
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";

            switch(position) {
                case PAGER_CRUMBS:
                    title = "Crumbs";
                    break;
                case PAGER_CHAT:
                    title = "Chat";
                    break;
                case PAGER_DISCUSSIONS:
                    title = "Discussions";
                    break;
            }

            return title;
        }
    }

    private class SlidingMenuListAdapter extends BaseAdapter {

        class ListItem {
            int image;
            String text;

            public ListItem(int image, String text) {
                this.image = image;
                this.text = text;
            }
        }

        private ListItem[] items;

        public SlidingMenuListAdapter() {
            this.items = new ListItem[5];

            this.items[DRAWER_LIST_ITEM_HOME] = new ListItem(R.drawable.ic_sliding_home, "Home");
            this.items[DRAWER_LIST_ITEM_PROFILE] = new ListItem(R.drawable.ic_sliding_profile, "Profile");
            this.items[DRAWER_LIST_ITEM_FRIENDS] = new ListItem(R.drawable.ic_sliding_friends, "Friends");
            this.items[DRAWER_LIST_ITEM_ALCHALLENGE] = new ListItem(R.drawable.ic_sliding_allchallenge, "AllChallenge");
            this.items[DRAWER_LIST_ITEM_SETTINGS] = new ListItem(R.drawable.ic_sliding_options, "Settings");
        }

        @Override
        public int getCount() {
            return this.items.length;
        }

        @Override
        public Object getItem(int i) {
            return this.items[i];
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {
            View view = convertView;
            ViewHolder holder;

            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.partial_list_sliding_menu, parent, false);

                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            else {
                holder = (ViewHolder) view.getTag();
            }

            holder.image.setImageResource(items[i].image);
            holder.text.setText(items[i].text);

            return view;
        }

        class ViewHolder {
            ImageView image;
            TextView text;

            ViewHolder(View root) {
                image = (ImageView) root.findViewById(R.id.image);
                text = (TextView) root.findViewById(R.id.text);
            }
        }
    }
}
