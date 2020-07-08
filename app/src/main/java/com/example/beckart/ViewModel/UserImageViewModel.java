package com.example.beckart.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.example.beckart.model.Image;
import com.example.beckart.repository.UserImageRepository;

public class UserImageViewModel extends AndroidViewModel {

    private UserImageRepository userImageRepository;

    public UserImageViewModel(@NonNull Application application) {
        super(application);
        userImageRepository = new UserImageRepository(application);
    }

    public LiveData<Image> getUserImage(int userId) {
        return userImageRepository.getUserImage(userId);
    }
}
