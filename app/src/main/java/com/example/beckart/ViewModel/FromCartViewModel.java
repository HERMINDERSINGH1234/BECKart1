package com.example.beckart.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;


import com.example.beckart.repository.FromCartRepository;
import com.example.beckart.utils.RequestCallback;

import okhttp3.ResponseBody;

public class FromCartViewModel extends AndroidViewModel {

    private FromCartRepository fromCartRepository;

    public FromCartViewModel(@NonNull Application application) {
        super(application);
        fromCartRepository = new FromCartRepository(application);
    }

    public LiveData<ResponseBody> removeFromCart(int userId, int productId, RequestCallback callback) {
        return fromCartRepository.removeFromCart(userId, productId, callback);
    }
}
