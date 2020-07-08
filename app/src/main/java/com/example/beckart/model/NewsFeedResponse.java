package com.example.beckart.model;

import com.example.beckart.model.NewsFeed;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsFeedResponse {

    @SerializedName("posters")
    private List<NewsFeed> posters;

    public List<NewsFeed> getPosters() {
        return posters;
    }

    public void setPosters(List<NewsFeed> posters) {
        this.posters = posters;
    }
}
