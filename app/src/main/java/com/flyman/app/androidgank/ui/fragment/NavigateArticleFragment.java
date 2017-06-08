package com.flyman.app.androidgank.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.flyman.app.androidgank.R;
import com.flyman.app.androidgank.adapter.NavigateArticleAdapter;
import com.flyman.app.androidgank.base.fragment.SubscriberFragmemt;
import com.flyman.app.androidgank.base.task.BaseTask;
import com.flyman.app.androidgank.base.task.ListTask;
import com.flyman.app.androidgank.contract.NavigateContract;
import com.flyman.app.androidgank.model.bean.ArticleResult;
import com.flyman.app.androidgank.presenter.NavigatePresenter;
import com.flyman.app.androidgank.utils.IntentUtil;
import com.flyman.app.androidgank.wrapper.ScrollRecyclerView;
import com.flyman.app.util.log.LogUtils;
import com.flyman.app.util.string.ChenkNullUtil;
import com.flyman.app.util.widget.LineItemDecortion;

import org.byteam.superadapter.OnItemClickListener;
import org.byteam.superadapter.SuperAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NavigateArticleFragment extends SubscriberFragmemt implements NavigateContract.View, ScrollRecyclerView.OnScrollCallback, SwipeRefreshLayout.OnRefreshListener, OnItemClickListener {
    private int args;
    public static final String ARGS_KEY = "args_key";
    public static final int ANDROID = 0;
    public static final int IOS = 1;
    public static final int WEB = 2;
    public static final int EXPAND = 3;
    private List<ArticleResult.ResultsBean> mList;
    @Bind(R.id.super_recycler_view)
    ScrollRecyclerView mRecyclerView;
    @Bind(R.id.super_swipe_refresh_view)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private SuperAdapter mAdapter;
    private View footView;
    public static NavigateArticleFragment getInstance(int args) {
        Bundle mBundle = new Bundle();
        mBundle.putInt(ARGS_KEY, args);
        NavigateArticleFragment mNavigateArticleFragment = new NavigateArticleFragment();
        mNavigateArticleFragment.setArguments(mBundle);
        return mNavigateArticleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle mBundle = getArguments();
            args = mBundle.getInt(ARGS_KEY);
        }
        setContentView(R.layout.super_recycler_view_layout);
    }

    @Override
    protected void onContentViewLoad(View container) {
        ButterKnife.bind(this, container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new LineItemDecortion(getActivity()));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mList = new ArrayList<>();
        mAdapter = new NavigateArticleAdapter(getActivity(), mList, R.layout.item_navigate_article_content);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollCallback(this,mAdapter);
        mRecyclerView.setAnimation(null);
        startPresenter();
    }

    @Override
    public void startPresenter() {
        mPresenter = new NavigatePresenter(this, args);
        mPresenter.start(new ListTask(ListTask.Id.PULL_REFRESH, true));//第一次获取数据
    }
    @Override
    public <T extends BaseTask> void showErrorMsg(T t) {
        toastShort(t.getMessage().toString());
    }

    @Override
    public void cleanViewState() {

    }

    @Override
    public <T extends List> void showPullRefreshData(T data) {
        mList.clear();
        mList.addAll(data);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public <T extends List> void showPushLoadMoreData(T data) {
        int positionStart = mList.size() - 1;
        int iteCount = data.size();
        mList.addAll(data);
        int positionEnd = mList.size() - 1;
        mAdapter.notifyItemRangeInserted(positionStart, iteCount);
        if (positionEnd - positionStart > 1) {
            mRecyclerView.scrollToPosition(positionStart);
        }
    }

    @Override
    public void setRefreshEnable(boolean isEnable) {

    }

    @Override
    public void setRefreshing(boolean isRefreshing) {
        mSwipeRefreshLayout.setRefreshing(isRefreshing);
    }

    @Override
    public void setLoadMOreRefreshing(boolean isRefreshing) {
        LogUtils.e("setLoadMOreRefreshing", "setLoadMOreRefreshing--------------isRefreshing = "+isRefreshing);
        if (footView == null) {
            footView = LayoutInflater.from(getActivity()).inflate(R.layout.beauty_layout_footer, null, false);
        }
        if (isRefreshing == true && mAdapter.hasFooterView() == false) {
            mAdapter.addFooterView(footView);
            LogUtils.e("setLoadMOreRefreshing", "addFooterView--------------");
        }
        if (isRefreshing == false && mAdapter.hasFooterView() == true) {
            mAdapter.removeFooterView();
            LogUtils.e("setLoadMOreRefreshing", "removeFooterView--------------");
        }
    }

    @Override
    public void onScrollToTop() {

    }
    @Override
    public void onScrollToBottom() {
        mPresenter.start(new ListTask(ListTask.Id.PUSH_LOAD_MORE_REFRESH, false));
    }

    @Override
    public void onRefresh() {
        mPresenter.start(new ListTask(ListTask.Id.PULL_REFRESH, false));
    }

    @Override
    public void onItemClick(View itemView, int viewType, int position) {
        LogUtils.e("onItemClick", "viewType = default");
        String url = ChenkNullUtil.getNullString(mList.get(position).getUrl());
        IntentUtil.startWebActivity(getContext(),url);
    }
}
