package com.example.beckart.net;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;

import com.example.beckart.model.Product;

import javax.sql.DataSource;


public class HistoryDataSourceFactory extends DataSource.Factory{

    private int userId;

    public HistoryDataSourceFactory(int userId) {
        this.userId = userId;
    }

    // Creating the mutable live database
    private MutableLiveData<PageKeyedDataSource<Integer, Product>> historyLiveDataSource = new MutableLiveData<>();

    public static HistoryDataSource historyDataSource;

    @Override
    public DataSource<Integer, Product> create() {
        // Getting our Data source object
        historyDataSource = new HistoryDataSource(userId);

        // Posting the Data source to get the values
        historyLiveDataSource.postValue(historyDataSource);

        // Returning the Data source
        return historyDataSource;
    }

    // Getter for Product live DataSource
    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getProductsInHistory() {
        return historyLiveDataSource;
    }
}
