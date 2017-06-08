package com.flyman.app.androidgank.io;

import com.flyman.app.androidgank.model.api.BeautyApi;
import com.flyman.app.androidgank.model.api.HomepageApi;
import com.flyman.app.androidgank.model.api.NavigateArticleApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetWork {
    public static final String BASE_URL = "http://gank.io/api/";
    public static final String GANK_HOMEPAGE = "http://gank.io/";
    public static final String GITHUB_HOMEPAGE = "https://github.com/JamJunLe/AndroidGank";
    public static HomepageApi getHomepageApi() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpClientManger.getInstance().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return mRetrofit.create(HomepageApi.class);
    }

    public static NavigateArticleApi getNavigateArticleApi() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpClientManger.getInstance().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return mRetrofit.create(NavigateArticleApi.class);
    }

    public static BeautyApi getBeautyApi() {
        Retrofit mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpClientManger.getInstance().getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return mRetrofit.create(BeautyApi.class);
    }



}
