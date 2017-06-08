package com.flyman.app.androidgank.presenter;

import android.graphics.Bitmap;

import com.flyman.app.androidgank.base.GankApplication;
import com.flyman.app.androidgank.base.presenter.SubscriberPresenter;
import com.flyman.app.androidgank.base.task.BaseTask;
import com.flyman.app.androidgank.base.task.ListTask;
import com.flyman.app.androidgank.contract.BeautyContract;
import com.flyman.app.androidgank.contract.NavigateContract;
import com.flyman.app.androidgank.io.NetWork;
import com.flyman.app.androidgank.model.api.BeautyApi;
import com.flyman.app.androidgank.model.bean.ArticleResult;
import com.flyman.app.androidgank.utils.GlideHelper;
import com.flyman.app.androidgank.utils.NetUtil;
import com.flyman.app.util.log.LogUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BeautyPresenter extends SubscriberPresenter implements NavigateContract.Presenter {
    private BeautyContract.View mView;
    private ListTask mTask;
    private int currentPage = 1;
    private int taskId;
    private boolean isFirstTimeGetData = false;
    private int pageSize = 10;

    //    private boolean hasLoadMoreResult = false;//是否加载了全部内容
    public BeautyPresenter(BeautyContract.View view) {
        mView = view;
    }

    @Override
    public void start(BaseTask task) {
        //无网络状态
        if (NetUtil.isNetConnection(GankApplication.getAppContext()) == false) {
            mView.showErrorMsg(new ListTask(ListTask.Message.MSG_NET_PASSIVE));
            mView.setRefreshing(false);
            mView.setLoadMOreRefreshing(false);
            return;
        }
        mTask = (ListTask) task;
        taskId = mTask.getTaskId();
        isFirstTimeGetData = mTask.isFirstTimeGetData();

        if (getTask(taskId) != null)//当前任务正在进行
        {
            LogUtils.e("task", "当前任务正在进行 taskId = " + taskId);
            return;
        }
        switch (taskId) {
            case ListTask.Id.PULL_REFRESH: {
                if (isFirstTimeGetData == true) {
                    mView.setRefreshing(true);
                }
                getPullRefreshData();
                break;
            }
            case ListTask.Id.PUSH_LOAD_MORE_REFRESH: {
                mView.setLoadMOreRefreshing(true);
                getLoadMoreData();
                break;
            }
            default: {
            }
        }
    }

    @Override
    public void getLoadMoreData() {
        currentPage = currentPage + 1;
        getData();
    }

    @Override
    public void getPullRefreshData() {
        currentPage = 1;
        getData();
    }

    private void getData() {

        final Subscriber mSubscriber = new Subscriber<List<ArticleResult.ResultsBean>>() {
            @Override
            public void onCompleted() {
                cancelTask(taskId);
                LogUtils.e("onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.toString() + "\r\n taskId =" + taskId);
                cancelTask(taskId);
                mView.setLoadMOreRefreshing(false);
                mView.setRefreshing(false);
                if (taskId == ListTask.Id.PULL_REFRESH) {
                    mView.showErrorMsg(new ListTask(ListTask.Message.MSG_PULL_REFRESH_FAIL));
                }
                if (taskId == ListTask.Id.PUSH_LOAD_MORE_REFRESH && currentPage > 0) {
                    currentPage = currentPage - 1;
                    mView.showErrorMsg(new ListTask(ListTask.Message.MSG_PUSH_LOAD_MORE_REFRESH_FAIL));
                }
            }

            @Override
            public void onNext(List<ArticleResult.ResultsBean> resultsBeen) {
                cancelTask(taskId);
                LogUtils.e("onNext", resultsBeen.toString());
                mView.setLoadMOreRefreshing(false);
                mView.setRefreshing(false);
                if (resultsBeen.toString() == null || "".equals(resultsBeen.toString()))//已经加载了内容
                {
                    mView.showErrorMsg(new ListTask(ListTask.Message.MSG_PUSH_LOAD_MORE_REFRESH_FINISH));
                    return;
                }
                if (currentPage == 1) {
                    mView.showPullRefreshData(resultsBeen);

                } else {
                    mView.showPushLoadMoreData(resultsBeen);
                }
            }
        };
        BeautyApi api = NetWork.getBeautyApi();
        Observable<ArticleResult> mObservable = api.getBeauties(pageSize, currentPage);
        mObservable
                .map(new Func1<ArticleResult, List<ArticleResult.ResultsBean>>() {
                    @Override
                    public List<ArticleResult.ResultsBean> call(ArticleResult articleResult) {
                        LogUtils.e("", articleResult.toString());
                        List<ArticleResult.ResultsBean> mList= articleResult.getResults();
                        List<ArticleResult.ResultsBean> removeNullList= new ArrayList<ArticleResult.ResultsBean>();
                        LogUtils.e("wh", "mList.size() = "+mList.size());
                        for (ArticleResult.ResultsBean result : mList) {
                            Bitmap mBitmap = GlideHelper.getBitmap(GankApplication.getAppContext(), result.getUrl());
                            if (mBitmap != null) {
                                result.setHeight(mBitmap.getHeight());
                                result.setWidth(mBitmap.getWidth());
                                removeNullList.add(result);
//                                if (mBitmap.isRecycled() == false)
//                                {
//                                    mBitmap.recycle();
//                                }
//                                mBitmap.recycle();
//                                LogUtils.e("wh", "mBitmap.getWidth() ="+mBitmap.getWidth()+"  mBitmap.getHeight() = "+mBitmap.getHeight());
                            }
                            else {
                                LogUtils.e("123456", "绝望了");
                            }
                        }
                        return mList;
                    }

                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (isFirstTimeGetData == true) {
                            mView.setRefreshing(true);
                            //存储任务
                            saveTask(taskId, mSubscriber);
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
        //存储任务
        saveTask(taskId, mSubscriber);


    }
}
