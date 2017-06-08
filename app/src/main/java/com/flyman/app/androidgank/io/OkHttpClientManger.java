package com.flyman.app.androidgank.io;

import com.flyman.app.androidgank.base.GankApplication;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkHttpClientManger {
    private static OkHttpClient mOkHttpClient;
    private OkHttpClientManger() {
    }
    public static OkHttpClientManger getInstance()
    {
        return Holder.mOkHttpClientManger;
    }
    public OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
            client.readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
            client.connectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
            //设置缓存路径
            File httpCacheDirectory = new File(GankApplication.getAppContext().getCacheDir(), "okhttpCache");
            //设置缓存 10M
            Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            client.cache(cache);
            //设置拦截器
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            mOkHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        }
        return mOkHttpClient;
    }
    private static class Holder{
        private static OkHttpClientManger mOkHttpClientManger = new OkHttpClientManger();
    }
}
