package com.example.beckart.view;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import com.example.beckart.R;
import com.example.beckart.ViewModel.CategoryViewModel;
import com.example.beckart.adapter.ProductAdapter;
import com.example.beckart.databinding.ActivityCategoryBinding;
import com.example.beckart.model.Product;
import com.example.beckart.receiver.NetworkChangeReceiver;
import com.example.beckart.storage.LoginUtils;
import com.example.beckart.utils.Constant;
import com.example.beckart.utils.OnNetworkListener;

import static com.example.beckart.storage.LanguageUtils.loadLocale;
import static com.example.beckart.utils.Constant.PRODUCT;
import static com.example.beckart.utils.InternetUtils.isNetworkConnected;

public class CategoryActivity extends AppCompatActivity implements ProductAdapter.ProductAdapterOnClickHandler, OnNetworkListener {

    private static final String TAG = "CategoryActivity";
    private ActivityCategoryBinding binding;
    private ProductAdapter productAdapter;
    private CategoryViewModel categoryViewModel;
    private String category;
    private Snackbar snack;
    private NetworkChangeReceiver mNetworkReceiver;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_category);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.app_name));

        // This line shows Up button
        actionBar.setDisplayHomeAsUpEnabled(true);

        snack = Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_INDEFINITE);

        // Get Category from ProductActivity Intent
        Intent intent = getIntent();
        category = intent.getStringExtra(Constant.CATEGORY);

        // Update Toolbar
        getSupportActionBar().setTitle(category);

        int userID = LoginUtils.getInstance(this).getUserInfo().getId();
        categoryViewModel = ViewModelProviders.of(this).get(CategoryViewModel.class);
        categoryViewModel.loadProductsByCategory(category.toLowerCase(), userID);

        setupRecyclerViews();

        getProductsByCategory();

        mNetworkReceiver = new NetworkChangeReceiver();
        mNetworkReceiver.setOnNetworkListener(this);
    }

    private void setupRecyclerViews() {
        binding.categoryList.setLayoutManager(new GridLayoutManager(this, (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) ? 2 : 4));
        binding.categoryList.setHasFixedSize(true);
        productAdapter = new ProductAdapter(this,this);
    }


    public void getProductsByCategory() {
        if (isNetworkConnected(this)) {
            categoryViewModel.categoryPagedList.observe((LifecycleOwner) this, new Observer<PagedList<Product>>() {
                @Override
                public void onChanged(@Nullable PagedList<Product> products) {
                    productAdapter.notifyDataSetChanged();
                    productAdapter.submitList(products);
                }
            });

            binding.categoryList.setAdapter(productAdapter);
        }
    }

    @Override
    public void onClick(Product product) {
        Intent intent = new Intent(CategoryActivity.this, com.example.beckart.view.DetailsActivity.class);
        // Pass an object of product class
        intent.putExtra(PRODUCT, (product));
        startActivity(intent);
    }

    @Override
    public void onNetworkConnected() {
        hideSnackBar();
        getProductsByCategory();
    }

    @Override
    public void onNetworkDisconnected() {
        showSnackBar();
    }

    public void showSnackBar() {
        snack.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.red));
        snack.show();
    }

    public void hideSnackBar() {
        snack.dismiss();
    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerNetworkBroadcastForNougat();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mNetworkReceiver);
    }
}
