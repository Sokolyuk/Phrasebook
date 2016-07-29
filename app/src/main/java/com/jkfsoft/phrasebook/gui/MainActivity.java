package com.jkfsoft.phrasebook.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jkfsoft.phrasebook.R;
import com.jkfsoft.phrasebook.logic.DBMgr;
import com.jkfsoft.phrasebook.logic.db.DbOpenHelper;
import com.jkfsoft.phrasebook.model.Card;
import com.jkfsoft.phrasebook.model.Lang;
import com.jkfsoft.phrasebook.model.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dmitry Sokolyuk on 26.07.2016.
 */

public class MainActivity extends AppCompatActivity {

    //region global arrays/tables & variables
    private static List<Card> mCards;
    private static List<Tag> mTags;
    private static List<Lang> mLangs;
    private static DbOpenHelper mOpenHelper;
    public static String[] db_script_create;
    public static String[] db_script_drop;
    //endregion

    //region navigation
    private DrawerLayout mDrawerLayout;
    private static ViewPager mViewPager;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db_script_create = getResources().getStringArray(R.array.db_script_create);
        db_script_drop = getResources().getStringArray(R.array.db_script_drop);

        //region app data init & load
        mOpenHelper = DbOpenHelper.getInstance(this);

        reLoadTables(this);

        //endregion


        //region init navigation
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        if (mViewPager != null) {
            setupViewPager(mViewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(v->{
            switch (mViewPager.getCurrentItem()){
                case INavConsts.tabHome:
                    startActivity(new Intent(this, EditCardActivity.class));
                    break;
                case INavConsts.tabTags:
                    startActivity(new Intent(this, EditTagActivity.class));
                    break;
                default:
            }
        });
        //endregion
    }

    public static void reLoadTables(Context context) {
        //load table tag
        DBMgr.selectTags(context);

        //load table tag
        DBMgr.selectCards(context);

        //load table lang
        DBMgr.selectLangs(context);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(i->{
            i.setChecked(true);
            mDrawerLayout.closeDrawers();
            switch(i.getItemId()){
                case R.id.mn_home:
                    mViewPager.setCurrentItem(INavConsts.tabHome);
                    break;
                case R.id.mn_tags:
                    mViewPager.setCurrentItem(INavConsts.tabTags);
                    break;
                case R.id.mn_learning:
                    mViewPager.setCurrentItem(INavConsts.tabLearning);
                    break;
                case R.id.mn_datatools:
                    startActivity(new Intent(this, DataToolsActivity.class));
                    break;
                case R.id.mn_about:
                    startActivity(new Intent(this, AboutActivity.class));
                    break;
                default:
            }
            return true;
        });
    }

    /**
     * Configure navigation
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentHome(), getString(R.string.str_pager_home));
        adapter.addFragment(new FragmentTags(), getString(R.string.str_pager_tags));
        adapter.addFragment(new FragmentLearning(), getString(R.string.str_pager_learning));
        viewPager.setAdapter(adapter);
    }

    /**
     * Adapter-class for navigation
     *
     */
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    //fix bug v.23 - https://code.google.com/p/android/issues/detail?id=183166
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Optimization for Toast message
     *
     * @param context
     * @param mess
     */
    public static void showMess(Context context, String mess) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup)((Activity)context).findViewById(R.id.toast_layout_root));
        TextView text = (TextView)layout.findViewById(R.id.text);
        text.setText(mess);
        Toast toast = new Toast(((Activity)context).getApplicationContext());
        toast.setGravity(Gravity.RIGHT | Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
        Log.d(context.getString(R.string.toast_msg), mess);
    }

    //region getter & setter
    public static List<Card> getCards() {
        return mCards;
    }

    public static void setCards(List<Card> cards) {
        MainActivity.mCards = cards;
        if (FragmentHome.mCardsListViewAdaptor != null) FragmentTags.mTagsListViewAdaptor.notifyDataSetChanged();
    }

    public static List<Tag> getTags() {
        return mTags;
    }

    public static void setTags(List<Tag> tags) {
        MainActivity.mTags = tags;
        if (FragmentTags.mTagsListViewAdaptor != null) FragmentTags.mTagsListViewAdaptor.notifyDataSetChanged();
    }

    public static List<Lang> getLangs() {
        return mLangs;
    }

    public static void setLangs(List<Lang> mLangs) {
        MainActivity.mLangs = mLangs;
    }

    public static DbOpenHelper getmOpenHelper() {
        return mOpenHelper;
    }
    //endregion


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
