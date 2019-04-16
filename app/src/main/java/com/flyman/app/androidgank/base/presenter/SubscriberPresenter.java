package com.flyman.app.androidgank.base.presenter;

import com.flyman.app.androidgank.base.view.IView;
import com.flyman.app.util.log.LogUtils;

import java.util.HashMap;
import java.util.Map;

import rx.Subscriber;

public abstract class SubscriberPresenter<T extends Subscriber, V extends IView> extends BasePresenter {
    protected Map<Object, T> taskMap = new HashMap<>();

    /**
     * 保存任务
     *
     * @param taskId Object subscriber T
     * @return Subscriber
     */
    protected Subscriber saveTask(Object taskId, T subscriber) {
        Subscriber mSubscriber = getTask(taskId);//当前任务存在
        if (mSubscriber == null) {
            mSubscriber = taskMap.put(taskId, subscriber);
        }
        LogUtils.e("task", "saveTask taskId =" + taskId);
        return mSubscriber;

    }

    /**
     * 获取任务
     *
     * @param taskId Object
     * @return Subscriber
     */
    protected Subscriber getTask(Object taskId) {
        Subscriber mSubscriber = null;
        if (taskMap != null && taskMap.containsKey(taskId) == true) ;
        {
            mSubscriber = taskMap.get(taskId);
        }
        return mSubscriber;
    }

    /**
     * 判断是否为唯一的任务
     *
     * @param taskId Object
     * @return Subscriber
     */
    protected Subscriber isSoleTask(Object taskId) {
        Subscriber mSubscriber = null;
        if (taskMap != null && taskMap.containsKey(taskId) == true) ;
        {
            mSubscriber = taskMap.get(taskId);
        }
        return mSubscriber;
    }

    /**
     * 取消任务
     *
     * @param taskId Object
     * @return Subscriber
     */
    protected void cancelTask(Object taskId) {
        Subscriber mSubscriber = getTask(taskId);
        if (mSubscriber != null && !mSubscriber.isUnsubscribed()) {
            mSubscriber.unsubscribe();
            taskMap.remove(taskId);
        }
        LogUtils.e("task", "cancelTask taskId =" + taskId);
    }

    /**
     * 取消所有任务
     */
    protected void cancelAllTask() {
        if (taskMap != null) {
            for (Map.Entry<Object, T> entry : taskMap.entrySet()) {
                cancelTask(entry.getKey());
            }
        }
        LogUtils.e("task", "cancelAllTask");
    }

    @Override
    public void onViewDestroy() {
        cancelAllTask();
    }
}
