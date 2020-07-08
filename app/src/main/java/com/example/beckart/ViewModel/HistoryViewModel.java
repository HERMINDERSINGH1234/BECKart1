package com.example.beckart.ViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.arch.lifecycle.LiveData;
import com.example.beckart.model.Product;
import com.example.beckart.net.HistoryDataSource;
import com.example.beckart.net.HistoryDataSourceFactory;

import static com.example.beckart.net.HistoryDataSourceFactory.historyDataSource;

public class HistoryViewModel extends ViewModel {
    public LiveData historyPagedList;
    private MutableLiveData<PageKeyedDataSource<Integer, Product>> liveDataSource;

    public void loadHistory(int userId) {
        HistoryDataSourceFactory historyDataSourceFactory = new HistoryDataSourceFactory(userId);

        liveDataSource=historyDataSourceFactory.getProductsInHistory();

        // Get PagedList configuration
        PagedList.Config pagedListConfig = (new PagedList.Config.Builder().setEnablePlaceholders(false).setPageSize(HistoryDataSource.PAGE_SIZE).build();

        // Build the paged list
        historyPagedList = (new LivePagedListBuilder(historyDataSourceFactory, pagedListConfig)).build();
    }

    public void invalidate(){
        if(historyDataSource != null) historyDataSource.invalidate();
    }


}
