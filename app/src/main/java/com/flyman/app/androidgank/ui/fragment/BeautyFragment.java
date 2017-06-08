package com.flyman.app.androidgank.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import com.flyman.app.androidgank.R;
import com.flyman.app.androidgank.adapter.BeautyAdapter;
import com.flyman.app.androidgank.base.fragment.SubscriberFragmemt;
import com.flyman.app.androidgank.base.task.BaseTask;
import com.flyman.app.androidgank.base.task.ListTask;
import com.flyman.app.androidgank.contract.BeautyContract;
import com.flyman.app.androidgank.model.bean.ArticleResult;
import com.flyman.app.androidgank.presenter.BeautyPresenter;
import com.flyman.app.androidgank.utils.IntentUtil;
import com.flyman.app.androidgank.wrapper.ScrollRecyclerView;
import com.flyman.app.util.log.LogUtils;

import org.byteam.superadapter.OnItemClickListener;
import org.byteam.superadapter.SuperAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class BeautyFragment extends SubscriberFragmemt implements BeautyContract.View, SwipeRefreshLayout.OnRefreshListener, ScrollRecyclerView.OnScrollCallback, OnItemClickListener {
    @Bind(R.id.super_recycler_view)
    ScrollRecyclerView mRecyclerView;
    @Bind(R.id.super_swipe_refresh_view)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private SuperAdapter mAdapter;
    private List<ArticleResult.ResultsBean> mList;
    private BeautyPresenter mPresenter;
    private View footView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_recycler_view_layout);
    }

    @Override
    protected void onContentViewLoad(View container) {
        ButterKnife.bind(this, container);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mList = new ArrayList<>();
        mAdapter = new BeautyAdapter(getActivity(), mList, R.layout.item_beautyfragment_beauty);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollCallback(this, mAdapter);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.getItemAnimator().setChangeDuration(0);//将动画禁用
        mRecyclerView.setItemAnimator(null);

//        mSwipeRefreshLayout.setOnRefreshListener(this);
//        mList = new ArrayList<>();
//        mAdapter = new TestAdapter(getActivity(), mList);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setOnScrollCallback(this, mAdapter);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimatorFix());
//        mRecyclerView.setAnimation(null);
        startPresenter();
    }

    @Override
    public void startPresenter() {
        mPresenter = new BeautyPresenter(this);
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
//        mAdapter.notifyDataSetHasChanged();

    }

    @Override
    public <T extends List> void showPushLoadMoreData(T data) {
        int positionStart = mList.size() - 1;
        int iteCount = data.size();
        mList.addAll(data);
        int positionEnd = mList.size() - 1;
        mAdapter.notifyItemRangeInserted(positionStart, iteCount);
//        if (positionEnd - positionStart > 1) {
//            mRecyclerView.scrollToPosition(positionStart+1);
//        }
//        mList.addAll(data);
//        mAdapter.notifyDataSetChanged();

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
        LogUtils.e("setLoadMOreRefreshing", "setLoadMOreRefreshing--------------isRefreshing = " + isRefreshing);
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
        if (mList.size() > 0) {
            IntentUtil.startImageActivity(getActivity(),mList,position);
        }
    }
}

