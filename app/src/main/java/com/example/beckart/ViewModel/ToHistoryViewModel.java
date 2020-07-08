package com.example.beckart.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.beckart.model.History;
import com.example.beckart.repository.ToHistoryRepository;

import okhttp3.ResponseBody;

public class ToHistoryViewModel extends AndroidViewModel {

    private ToHistoryRepository toHistoryRepository;

    public ToHistoryViewModel(@NonNull Application application) {
        super(application);
        toHistoryRepository = new ToHistoryRepository(application);
    }

    public LiveData<ResponseBody> addToHistory(History history) {
        return toHistoryRepository.addToHistory(history);
    }
}
