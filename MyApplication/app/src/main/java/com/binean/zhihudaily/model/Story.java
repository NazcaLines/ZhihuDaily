package com.binean.zhihudaily.model;

import java.util.List;

public class Story extends BaseStory {
    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public boolean hasImage() {
        return images != null;
    }

    private List<String>images;
}
