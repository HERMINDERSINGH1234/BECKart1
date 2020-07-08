package com.example.beckart.view;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import com.example.beckart.R;
import com.example.beckart.ViewModel.FavoriteViewModel;
import com.example.beckart.adapter.WishListAdapter;
import com.example.beckart.databinding.ActivityWishlistBinding;
import com.example.beckart.model.Product;
import com.example.beckart.storage.LoginUtils;

import java.util.List;

import static com.example.beckart.storage.LanguageUtils.loadLocale;
import static com.example.beckart.utils.Constant.PRODUCT;
import static com.example.beckart.utils.InternetUtils.isNetworkConnected;

public class WishListActivity extends AppCompatActivity {

    private ActivityWishlistBinding binding;
    private WishListAdapter wishListAdapter;
    private List<Product> favoriteList;
    private FavoriteViewModel favoriteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wishlist);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getResources().getString(R.string.my_wishList));

        setUpRecyclerView();

        getFavorites();
    }

    private void setUpRecyclerView() {
        binding.favoriteList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        binding.favoriteList.setHasFixedSize(true);
        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);
    }

    private void getFavorites() {
        if (isNetworkConnected(this)) {
            favoriteViewModel.getFavorites(LoginUtils.getInstance(this).getUserInfo().getId()).observe((LifecycleOwner) this, favoriteApiResponse -> {
                if (favoriteApiResponse != null) {
                    favoriteList = favoriteApiResponse.getFavorites();
                    if (favoriteList.size() == 0) {
                        binding.noBookmarks.setVisibility(View.VISIBLE);
                        binding.emptyWishlist.setVisibility(View.VISIBLE);
                    }else {
                        binding.favoriteList.setVisibility(View.VISIBLE);
                    }
                    wishListAdapter = new WishListAdapter(getApplicationContext(), favoriteList, product -> {
                        Intent intent = new Intent(WishListActivity.this, DetailsActivity.class);
                        // Pass an object of product class
                        intent.putExtra(PRODUCT, (product));
                        startActivity(intent);
                    },this);
                }
                binding.loadingIndicator.setVisibility(View.GONE);
                binding.favoriteList.setAdapter(wishListAdapter);
                wishListAdapter.notifyDataSetChanged();
            });
        }else {
            binding.emptyWishlist.setVisibility(View.VISIBLE);
            binding.loadingIndicator.setVisibility(View.GONE);
            binding.emptyWishlist.setText(getString(R.string.no_internet_connection));
        }
    }

}
