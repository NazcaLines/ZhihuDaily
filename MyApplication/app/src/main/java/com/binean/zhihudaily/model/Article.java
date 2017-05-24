package com.binean.zhihudaily.model;

/**
 * Created by 彬旭 on 2017/5/24.
 */

public class Article {
    private String mTitle;
    private int mId;
    private String mUrl;

    public Article(String Title, int Id, String Url) {
        this.mTitle = Title;
        this.mId = Id;
        this.mUrl = Url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String Title) {
        this.mTitle = Title;
    }

    public int getId() {
        return mId;
    }

    public void setId(int Id) {
        this.mId = Id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String Url) {
        this.mUrl = Url;
    }
}
