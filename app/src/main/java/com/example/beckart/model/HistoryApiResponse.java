package com.example.beckart.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HistoryApiResponse {

    @SerializedName("history")
    private List<com.example.beckart.model.Product> historyList;

    public List<com.example.beckart.model.Product> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<com.example.beckart.model.Product> historyList) {
        this.historyList = historyList;
    }
}
