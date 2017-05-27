package com.binean.zhihudaily.network;

import com.binean.zhihudaily.model.Detail;
import com.binean.zhihudaily.network.api.ZhihuAPI;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 彬旭 on 2017/5/26.
 */

public class NetUtils {
    public static final String BASE_URL = "http://news-at.zhihu.com/api/4/";
    public static final String GAME_NUMBER = "2";
    public static final String MOVIE_NUMBER = "3";
    public static final String DESIGN_NUMBER = "4";
    public static final String COMPANY_NUMBER = "5";
    public static final String FINANCE_NUMBER = "6";
    public static final String MUSIC_NUMBER = "7";
    public static final String SPORTS_NUMBER = "8";
    public static final String CARTOON_NUMBER = "9";
    public static final String SECURITY_NUMBER = "10";
    public static final String INTEREST_NUMBER = "11";
    public static final String PSY_NUMBER = "13";

    public static ZhihuAPI getApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(ZhihuAPI.class);
    }

    public static String transformHTML(Detail detail) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"img-wrap\">")
                .append("<h1 class=\"headline-title\">")
                .append(detail.getTitle()).append("</h1>")
                .append("<span class=\"img-source\">")
                .append(detail.getImage_source()).append("</span>")
                .append("<img src=\"").append(detail.getImage())
                .append("\" alt=\"\">")
                .append("<div class=\"img-mask\"></div>");
        String mNewsContent = "<link rel=\"stylesheet\" " +
                "type=\"text/css\" href=\"news_content_style.css\"/>" +
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_header_style.css\"/>" +
                detail.getBody().replace("<div class=\"img-place-holder\">", sb.toString());
        return mNewsContent;
    }
}
