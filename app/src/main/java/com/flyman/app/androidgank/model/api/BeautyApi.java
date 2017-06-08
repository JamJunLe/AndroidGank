package com.flyman.app.androidgank.model.api;

import com.flyman.app.androidgank.model.bean.ArticleResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface BeautyApi {
    @GET("data/福利/{number}/{page}")
    Observable<ArticleResult> getBeauties(@Path("number") int number, @Path("page") int page);
}
