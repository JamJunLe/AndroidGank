package com.flyman.app.androidgank.model.api;

import com.flyman.app.androidgank.model.bean.ArticleResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface NavigateArticleApi {
    @GET("data/Android/{number}/{page}")
    Observable<ArticleResult> getAndroid(@Path("number") int number, @Path("page") int page);
    @GET("data/iOS/{number}/{page}")
    Observable<ArticleResult> getIOS(@Path("number") int number, @Path("page") int page);
    @GET("data/前端/{number}/{page}")
    Observable<ArticleResult> getWeb(@Path("number") int number, @Path("page") int page);
    @GET("data/拓展资源/{number}/{page}")
    Observable<ArticleResult> getExpand(@Path("number") int number, @Path("page") int page);
}
