package com.flyman.app.androidgank.base.task;

public class BaseTask {
    protected int taskId;
    protected String message;
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public BaseTask(int taskId) {
        this.taskId = taskId;
    }

    public BaseTask(String message) {
        this.message = message;
    }

    public BaseTask(int taskId, String message) {
        this.taskId = taskId;
        this.message = message;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
