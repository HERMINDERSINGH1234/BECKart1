package com.example.beckart.repository;

import android.app.Application;
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.example.beckart.model.ProductApiResponse;
import com.example.beckart.net.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchRepository {

    private static final String TAG = SearchRepository.class.getSimpleName();
    private Application application;

    public SearchRepository(Application application) {
        this.application = application;
    }

    public LiveData<ProductApiResponse> getResponseDataBySearch(String keyword, int userId) {
        final MutableLiveData<ProductApiResponse> mutableLiveData = new MutableLiveData<>();

        RetrofitClient.getInstance()
                .getApi().searchForProduct(keyword, userId)
                .enqueue(new Callback<ProductApiResponse>() {
                    @Override
                    public void onResponse(Call<ProductApiResponse> call, Response<ProductApiResponse> response) {
                        Log.d(TAG, "onResponse: Succeeded");

                        ProductApiResponse productApiResponse = response.body();

                        if (response.body() != null) {
                            mutableLiveData.setValue(productApiResponse);
                            Log.d(TAG, String.valueOf(response.body().getProducts()));
                        }
                    }

                    @Override
                    public void onFailure(Call<ProductApiResponse> call, Throwable t) {
                        Log.v("onFailure", " Failed to get products");
                    }
                });
        return mutableLiveData;
    }
}
