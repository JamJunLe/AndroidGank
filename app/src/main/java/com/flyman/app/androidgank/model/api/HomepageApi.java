package com.flyman.app.androidgank.model.api;

import com.flyman.app.androidgank.model.bean.DayHistoryResult;
import com.flyman.app.androidgank.model.bean.DateHistoryResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface HomepageApi {
    @GET("day/{date}")
    Observable<DayHistoryResult> getDayHistoryResult(@Path("date") String date);
    @GET("day/history")
    Observable<DateHistoryResult> getDateHistoryResult();//获取日期数据
}
