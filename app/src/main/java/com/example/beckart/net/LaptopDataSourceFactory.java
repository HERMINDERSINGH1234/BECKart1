package com.example.beckart.net;

//import androidx.lifecycle.MutableLiveData;
//import androidx.paging.DataSource;
//import androidx.paging.PageKeyedDataSource;

import android.arch.lifecycle.MutableLiveData;

import com.example.beckart.model.Product;

import javax.sql.DataSource;

public class LaptopDataSourceFactory extends DataSource.Factory{

    // Creating the mutable live database
    private MutableLiveData<PageKeyedDataSource<Integer, Product>> laptopLiveDataSource = new MutableLiveData<>();

    public static com.example.beckart.net.ProductDataSource laptopDataSource;

    private String category;
    private int userId;

    public LaptopDataSourceFactory(String category, int userId){
        this.category = category;
        this.userId = userId;
    }

    @Override
    public DataSource<Integer, Product> create() {
        // Getting our Data source object
        laptopDataSource = new com.example.beckart.net.ProductDataSource(category,userId);

        // Posting the Data source to get the values
        laptopLiveDataSource.postValue(laptopDataSource);

        // Returning the Data source
        return laptopDataSource;
    }


    // Getter for Product live DataSource
    public MutableLiveData<PageKeyedDataSource<Integer, Product>> getLaptopLiveDataSource() {
        return laptopLiveDataSource;
    }
}