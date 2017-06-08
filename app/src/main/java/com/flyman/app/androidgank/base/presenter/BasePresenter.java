package com.flyman.app.androidgank.base.presenter;

import com.flyman.app.androidgank.base.task.BaseTask;

public abstract class BasePresenter<T extends BaseTask> {
    public abstract void start(T task);
    public abstract void onViewDestroy();
}
