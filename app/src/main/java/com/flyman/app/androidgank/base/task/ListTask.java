package com.flyman.app.androidgank.base.task;

public class ListTask extends BaseTask {
    protected boolean isFirstTimeGetData;

    public ListTask(int taskId) {
        super(taskId);
    }

    public ListTask(String message) {
        super(message);
    }

    public ListTask(int taskId, boolean isFirstTimeGetData) {
        super(taskId);
        this.isFirstTimeGetData = isFirstTimeGetData;
    }

    public boolean isFirstTimeGetData() {
        return isFirstTimeGetData;
    }

    public void setFirstTimeGetData(boolean firstTimeGetData) {
        isFirstTimeGetData = firstTimeGetData;
    }

    public interface Id {
        int PULL_REFRESH = 1;
        int PUSH_LOAD_MORE_REFRESH = 2;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public interface Message {
        String MSG_PUSH_LOAD_MORE_REFRESH_FINISH = "没有更多数据了";
        String MSG_PULL_REFRESH_FAIL = "刷新失败";
        String MSG_PUSH_LOAD_MORE_REFRESH_FAIL = "上拉刷新失败";
        String MSG_NET_ERROR = "网络错误";
        String MSG_NET_PASSIVE = "网络罢工了,请重试";
    }
}
