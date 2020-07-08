package com.example.beckart.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.beckart.repository.RemoveFavoriteRepository;
import com.example.beckart.utils.RequestCallback;

import okhttp3.ResponseBody;

public class RemoveFavoriteViewModel extends AndroidViewModel {

    private RemoveFavoriteRepository removeFavoriteRepository;

    public RemoveFavoriteViewModel(@NonNull Application application) {
        super(application);
        removeFavoriteRepository = new RemoveFavoriteRepository(application);
    }

    public LiveData<ResponseBody> removeFavorite(int userId, int productId, RequestCallback callback) {
        return removeFavoriteRepository.removeFavorite(userId, productId, callback);
    }
}
