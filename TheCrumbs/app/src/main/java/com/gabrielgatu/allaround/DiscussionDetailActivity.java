package com.gabrielgatu.allaround;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.gabrielgatu.allaround.fragments.DiscussionChatFragment;
import com.gabrielgatu.allaround.fragments.DiscussionDetailFragment;
import com.gabrielgatu.allaround.libraries.ActionBarCustomActivity;
import com.gabrielgatu.allaround.utils.Utils;
import com.gabrielgatu.allaround.fragments.DiscussionListFragment;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;


public class DiscussionDetailActivity extends ActionBarCustomActivity {

    private Toolbar mToolbar;

    private PagerSlidingTabStrip mTabs;
    private ViewPager mPager;
    private ViewPagerAdapter mAdapter;

    private String mDiscussionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_detail);

        // Init actionbar
        initActionBar("Discussion");
        setToolbarBackButtonEnabled(true);

        // Init UI components
        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.pager);

        // Get the discussion id and pass it
        // to the fragments
        mDiscussionID = getIntent().getStringExtra(DiscussionListFragment.TAG_DISCUSSION_ID);

        // Init
        initPagerAdapter();
    }

    private void initPagerAdapter() {
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mTabs.setViewPager(mPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_discussion_detail, menu);
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

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new Fragment();

            switch(position) {
                case 0:
                    fragment = new DiscussionDetailFragment();
                    break;
                case 1:
                    fragment = new DiscussionChatFragment();
                    break;
            }

            Bundle params = new Bundle();
            params.putString(Utils.TAG_USER_ID, mDiscussionID);
            fragment.setArguments(params);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";

            switch(position) {
                case 0:
                    title = "Details";
                    break;
                case 1:
                    title = "Chat";
                    break;
            }

            return title;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}