package com.flyman.app.util.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.flyman.app.util.R;
import com.flyman.app.util.widget.SuperSwipeRefreshLayout;

/**
 * @author Flyman
 * @ClassName RefreshRecyclerFragment
 * @description 带下拉刷新与上拉加载的Fragment
 * @date 2017-4-30 1:18
 */
public abstract class RefreshRecyclerFragment extends LazyFragment implements SuperSwipeRefreshLayout.OnPullRefreshListener, SuperSwipeRefreshLayout.OnPushLoadMoreListener {
    protected View headView;
    protected View footerView;
    protected TextView tv_head;//下拉头信息
    protected ProgressBar pb_head;//下拉头进度条
    protected ImageView iv_head;//下拉进度条图片

    protected TextView tv_footer;//上拉头信息
    protected ProgressBar pb_footer;//上拉头进度条
    protected ImageView iv_footer;//上拉进度条图片
    protected ProgressBar pb_loading;//页面中央的进度条,用以显示第一次的加载进度
    protected SuperSwipeRefreshLayout swipeRefreshLayout;
    protected RecyclerView recyclerView;

    @Override
    protected void onContentViewLoad(View container) {
        swipeRefreshLayout = (SuperSwipeRefreshLayout) container.findViewById(R.id.swipe_view);
        recyclerView = (RecyclerView) container.findViewById(R.id.recy_view);
//        pb_loading = (ProgressBar) container.findViewById(R.id.pb_loading);
        //设置swipeRefreshLayout
        swipeRefreshLayout.setHeaderView(createHeadView());
        swipeRefreshLayout.setFooterView(createFootView());
        swipeRefreshLayout.setOnPullRefreshListener(this);
        swipeRefreshLayout.setOnPushLoadMoreListener(this);
        swipeRefreshLayout.setEnabled(true);
        setRecyclerView(recyclerView);
        setAdapter(recyclerView);
    }

    protected View createHeadView() {
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_header, null);
        tv_head = (TextView) headView.findViewById(R.id.tv_head);
        pb_head = (ProgressBar) headView.findViewById(R.id.pb_head);
        iv_head = (ImageView) headView.findViewById(R.id.iv_head);
        return headView;

    }

    protected View createFootView() {
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_footer, null);
        tv_footer = (TextView) footerView.findViewById(R.id.tv_footer);
        pb_footer = (ProgressBar) footerView.findViewById(R.id.pb_footer);
        iv_footer = (ImageView) footerView.findViewById(R.id.iv_footer);
        return footerView;
    }

    /**
     * 提供给子类设置recyclerView
     * //设置adapter
     mTopicList = new ArrayList<>();
     mTopicAdapter = new TopicAdapter(mTopicList, getActivity(), this);
     recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
     recyclerView.setAdapter(mTopicAdapter);
     recyclerView.addItemDecoration(new LineItemDecortion(getActivity()));
     mTopicPresenter = new TopicPresenter(this);
     * @param
     * @return nothing
     */
    protected abstract void setRecyclerView(RecyclerView recyclerView);

    protected abstract void setAdapter(RecyclerView recyclerView);
    /**
     * 提供给子类刷新操作
     *
     * @param
     * @return nothing
     */
    protected abstract void onPullRefresh();

    /**
     * 提供给子类上拉刷新操作
     *
     * @param
     * @return nothing
     */
    protected abstract void onPushLoadMore();



    @Override
    public void onRefresh() {
        tv_head.setText("正在刷新");
        tv_head.setVisibility(View.VISIBLE);
        pb_head.setVisibility(View.GONE);
        pb_head.setVisibility(View.VISIBLE);
        onPullRefresh();
    }

    @Override
    public void onPullDistance(int distance) {

    }

    @Override
    public void onPullEnable(boolean enable) {
        tv_head.setText(enable ? "松开刷新" : "下拉刷新");
        iv_head.setVisibility(View.VISIBLE);
        pb_head.setVisibility(View.GONE);
        iv_head.setRotation(enable ? 180 : 0);
    }

    @Override
    public void onLoadMore() {
        tv_footer.setText("正在加载...");
        tv_footer.setVisibility(View.VISIBLE);
        iv_footer.setVisibility(View.GONE);
        pb_footer.setVisibility(View.VISIBLE);
        onPushLoadMore();
    }

    @Override
    public void onPushDistance(int distance) {
    }

    @Override
    public void onPushEnable(boolean enable) {
        tv_footer.setText(enable ? "松开加载" : "上拉加载");
        iv_footer.setVisibility(View.VISIBLE);
        pb_footer.setVisibility(View.GONE);
        iv_footer.setRotation(enable ? 0 : 180);

    }
}
