package com.example.beckart.view;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import com.example.beckart.R;
import com.example.beckart.ViewModel.OrderViewModel;
import com.example.beckart.adapter.OrderAdapter;
import com.example.beckart.databinding.ActivityOrdersBinding;
import com.example.beckart.model.Order;
import com.example.beckart.storage.LoginUtils;

import static com.example.beckart.utils.Constant.ORDER;

public class OrdersActivity extends AppCompatActivity implements OrderAdapter.OrderAdapterOnClickHandler {

    private ActivityOrdersBinding binding;
    private OrderViewModel orderViewModel;
    private OrderAdapter orderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orders);

        orderViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);

        setUpRecycleView();

        getOrders();
    }

    private void setUpRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.orderList.setLayoutManager(layoutManager);
        binding.orderList.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        binding.orderList.addItemDecoration(dividerItemDecoration);
    }

    private void getOrders() {
        orderViewModel.getOrders(LoginUtils.getInstance(this).getUserInfo().getId()).observe((LifecycleOwner) this, orderApiResponse -> {
            orderAdapter = new OrderAdapter(getApplicationContext(), orderApiResponse.getOrderList(),this);
            binding.orderList.setAdapter(orderAdapter);
            orderAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onClick(Order order) {
        Intent intent = new Intent(OrdersActivity.this, com.example.beckart.view.StatusActivity.class);
        // Pass an object of order class
        intent.putExtra(ORDER, (order));
        startActivity(intent);
    }
}
