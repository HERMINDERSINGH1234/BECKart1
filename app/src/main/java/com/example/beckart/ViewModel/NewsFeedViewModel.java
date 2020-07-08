package com.example.beckart.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.beckart.model.NewsFeedResponse;
import com.example.beckart.repository.NewsFeedRepository;

public class NewsFeedViewModel extends AndroidViewModel {

    private NewsFeedRepository newsFeedRepository;

    public NewsFeedViewModel(@NonNull Application application) {
        super(application);
        newsFeedRepository = new NewsFeedRepository(application);
    }

    public LiveData<NewsFeedResponse> getPosters() {
        return newsFeedRepository.getPosters();
    }
}
