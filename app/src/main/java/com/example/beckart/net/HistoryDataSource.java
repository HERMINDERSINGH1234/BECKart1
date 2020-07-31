package com.example.beckart.net;



import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.beckart.model.HistoryApiResponse;
import com.example.beckart.model.Product;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryDataSource extends PageKeyedDataSource<Integer, Product> {

    private static final int FIRST_PAGE = 1;
    public static final int PAGE_SIZE = 20;
    private int userId;

    public HistoryDataSource(int userId) {
        this.userId = userId;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Product> callback) {
        com.example.beckart.net.RetrofitClient.getInstance()
                .getApi().getProductsInHistory(userId,FIRST_PAGE)
                .enqueue(new Callback<HistoryApiResponse>() {
                    @Override
                    public void onResponse(Call<HistoryApiResponse> call, Response<HistoryApiResponse> response) {
                        Log.v("onResponse", "Succeeded " + response.body().getHistoryList().size());

                        if (response.body().getHistoryList()== null) {
                            return;
                        }

                        if (response.body() != null) {
                            callback.onResult(response.body().getHistoryList(), null, FIRST_PAGE + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<HistoryApiResponse> call, Throwable t) {
                        Log.v("onFailure", "Failed to get Products");
                    }
                });
    }


    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {
        com.example.beckart.net.RetrofitClient.getInstance()
                .getApi().getProductsInHistory(userId,params.key)
                .enqueue(new Callback<HistoryApiResponse>() {
                    @Override
                    public void onResponse(Call<HistoryApiResponse> call, Response<HistoryApiResponse> response) {
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (response.body() != null) {
                            // Passing the loaded database and the previous page key
                            callback.onResult(response.body().getHistoryList(), adjacentKey);
                        }
                    }

                    @Override
                    public void onFailure(Call<HistoryApiResponse> call, Throwable t) {
                        Log.v("onFailure", "Failed to previous Products");
                    }
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Product> callback) {
        com.example.beckart.net.RetrofitClient.getInstance()
                .getApi().getProductsInHistory(userId,params.key)
                .enqueue(new Callback<HistoryApiResponse>() {
                    @Override
                    public void onResponse(Call<HistoryApiResponse> call, Response<HistoryApiResponse> response) {
                        if (response.body() != null) {
                            // If the response has next page, increment the next page number
                            Integer key = response.body().getHistoryList().size() == PAGE_SIZE ? params.key + 1 : null;

                            // Passing the loaded database and next page value
                            callback.onResult(response.body().getHistoryList(), key);
                        }
                    }

                    @Override
                    public void onFailure(Call<HistoryApiResponse> call, Throwable t) {
                        Log.v("onFailure", "Failed to get next Products");
                    }
                });
    }
}
