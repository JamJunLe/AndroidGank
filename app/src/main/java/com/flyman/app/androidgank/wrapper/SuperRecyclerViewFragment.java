package com.flyman.app.androidgank.wrapper;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.flyman.app.androidgank.R;
import com.flyman.app.util.fragment.LazyFragment;
import com.flyman.app.util.log.LogUtils;

import org.byteam.superadapter.OnItemClickListener;
import org.byteam.superadapter.SuperAdapter;


public abstract class SuperRecyclerViewFragment extends LazyFragment implements ScrollRecyclerView.OnScrollCallback, SwipeRefreshLayout.OnRefreshListener {
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected ScrollRecyclerView mRecyclerView;
    protected SuperAdapter mAdapter;
    protected View footView;
    protected View headView;
    protected int defaultFooterView = R.layout.beauty_layout_footer;
    protected int defaultHeaderView = R.layout.layout_header;
    @Override
    protected void onContentViewLoad(View container) {
        mSwipeRefreshLayout = (SwipeRefreshLayout) container.findViewById(R.id.super_swipe_refresh_view);
        mRecyclerView = (ScrollRecyclerView) container.findViewById(R.id.super_recycler_view);
        mRecyclerView.setOnScrollCallback(this,mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        setRecyclerAndAdapter(mRecyclerView,mAdapter);
        //创建默认的头和尾文件,如需自定义,请调用addHeaderView和addFootView方法
//        setHeaderView(defaultHeaderView);
        setFooterView(defaultFooterView);
    }

    /**
     * 提供给子类设置RecyclerView
     *
     * @param mRecyclerView ScrollRecyclerView
     * @return nothing
     */
    protected abstract void setRecyclerAndAdapter(ScrollRecyclerView mRecyclerView,SuperAdapter adapter);



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



    protected void removeFooterView() {
        if (mAdapter != null && footView != null && !mAdapter.hasFooterView()) {
            mAdapter.removeFooterView();
        }
    }

    protected void removeHeaderView() {
        if (mAdapter != null && footView != null && !mAdapter.hasHeaderView()) {
            mAdapter.removeHeaderView();
        }
    }

    protected View setHeaderView(int layoutHeader)
    {
        this.headView = LayoutInflater.from(getActivity()).inflate(layoutHeader, null, false);
        return headView;
    }

    protected View setFooterView(int layoutFooter)
    {
        this.footView = LayoutInflater.from(getActivity()).inflate(layoutFooter, null, false);
        return footView;
    }

    protected void OnItemClick(View itemView, int viewType, int position) {
    }

    protected void showFooterView(boolean visibility)
    {
        if (mAdapter != null && footView!= null)
        {
           if (visibility == true&&!mAdapter.hasFooterView())
           {
               mAdapter.addFooterView(footView);
           }
           if(visibility == false&&mAdapter.hasFooterView()){
               mAdapter.removeFooterView();
           }
        }
    }

    protected void showHeaderView()
    {
        if (mAdapter != null && footView!= null)
        {
            mAdapter.addHeaderView(headView);
        }
    }

    protected void showSwipeView(boolean isRefreshing)
    {
        if (mSwipeRefreshLayout != null)
        {
            LogUtils.e("setRefreshing =========="+isRefreshing);
            mSwipeRefreshLayout.setRefreshing(isRefreshing);
        }
    }


    protected void hideFooterView()
    {
        removeFooterView();
    }

    protected void hideHeaderView()
    {
        removeHeaderView();
    }
    protected void setItemClickListener() {
        if (mAdapter != null) {
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View itemView, int viewType, int position) {
                    OnItemClick(itemView, viewType, position);
                }
            });
        }

    }


    @Override
    public void onScrollToTop() {

    }

    @Override
    public void onScrollToBottom() {
        onPushLoadMore();
    }


    @Override
    public void onRefresh() {
        onPullRefresh();
    }
}
