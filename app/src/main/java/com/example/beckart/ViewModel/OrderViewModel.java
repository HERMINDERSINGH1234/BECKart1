package com.example.beckart.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;


import com.example.beckart.model.OrderApiResponse;
import com.example.beckart.repository.OrderRepository;

public class OrderViewModel extends AndroidViewModel {

    private OrderRepository orderRepository;

    public OrderViewModel(@NonNull Application application) {
        super(application);
        orderRepository = new OrderRepository(application);
    }

    public LiveData<OrderApiResponse> getOrders(int userId) {
        return orderRepository.getOrders(userId);
    }
}

