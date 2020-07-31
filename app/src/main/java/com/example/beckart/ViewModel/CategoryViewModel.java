package com.example.beckart.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.PagedList;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;

import com.example.beckart.model.Product;
import com.example.beckart.net.ProductDataSource;
import com.example.beckart.net.ProductDataSourceFactory;

public class CategoryViewModel extends ViewModel {
    public LiveData categoryPagedList;
    private MutableLiveData<PageKeyedDataSource<Integer, Product>> categoryLiveDataSource;

    public void loadProductsByCategory(String category, int userId) {
        ProductDataSourceFactory productDataSourceFactory = new ProductDataSourceFactory(category,userId);
        categoryLiveDataSource = productDataSourceFactory.getProductLiveDataSource();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(ProductDataSource.PAGE_SIZE)
                        .build();

        categoryPagedList = (new LivePagedListBuilder(productDataSourceFactory, pagedListConfig)).build();
    }

}
