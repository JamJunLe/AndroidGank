package com.flyman.app.androidgank.wrapper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.flyman.app.util.log.LogUtils;

/**
 * @author Flyman
 * @ClassName ScrollRecyclerView
 * @description 实现了滚动状态回调的RecyclerView
 * @date 2017-5-9 22:16
 */
public class ScrollRecyclerView extends RecyclerView {
    protected int lastVisibleItem;//最后一个可见的item
    protected LinearLayoutManager mLinearLayoutManager;//
    protected GridLayoutManager mGridLayoutManager;//
    protected StaggeredGridLayoutManager mStaggeredGridLayoutManager;//

    public ScrollRecyclerView(Context context) {
        super(context);
    }

    public ScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollCallback(final OnScrollCallback callback, final Adapter adapter) {
        if (callback == null) {
            return;
        }
        final LayoutManager layoutManager = getLayoutManager();
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                LogUtils.e("lastVisibleItem", "adapter.getItemCount()"+adapter.getItemCount());
//                LogUtils.e("lastVisibleItem", "lastVisibleItem = "+lastVisibleItem);
                //RecyclerView.canScrollVertically(1)的值表示是否能向下滚动，false表示已经滚动到底部
                //RecyclerView.canScrollVertically(-1)的值表示是否能向上滚动，false表示已经滚动到顶部
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == adapter.getItemCount() && !recyclerView.canScrollVertically(1)) {
                    callback.onScrollToBottom();
                }
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (!recyclerView.canScrollVertically(1)) {
//                        callback.onScrollToBottom();
//                    }
//                    if (!recyclerView.canScrollVertically(-1)) {
//                        callback.onScrollToTop();
//                    }
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                //通过LayoutMager最后一个可见的item
                if (layoutManager instanceof LinearLayoutManager) {
                    mLinearLayoutManager = (LinearLayoutManager) layoutManager;
                    lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                }
                if (layoutManager instanceof GridLayoutManager) {
                    mGridLayoutManager = (GridLayoutManager) layoutManager;
                    lastVisibleItem = mGridLayoutManager.findLastVisibleItemPosition();
                }
                if (layoutManager instanceof StaggeredGridLayoutManager) {
                    mStaggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                    int cout = mStaggeredGridLayoutManager.getSpanCount();
                    int[] positions = mStaggeredGridLayoutManager.findLastVisibleItemPositions(null);
                    switch (cout) {
                        case 1: {//1列
                            lastVisibleItem = positions[0];
                            break;
                        }
                        case 2: {//2列
                            lastVisibleItem = Math.max(positions[0], positions[1]);//根据StaggeredGridLayoutManager设置的列数来定
                            break;
                        }
                        default: {
                            lastVisibleItem = bubbleSort(positions)[positions.length - 1];
                        }
                    }
                }

            }
        });

//        addOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                callback.onStateChanged(ScrollRecyclerView.this, newState);
//                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//                    if (!recyclerView.canScrollVertically(1)) {
//                        callback.onScrollToBottom();
//                    }
//                    if (!recyclerView.canScrollVertically(-1)) {
//                        callback.onScrollToTop();
//                    }
//                }
//            }
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                //通过LayoutMager最后一个可见的item
//                LayoutManager layoutManager  = recyclerView.getLayoutManager();
//                if(layoutManager instanceof LinearLayoutManager){
//                    mLinearLayoutManager = (LinearLayoutManager)layoutManager;
//                    lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
//                }
//                if(layoutManager instanceof GridLayoutManager){
//                    mGridLayoutManager = (GridLayoutManager)layoutManager;
//                    lastVisibleItem = mGridLayoutManager.findLastVisibleItemPosition();
//                }
//                if(layoutManager instanceof StaggeredGridLayoutManager){
//                    mStaggeredGridLayoutManager = (StaggeredGridLayoutManager)layoutManager;
//                }
//
//                if (dy > 0) {
//                    callback.onScrollDown(ScrollRecyclerView.this, dy);
//                } else {
//                    callback.onScrollUp(ScrollRecyclerView.this, dy);
//                }
//            }
//        });

    }

    private int[] bubbleSort(int[] sort) {
        int length = sort.length;
        boolean flag;//如果没有则说明排序已经完成
        LogUtils.e("lastVisibleItem", "排序前" + sort.length);

        for (int i = 0; i < length - 1; i++) {
            flag = false;
            for (int j = 0; j < length - 1 - i; j++) {
                if (sort[j] > sort[j + 1]) {
                    flag = true;//发生了交换
                    int temp = sort[j];
                    sort[j] = sort[j + 1];
                    sort[j + 1] = temp;
                }
            }
            if (flag == false)
                break;
        }

        for (int i : sort) {
            System.out.print("排序后" + i + ",");
        }
        return sort;
    }

//    public interface OnScrollCallback {
//        //当移动到最顶部时
//        void onScrollToTop();
//        //当移动到最底部时部时
//        void onScrollToBottom();
//
//        void onStateChanged(ScrollRecyclerView recycler, int state);
//
//        void onScrollUp(ScrollRecyclerView recycler, int dy);
//
//        void onScrollDown(ScrollRecyclerView recycler, int dy);
//
//
//    }

    public interface OnScrollCallback {
        //当移动到最顶部时
        void onScrollToTop();

        //当移动到最底部时部时
        void onScrollToBottom();

//        void onStateChanged(ScrollRecyclerView recycler, int state);
//
//        void onScrollUp(ScrollRecyclerView recycler, int dy);
//
//        void onScrollDown(ScrollRecyclerView recycler, int dy);


    }
}
