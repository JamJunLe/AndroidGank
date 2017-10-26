package com.flyman.app.androidgank.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.flyman.app.androidgank.R;
import com.flyman.app.androidgank.base.activity.SubscriberActivity;
import com.flyman.app.androidgank.io.NetWork;
import com.flyman.app.androidgank.model.bean.About;
import com.flyman.app.androidgank.ui.fragment.BeautyFragment;
import com.flyman.app.androidgank.ui.fragment.DailyFragment;
import com.flyman.app.androidgank.ui.fragment.NavigateArticleFragment;
import com.flyman.app.androidgank.utils.IntentUtil;
import com.flyman.app.util.fragment.FragmentSwitchManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.flyman.app.androidgank.ui.fragment.NavigateArticleFragment.getInstance;

public class MainActivity extends SubscriberActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    private List<Fragment> mFragmentList;
    private FragmentSwitchManager fsm;
    private List<About> mList;
    private MaterialDialog mDialog;
    private LinearLayout aboutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initValAndConfig();
        showFragment(savedInstanceState);
    }

    @Override
    protected void initValAndConfig() {
        toolbar.setTitle(getResources().getString(R.string.main_toolbar_daily));
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        mList = new ArrayList<>();
    }

    private void showFragment(Bundle savedInstanceState) {
        mFragmentList = new ArrayList<>();
        Fragment daily = new DailyFragment();
        Fragment navigateAndroid = getInstance(NavigateArticleFragment.ANDROID);
        Fragment navigateIos = getInstance(NavigateArticleFragment.IOS);
        Fragment navigateWeb = getInstance(NavigateArticleFragment.WEB);
        Fragment navigateExpand = getInstance(NavigateArticleFragment.EXPAND);
        Fragment beautyFragment = new BeautyFragment();
//        Fragment daily2 = new DailyFragmentTest();
        mFragmentList.add(daily);
        mFragmentList.add(navigateAndroid);
        mFragmentList.add(navigateIos);
        mFragmentList.add(navigateWeb);
        mFragmentList.add(navigateExpand);
        mFragmentList.add(beautyFragment);
        fsm = new FragmentSwitchManager(mFragmentList, getSupportFragmentManager(), R.id.fl_container);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            showAboutInfo();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAboutInfo() {
        createAboutInfo();
        mDialog.show();
    }

    private void createAboutInfo() {
        if (mList.size() == 0) {
            //用于设置后续新建button的LayoutParams
            aboutContainer = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.about_container, null, false);
            TextView tv_gank_io = (TextView) aboutContainer.findViewById(R.id.tv_gank_io);
            TextView tv_gank_address = (TextView) aboutContainer.findViewById(R.id.tv_gank_address);
            tv_gank_io.setOnClickListener(this);
            tv_gank_address.setOnClickListener(this);
            tv_gank_io.setText(Html.fromHtml(getResources().getString(R.string.text_about_data_from) + " <font color='#2196f3'><a href=" + ">gank.io/api</a>"));
            tv_gank_address.setText(Html.fromHtml(getResources().getString(R.string.text_about_data_from_github)+" <font color='#2196f3'><a href=" + ">AndroidGank</a>"));
            String[] titles = getResources().getStringArray(R.array.open_source_framework_title);
            String[] urls = getResources().getStringArray(R.array.open_source_framework_url);
            for (int i = 0; i < titles.length; i++) {
                mList.add(new About(titles[i], urls[i]));
                TextView tv = (TextView) LayoutInflater.from(this).inflate(R.layout.item_about_txtview, null, false);
                tv.setText(Html.fromHtml("<font color='#2196f3'><a href=>" + titles[i] + "</a>"));
                aboutContainer.addView(tv);
                tv.setTag(i);
                tv.setOnClickListener(this);
            }
        }
        if (mDialog == null) {
            mDialog = new MaterialDialog.Builder(this)
                    .title(getResources().getString(R.string.action_about))
                    .customView(aboutContainer, true)
                    .positiveText(getResources().getString(R.string.action_about_close))
                    .build();

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (fsm != null) {
            switch (id) {
                case R.id.nav_daily: {
                    fsm.setCurrentFragment(0);
                    toolbar.setTitle(getResources().getString(R.string.main_toolbar_daily));
                    break;
                }
                case R.id.nav_android: {
                    fsm.setCurrentFragment(1);
                    toolbar.setTitle(getResources().getString(R.string.main_toolbar_android));
                    break;
                }
                case R.id.nav_ios: {
                    fsm.setCurrentFragment(2);
                    toolbar.setTitle(getResources().getString(R.string.main_toolbar_ios));
                    break;
                }
                case R.id.nav_web: {
                    fsm.setCurrentFragment(3);
                    toolbar.setTitle(getResources().getString(R.string.main_toolbar_web));
                    break;
                }
                case R.id.nav_expand: {
                    fsm.setCurrentFragment(4);
                    toolbar.setTitle(getResources().getString(R.string.main_toolbar_expand));
                    break;
                }
                case R.id.nav_beauty: {
                    fsm.setCurrentFragment(5);
                    toolbar.setTitle(getResources().getString(R.string.main_toolbar_beauty));
                    break;
                }
                case R.id.nav_about: {
                    showAboutInfo();
//                    toolbar.setTitle(getResources().getString(R.string.main_toolbar_about));
                    break;
                }

            }

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String url;
        switch (id) {
            case R.id.tv_gank_io: {
                url = NetWork.GANK_HOMEPAGE;
                break;
            }
            case R.id.tv_gank_address: {
                url = NetWork.GITHUB_HOMEPAGE;
                break;
            }
            default: {
                int position = (int) v.getTag();
                url = mList.get(position).getUrl();
            }
        }
        IntentUtil.startWebActivity(this, url);
    }
}
