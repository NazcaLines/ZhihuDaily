package com.binean.zhihudaily.model;

class BaseStory {
    protected int type;
    protected int id;
    protected String ga_prefix;
    protected String title;

    public int getType() {
        return type;
    }

    public void setmType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}