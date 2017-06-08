package com.flyman.app.androidgank.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.flyman.app.androidgank.R;
import com.flyman.app.androidgank.adapter.ShowImageAdapter;
import com.flyman.app.androidgank.base.activity.SubscriberActivity;
import com.flyman.app.androidgank.model.bean.ArticleResult;
import com.flyman.app.androidgank.utils.IntentUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ImageActivity extends SubscriberActivity implements ViewPager.OnPageChangeListener {

    private List<ArticleResult.ResultsBean> mList;
    private int position;
    @Bind(R.id.vp_activity_image)
    ViewPager mViewPager;
    @Bind(R.id.tv_activity_image_position)
    TextView mNumTextView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private ShowImageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);
        initValAndConfig();
        intiToolbar();
        initViewPage();
    }

    @Override
    protected void initValAndConfig() {
        Intent mIntent = getIntent();
        if (mIntent != null) {
            mList = (List<ArticleResult.ResultsBean>) mIntent.getExtras().getSerializable(IntentUtil.IMAGE_ARRAY_LIST);
            position = mIntent.getExtras().getInt(IntentUtil.IMAGE_ARRAY_LIST_POSITION, 0);
        }
        mNumTextView.setText(position + 1 + "/" + mList.size());

    }

    private void initViewPage() {
        mAdapter = new ShowImageAdapter(mList, this);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(mAdapter);
        if (position >= 0) {
            mViewPager.setCurrentItem(position);
        }

    }

    private void intiToolbar() {
        mToolbar.setTitle("福利");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.toolbarTransparent));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mNumTextView.setText(position + 1 + "/" + mList.size());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
