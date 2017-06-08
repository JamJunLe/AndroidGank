package com.flyman.app.androidgank.presenter;

import com.flyman.app.androidgank.base.GankApplication;
import com.flyman.app.androidgank.base.presenter.SubscriberPresenter;
import com.flyman.app.androidgank.base.task.BaseTask;
import com.flyman.app.androidgank.base.task.ListTask;
import com.flyman.app.androidgank.contract.NavigateContract;
import com.flyman.app.androidgank.io.NetWork;
import com.flyman.app.androidgank.model.api.NavigateArticleApi;
import com.flyman.app.androidgank.model.bean.ArticleResult;
import com.flyman.app.androidgank.ui.fragment.NavigateArticleFragment;
import com.flyman.app.androidgank.utils.NetUtil;
import com.flyman.app.util.log.LogUtils;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class NavigatePresenter extends SubscriberPresenter implements NavigateContract.Presenter {
    private NavigateContract.View mView;
    private ListTask mTask;
    private int currentPage = 1;
//    private int totalPage = 0;
    private int args;
    private int taskId;
    private boolean isFirstTimeGetData = false;

    public NavigatePresenter(NavigateContract.View view, int args) {
        mView = view;
        this.args = args;
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
//        if (currentPage > totalPage - 1 && totalPage > 0) {
//            mView.showErrorMsg(new ListTask(ListTask.Message.MSG_PUSH_LOAD_MORE_REFRESH_FINISH));
//            return;
//        }
        currentPage = currentPage + 1;
        getData();
    }

    @Override
    public void getPullRefreshData() {
        currentPage = 1;
        getData();
    }

    private void getData() {
        if(getTask(taskId) !=null)//当前任务正在进行
        {
            LogUtils.e("task","当前任务正在进行 taskId = "+taskId);
            return;
        }
        Subscriber mSubscriber = new Subscriber<List<ArticleResult.ResultsBean>>() {
            @Override
            public void onCompleted() {
                cancelTask(taskId);
                LogUtils.e("onCompleted");
            }
            @Override
            public void onError(Throwable e) {
                LogUtils.e(e.toString() + "\r\n taskId =" + taskId);
                cancelTask(taskId);
                if (taskId == ListTask.Id.PULL_REFRESH) {
                    mView.showErrorMsg(new ListTask(ListTask.Message.MSG_PULL_REFRESH_FAIL));
                    mView.setRefreshing(false);
                }
                if (taskId == ListTask.Id.PUSH_LOAD_MORE_REFRESH && currentPage > 0) {
                    currentPage = currentPage - 1;
                    mView.showErrorMsg(new ListTask(ListTask.Message.MSG_PUSH_LOAD_MORE_REFRESH_FAIL));
                    mView.setLoadMOreRefreshing(false);
                }
            }
            @Override
            public void onNext(List<ArticleResult.ResultsBean> resultsBeen) {
                cancelTask(taskId);
                LogUtils.e("onNext", resultsBeen.toString());
                if (currentPage == 1) {
                    mView.showPullRefreshData(resultsBeen);
                    mView.setRefreshing(false);
                } else {
                    mView.showPushLoadMoreData(resultsBeen);
                    mView.setLoadMOreRefreshing(false);
                }
            }


        };
        NavigateArticleApi navigateArticleApi = NetWork.getNavigateArticleApi();
        Observable<ArticleResult> mObservable = null;
        switch (args) {
            case NavigateArticleFragment.ANDROID: {
                mObservable = navigateArticleApi.getAndroid(20, currentPage);
                break;
            }
            case NavigateArticleFragment.IOS: {
                mObservable = navigateArticleApi.getIOS(20, currentPage);
                break;
            }
            case NavigateArticleFragment.WEB: {
                mObservable = navigateArticleApi.getWeb(20, currentPage);
                break;
            }
            case NavigateArticleFragment.EXPAND: {
                mObservable = navigateArticleApi.getExpand(20, currentPage);
                break;
            }
            default: {

            }
        }
        mObservable
                .map(new Func1<ArticleResult, List<ArticleResult.ResultsBean>>() {
                    @Override
                    public List<ArticleResult.ResultsBean> call(ArticleResult articleResult) {
                        LogUtils.e("", articleResult.toString());
                        return articleResult.getResults();
                    }

                })
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
        //存储任务
        saveTask(taskId,mSubscriber);


    }
}
