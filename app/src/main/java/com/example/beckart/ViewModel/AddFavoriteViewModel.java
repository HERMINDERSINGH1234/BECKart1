package com.example.beckart.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;


import com.example.beckart.model.Favorite;
import com.example.beckart.repository.AddFavoriteRepository;
import com.example.beckart.utils.RequestCallback;

import okhttp3.ResponseBody;


public class AddFavoriteViewModel extends AndroidViewModel {

    private AddFavoriteRepository addFavoriteRepository;

    public AddFavoriteViewModel(@NonNull Application application) {
        super(application);
        addFavoriteRepository = new AddFavoriteRepository(application);
    }

    public LiveData<ResponseBody> addFavorite(Favorite favorite, RequestCallback callback) {
        return addFavoriteRepository.addFavorite(favorite,callback);
    }
}
