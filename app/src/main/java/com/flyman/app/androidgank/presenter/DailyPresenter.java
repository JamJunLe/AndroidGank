package com.flyman.app.androidgank.presenter;

import com.flyman.app.androidgank.base.GankApplication;
import com.flyman.app.androidgank.base.presenter.SubscriberPresenter;
import com.flyman.app.androidgank.base.task.BaseTask;
import com.flyman.app.androidgank.base.task.ListTask;
import com.flyman.app.androidgank.contract.DailyContract;
import com.flyman.app.androidgank.io.NetWork;
import com.flyman.app.androidgank.model.api.HomepageApi;
import com.flyman.app.androidgank.model.bean.DateHistoryResult;
import com.flyman.app.androidgank.model.bean.DayHistoryArticleCompound;
import com.flyman.app.androidgank.model.bean.DayHistoryResult;
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

public class DailyPresenter extends SubscriberPresenter implements DailyContract.Presenter {

    private DailyContract.View mView;
    private ListTask mTask;
    private int currentPage = 0;
    private int totalPage = 0;
    private int taskId;
    private boolean isFirstTimeGetData = false;
    private List<String> dayList;

    public DailyPresenter(DailyContract.View view) {
        this.mView = view;
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
                getPullRefreshData();
                break;
            }
            case ListTask.Id.PUSH_LOAD_MORE_REFRESH: {
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
        currentPage = 0;
        getData();
    }

    private void getData() {
        final Subscriber mSubscriber = new Subscriber<List<DayHistoryArticleCompound>>() {
            @Override
            public void onCompleted() {
                cancelTask(taskId);
            }
            @Override
            public void onError(Throwable e) {
                cancelTask(taskId);
                LogUtils.e(e.toString() + "\r\n taskId =" + taskId);
                if (taskId == ListTask.Id.PULL_REFRESH) {
                    mView.showErrorMsg(new ListTask(ListTask.Message.MSG_PULL_REFRESH_FAIL));
                    mView.setRefreshing(false);
                }
                if (taskId == ListTask.Id.PUSH_LOAD_MORE_REFRESH && currentPage > 0) {
                    currentPage = currentPage - 1;
                    mView.setLoadMOreRefreshing(false);
                    mView.showErrorMsg(new ListTask(ListTask.Message.MSG_PUSH_LOAD_MORE_REFRESH_FAIL));
                }

            }

            @Override
            public void onNext(List<DayHistoryArticleCompound> dayHistoryArticleComs) {
                cancelTask(taskId);
                if (currentPage == 0) {
                    mView.showPullRefreshData(dayHistoryArticleComs);
                    mView.setRefreshing(false);
                } else {
                    mView.showPushLoadMoreData(dayHistoryArticleComs);
                    mView.setLoadMOreRefreshing(false);
                }

            }

        };
        final HomepageApi homepageApi = NetWork.getHomepageApi();
        //已经获取了日期数据
        if (dayList != null && dayList.size() > 0) {
            LogUtils.e("Thread", "已经获取了日期数据 = " + dayList.get(currentPage).replace("-", "/"));
            LogUtils.e("Thread", " currentPage = " + currentPage);
            homepageApi.getDayHistoryResult(dayList.get(currentPage).replace("-", "/"))
                    .subscribeOn(Schedulers.io())
                    .map(new Func1<DayHistoryResult, List<DayHistoryArticleCompound>>() {
                        @Override
                        public List<DayHistoryArticleCompound> call(DayHistoryResult dayHistoryResult) {
                            try {
                                List<DayHistoryArticleCompound> te = getDayHistoryArticleComList(dayHistoryResult);
                            } catch (Exception e) {
                                LogUtils.e("Thread", " e = " + e.toString());
                            }
                            return getDayHistoryArticleComList(dayHistoryResult);
                        }
                    })
                    .doOnSubscribe(new Action0() {
                        @Override
                        public void call() {
                            if (isFirstTimeGetData == true) {
                                mView.setRefreshing(true);
                            }
                        }
                    })
                    .subscribeOn(AndroidSchedulers.mainThread()) // 指定主线程
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mSubscriber);
        } else {
            //获取日期数据
            LogUtils.e("Thread", "正在获取日期数据");
            homepageApi.getDateHistoryResult()
                    .flatMap(new Func1<DateHistoryResult, Observable<DayHistoryResult>>() {
                        @Override
                        public Observable<DayHistoryResult> call(DateHistoryResult date) {
                            totalPage = date.getResults().size();
                            dayList = date.getResults();
                            LogUtils.e("ArtcleResult", "totalPage =" + totalPage);
                            LogUtils.e("ArtcleResult", date.getResults().get(currentPage).replace("-", "/"));
                            return homepageApi.getDayHistoryResult(date.getResults().get(currentPage).replace("-", "/"));
                        }
                    })
                    .map(new Func1<DayHistoryResult, List<DayHistoryArticleCompound>>() {
                        @Override
                        public List<DayHistoryArticleCompound> call(DayHistoryResult dayHistoryResult) {
                            return getDayHistoryArticleComList(dayHistoryResult);
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
        }



    }

    private List<DayHistoryArticleCompound> getDayHistoryArticleComList(DayHistoryResult dayHistoryResult) {
        DayHistoryResult.ResultsBean bean = dayHistoryResult.getResults();
        List<DayHistoryArticleCompound> dayHistoryArticleComList = new ArrayList();
        //构造数据
        if (bean != null) {
            dayHistoryArticleComList.add(new DayHistoryArticleCompound(DayHistoryArticleCompound.Type.date, dayList.get(currentPage)));//日期
            if (bean.getBeauty() != null) {
                for (DayHistoryResult.DayHistoryArticle dayHistoryArticle : bean.getBeauty()) {
                    dayHistoryArticleComList.add(new DayHistoryArticleCompound(DayHistoryArticleCompound.Type.beauty, dayHistoryArticle));
                }
            }
            if (bean.getAndroid() != null) {
                for (DayHistoryResult.DayHistoryArticle dayHistoryArticle : bean.getAndroid()) {
                    dayHistoryArticleComList.add(new DayHistoryArticleCompound(DayHistoryArticleCompound.Type.android, dayHistoryArticle));
                }
            }
            if (bean.getiOS() != null) {
                for (DayHistoryResult.DayHistoryArticle dayHistoryArticle : bean.getiOS()) {
                    dayHistoryArticleComList.add(new DayHistoryArticleCompound(DayHistoryArticleCompound.Type.iOS, dayHistoryArticle));
                }
            }
            if (bean.getExpand() != null) {
                for (DayHistoryResult.DayHistoryArticle dayHistoryArticle : bean.getExpand()) {
                    dayHistoryArticleComList.add(new DayHistoryArticleCompound(DayHistoryArticleCompound.Type.expand, dayHistoryArticle));
                }
            }
            if (bean.getRecommend() != null) {
                for (DayHistoryResult.DayHistoryArticle dayHistoryArticle : bean.getRecommend()) {
                    dayHistoryArticleComList.add(new DayHistoryArticleCompound(DayHistoryArticleCompound.Type.recommend, dayHistoryArticle));
                }
            }
//            if (bean.getVideo() != null) {
//                for (DayHistoryResult.DayHistoryArticle dayHistoryArticle : bean.getVideo()) {
//                    dayHistoryArticleComList.add(new DayHistoryArticleCompound(DayHistoryArticleCompound.Type.video, dayHistoryArticle));
//                }
//            }
        }

        return dayHistoryArticleComList;
    }




}
