package com.example.beckart.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CartApiResponse {

    @SerializedName("carts")
    private List<com.example.beckart.model.Product> carts;

    public List<com.example.beckart.model.Product> getProductsInCart() {
        return carts;
    }

    public void setProductsInCart(List<com.example.beckart.model.Product> carts) {
        this.carts = carts;
    }
}
