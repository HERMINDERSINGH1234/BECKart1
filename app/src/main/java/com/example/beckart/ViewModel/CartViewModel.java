package com.example.beckart.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.example.beckart.model.CartApiResponse;
import com.example.beckart.repository.CartRepository;

public class CartViewModel extends AndroidViewModel {

    private CartRepository cartRepository;

    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepository = new CartRepository(application);
    }

    public LiveData<CartApiResponse> getProductsInCart(int userId) {
        return cartRepository.getProductsInCart(userId);
    }
}
