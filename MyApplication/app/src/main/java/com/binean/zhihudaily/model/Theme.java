package com.binean.zhihudaily.model;

import java.util.List;

/**
 * Created by 彬旭 on 2017/5/26.
 */

public class Theme {

    private String description;
    private String background;
    private String name;
    private String image;
//    private List<> editors;
    private List<Story>stories;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }
}
