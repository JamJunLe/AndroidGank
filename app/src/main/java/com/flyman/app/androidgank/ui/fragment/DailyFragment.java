package com.flyman.app.androidgank.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.flyman.app.androidgank.R;
import com.flyman.app.androidgank.adapter.DailyAdapter;
import com.flyman.app.androidgank.base.fragment.SubscriberFragmemt;
import com.flyman.app.androidgank.base.task.BaseTask;
import com.flyman.app.androidgank.base.task.ListTask;
import com.flyman.app.androidgank.contract.DailyContract;
import com.flyman.app.androidgank.model.bean.ArticleResult;
import com.flyman.app.androidgank.model.bean.DayHistoryArticleCompound;
import com.flyman.app.androidgank.presenter.DailyPresenter;
import com.flyman.app.androidgank.utils.IntentUtil;
import com.flyman.app.androidgank.wrapper.ScrollRecyclerView;
import com.flyman.app.util.log.LogUtils;
import com.flyman.app.util.string.ChenkNullUtil;

import org.byteam.superadapter.OnItemClickListener;
import org.byteam.superadapter.SuperAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class DailyFragment extends SubscriberFragmemt implements SwipeRefreshLayout.OnRefreshListener, ScrollRecyclerView.OnScrollCallback, DailyContract.View, OnItemClickListener {
    @Bind(R.id.super_recycler_view)
    ScrollRecyclerView mRecyclerView;
    @Bind(R.id.super_swipe_refresh_view)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private List<DayHistoryArticleCompound> mList;
    private SuperAdapter mAdapter;
    private View footView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_recycler_view_layout);
    }

    @Override
    protected void onContentViewLoad(View container) {
        ButterKnife.bind(this, container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mList = new ArrayList<>();
        mAdapter = new DailyAdapter(getActivity(), mList, null);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollCallback(this, mAdapter);
        startPresenter();

    }

    @Override
    public void onRefresh() {
        mPresenter.start(new ListTask(ListTask.Id.PULL_REFRESH));
    }

    @Override
    public void onScrollToTop() {

    }

    @Override
    public void onScrollToBottom() {
        mPresenter.start(new ListTask(ListTask.Id.PUSH_LOAD_MORE_REFRESH));
    }

    @Override
    public void startPresenter() {
        mPresenter = new DailyPresenter(this);
        mPresenter.start(new ListTask(ListTask.Id.PULL_REFRESH, true));
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
        if (footView == null) {
            footView = LayoutInflater.from(getActivity()).inflate(R.layout.beauty_layout_footer, null, false);
        }
        if (isRefreshing == true && mAdapter.hasFooterView() == false) {
            mAdapter.addFooterView(footView);
        }
        if (isRefreshing == false && mAdapter.hasFooterView() == true) {
            mAdapter.removeFooterView();
        }
    }

    @Override
    public void onItemClick(View itemView, int viewType, int position) {
        LogUtils.e("onItemClick", "viewType = "+viewType+ " position ="+position);
        switch (viewType) {
            case DayHistoryArticleCompound.Type.date: {
                LogUtils.e("onItemClick", "viewType = date");
                break;
            }
            case DayHistoryArticleCompound.Type.beauty: {
                LogUtils.e("onItemClick", "viewType = beauty");
                String url = ChenkNullUtil.getNullString(mList.get(position).getArticle().getUrl());
                List<ArticleResult.ResultsBean> imageList= new ArrayList<>();
                imageList.add(new ArticleResult.ResultsBean(url));
                IntentUtil.startImageActivity(getContext(),imageList,0);
                break;
            }
            default: {
                LogUtils.e("onItemClick", "viewType = default");
                String url = ChenkNullUtil.getNullString(mList.get(position).getArticle().getUrl());
                IntentUtil.startWebActivity(getContext(),url);
            }
        }
    }
}
