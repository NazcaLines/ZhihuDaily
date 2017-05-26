package com.binean.zhihudaily.network;

import com.binean.zhihudaily.network.api.ZhihuAPI;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 彬旭 on 2017/5/26.
 */

public class Net_utils {
    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/";

    public static ZhihuAPI getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(ZhihuAPI.class);
    }

}
