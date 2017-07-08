package com.binean.zhihudaily.network.api;

import com.binean.zhihudaily.model.Detail;
import com.binean.zhihudaily.model.Lastest;
import com.binean.zhihudaily.model.Theme;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 彬旭 on 2017/5/26.
 */

public interface ZhihuAPI {
    @GET("news/latest") Observable<Lastest> getLastest();

    @GET("theme/{number}") Observable<Theme> getTheme(@Path("number") String key);

    @GET("news/{id}") Observable<Detail> getStoryDetail(@Path("id") String id);

    @GET("news/before/{id}") Observable<Lastest> getBeforeStory(@Path("id") String id);
}
