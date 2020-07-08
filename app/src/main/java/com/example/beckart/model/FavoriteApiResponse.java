package com.example.beckart.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavoriteApiResponse {

    @SerializedName("favorites")
    private List<com.example.beckart.model.Product> favorites;

    public List<com.example.beckart.model.Product> getFavorites() {
        return favorites;
    }

    public void setFavorites(List<com.example.beckart.model.Product> favorites) {
        this.favorites = favorites;
    }
}
