package com.example.beckart.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.example.beckart.model.Shipping;
import com.example.beckart.repository.ShippingRepository;

import okhttp3.ResponseBody;

public class ShippingViewModel  extends AndroidViewModel {

    private ShippingRepository shippingRepository;

    public ShippingViewModel(@NonNull Application application) {
        super(application);
        shippingRepository = new ShippingRepository(application);
    }

    public LiveData<ResponseBody> addShippingAddress(Shipping shipping) {
        return shippingRepository.addShippingAddress(shipping);
    }
}
