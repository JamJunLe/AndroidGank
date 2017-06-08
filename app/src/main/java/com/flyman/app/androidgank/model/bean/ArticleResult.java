package com.flyman.app.androidgank.model.bean;

import java.io.Serializable;
import java.util.List;

public class ArticleResult implements Serializable {

    /**
     * error : false
     * results : [{"_id":"590d2d00421aa90c83a513e8","createdAt":"2017-05-06T09:55:12.318Z","desc":"自己实现一个超轻量级 JVM ","publishedAt":"2017-05-08T11:22:01.540Z","source":"chrome","type":"Android","url":"https://www.codeproject.com/Articles/24029/Home-Made-Java-Virtual-Machine","used":true,"who":"daimajia"},{"_id":"590ef4c5421aa90c83a513f0","createdAt":"2017-05-07T18:19:49.997Z","desc":"Android SO 文件的兼容和适配","publishedAt":"2017-05-08T11:22:01.540Z","source":"web","type":"Android","url":"http://blog.coderclock.com/2017/05/07/android/Android-so-files-compatibility-and-adaptation/","used":true,"who":"D_clock"},{"_id":"590f4e77421aa90c7d49ad59","createdAt":"2017-05-08T00:42:31.109Z","desc":"优雅的Snackbar","images":["http://img.gank.io/c230ed80-2a3b-443a-a831-df9453519e94"],"publishedAt":"2017-05-08T11:22:01.540Z","source":"web","type":"Android","url":"https://github.com/TonnyL/Light","used":true,"who":"黎赵太郎"},{"_id":"590faecb421aa90c7a8b2ad5","createdAt":"2017-05-08T07:33:31.794Z","desc":"一款支持透明度的取色器，想取哪里点哪里   O(∩_∩)O","images":["http://img.gank.io/800aa39c-8c0f-4fff-96dd-892259362e0f"],"publishedAt":"2017-05-08T11:22:01.540Z","source":"web","type":"Android","url":"https://github.com/DingMouRen/ColorPicker","used":true,"who":null},{"_id":"590fdc0a421aa90c7a8b2ad7","createdAt":"2017-05-08T10:46:34.42Z","desc":"Android Java8 外置标准库~","publishedAt":"2017-05-08T11:22:01.540Z","source":"chrome","type":"Android","url":"https://github.com/retropiler/retropiler","used":true,"who":"代码家"},{"_id":"590fdc87421aa90c7a8b2ad8","createdAt":"2017-05-08T10:48:39.747Z","desc":"滑动式选择器，用在选头像，选背景图非常适合的场景。","images":["http://img.gank.io/ba5e1840-1ecf-4dc8-a0e9-d8b558d7a192"],"publishedAt":"2017-05-08T11:22:01.540Z","source":"chrome","type":"Android","url":"https://github.com/GoodieBag/CarouselPicker","used":true,"who":"代码家"},{"_id":"590af3b3421aa90c7fefdd3c","createdAt":"2017-05-04T17:26:11.264Z","desc":"Android自定义动画酷炫的提交按钮","publishedAt":"2017-05-05T11:56:35.629Z","source":"web","type":"Android","url":"http://url.cn/48GSjkM","used":true,"who":"陈宇明"},{"_id":"590b23cb421aa90c7fefdd3e","createdAt":"2017-05-04T20:51:23.101Z","desc":"Android Dagger2: Critical things to know before you implement","publishedAt":"2017-05-05T11:56:35.629Z","source":"web","type":"Android","url":"https://blog.mindorks.com/android-dagger2-critical-things-to-know-before-you-implement-275663aecc3e","used":true,"who":"AMIT SHEKHAR"},{"_id":"590be7d5421aa90c7d49ad3f","createdAt":"2017-05-05T10:47:49.687Z","desc":"Android 室内场景构建组件，帮你快速的完成室内场景 View 的展示","images":["http://img.gank.io/40be7210-4720-4f08-9d0b-8793bbdde0bc"],"publishedAt":"2017-05-05T11:56:35.629Z","source":"chrome","type":"Android","url":"https://github.com/karonl/InDoorSurfaceView","used":true,"who":"代码家"},{"_id":"5909a86d421aa90c7d49ad30","createdAt":"2017-05-03T17:52:45.723Z","desc":"你一定会用到的RxJava常用操作符","publishedAt":"2017-05-04T11:43:26.66Z","source":"web","type":"Android","url":"http://url.cn/48DFizd","used":true,"who":"陈宇明"}]
     */

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean implements Serializable{
        /**
         * _id : 590d2d00421aa90c83a513e8
         * createdAt : 2017-05-06T09:55:12.318Z
         * desc : 自己实现一个超轻量级 JVM
         * publishedAt : 2017-05-08T11:22:01.540Z
         * source : chrome
         * type : Android
         * url : https://www.codeproject.com/Articles/24029/Home-Made-Java-Virtual-Machine
         * used : true
         * who : daimajia
         * images : ["http://img.gank.io/c230ed80-2a3b-443a-a831-df9453519e94"]
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;
        private int height;
        private int width;
        private List<String> images;

        public ResultsBean(String url) {
            this.url = url;
        }

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        @Override
        public String toString() {
            return "ResultsBean{" +
                    "_id='" + _id + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", desc='" + desc + '\'' +
                    ", publishedAt='" + publishedAt + '\'' +
                    ", source='" + source + '\'' +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    ", used=" + used +
                    ", who='" + who + '\'' +
                    ", height=" + height +
                    ", width=" + width +
                    ", images=" + images +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ArticleResult{" +
                "error=" + error +
                ", results=" + results +
                '}';
    }
}
