package com.flyman.app.androidgank.base.view;

import com.flyman.app.androidgank.base.task.BaseTask;

import java.util.List;

public interface IListView extends IView {

    <T extends BaseTask> void showErrorMsg(T t);

    void cleanViewState();//清除view的状态

    <T extends List> void showPullRefreshData(T data);

    <T extends List> void showPushLoadMoreData(T data);

    void setRefreshEnable(boolean isEnable);

    void setRefreshing(boolean isRefreshing);

    void setLoadMOreRefreshing(boolean isRefreshing);


}
