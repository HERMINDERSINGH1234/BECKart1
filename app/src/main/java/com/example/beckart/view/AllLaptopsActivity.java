package com.example.beckart.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
//import androidx.paging.PagedList;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.content.res.Configuration;
//import androidx.databinding.DataBindingUtil;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.annotation.Nullable;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.GridLayoutManager;

import com.example.beckart.R;
import com.example.beckart.ViewModel.ProductViewModel;
import com.example.beckart.adapter.ProductAdapter;
import com.example.beckart.databinding.ActivityAllLaptopsBinding;
import com.example.beckart.model.Product;
import com.example.beckart.storage.LoginUtils;

import static com.example.beckart.storage.LanguageUtils.loadLocale;
import static com.example.beckart.utils.Constant.PRODUCT;

public class AllLaptopsActivity extends AppCompatActivity implements ProductAdapter.ProductAdapterOnClickHandler {

    private ActivityAllLaptopsBinding binding;
    private ProductAdapter laptopAdapter;
    private ProductViewModel productViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_all_laptops);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.all_laptops));

        int userID = LoginUtils.getInstance(this).getUserInfo().getId();

        productViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        productViewModel.loadLaptops("laptop",userID);

        setupRecyclerViews();

        getAllLaptops();
    }

    private void setupRecyclerViews() {
        // Laptops
        binding.allLaptopsRecyclerView.setHasFixedSize(true);
        binding.allLaptopsRecyclerView.setLayoutManager(new GridLayoutManager(this, (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ? 2 : 4));
        laptopAdapter = new ProductAdapter(this, this);
    }

    public void getAllLaptops() {

        // Observe the productPagedList from ViewModel
        productViewModel.laptopPagedList.observe(this, new Observer<PagedList<Product>>() {
            @Override
            public void onChanged(@Nullable PagedList<Product> products) {
                laptopAdapter.submitList(products);
            }
        });

        binding.allLaptopsRecyclerView.setAdapter(laptopAdapter);
        laptopAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(Product product) {
        Intent intent = new Intent(AllLaptopsActivity.this, com.example.beckart.view.DetailsActivity.class);
        // Pass an object of product class
        intent.putExtra(PRODUCT, (product));
        startActivity(intent);
    }
}
