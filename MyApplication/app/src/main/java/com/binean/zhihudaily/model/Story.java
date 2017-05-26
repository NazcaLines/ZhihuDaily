package com.binean.zhihudaily.model;

import java.util.List;

/**
 * Created by 彬旭 on 2017/5/26.
 */

public class Story extends BaseStory {
    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    private List<String>images;
}
