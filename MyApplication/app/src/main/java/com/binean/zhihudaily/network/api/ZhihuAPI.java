package com.binean.zhihudaily.network.api;

import com.binean.zhihudaily.model.Lastest;

import retrofit2.Call;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by 彬旭 on 2017/5/26.
 */

public interface ZhihuAPI {
    @GET("news/latest") Observable<Lastest> getLastest();
}
