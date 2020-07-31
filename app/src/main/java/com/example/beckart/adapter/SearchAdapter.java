package com.example.beckart.adapter;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.beckart.R;
import com.example.beckart.ViewModel.AddFavoriteViewModel;
import com.example.beckart.ViewModel.FromCartViewModel;
import com.example.beckart.ViewModel.RemoveFavoriteViewModel;
import com.example.beckart.ViewModel.ToCartViewModel;
import com.example.beckart.ViewModel.ToHistoryViewModel;
import com.example.beckart.databinding.SearchListItemBinding;
import com.example.beckart.model.Cart;
import com.example.beckart.model.Favorite;
import com.example.beckart.model.History;
import com.example.beckart.model.Product;
import com.example.beckart.storage.LoginUtils;
import com.example.beckart.utils.RequestCallback;
import java.text.DecimalFormat;
import java.util.List;
import static com.example.beckart.utils.Constant.LOCALHOST;
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>{

    private Context mContext;
    // Declare an arrayList for products
    private List<Product> productList;

    private Product currentProduct;
    private AddFavoriteViewModel addFavoriteViewModel;
    private RemoveFavoriteViewModel removeFavoriteViewModel;
    private ToCartViewModel toCartViewModel;
    private FromCartViewModel fromCartViewModel;
    private ToHistoryViewModel toHistoryViewModel;

    // Create a final private SearchAdapterOnClickHandler called mClickHandler
    private SearchAdapterOnClickHandler clickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface SearchAdapterOnClickHandler {
        void onClick(Product product);
    }

    public SearchAdapter(Context mContext,List<Product> productList,SearchAdapterOnClickHandler clickHandler, FragmentActivity activity) {
        this.mContext = mContext;
        this.productList = productList;
        this.clickHandler = clickHandler;
        addFavoriteViewModel = ViewModelProviders.of(activity).get(AddFavoriteViewModel.class);
        removeFavoriteViewModel = ViewModelProviders.of(activity).get(RemoveFavoriteViewModel.class);
        toCartViewModel = ViewModelProviders.of(activity).get(ToCartViewModel.class);
        fromCartViewModel = ViewModelProviders.of(activity).get(FromCartViewModel.class);
        toHistoryViewModel = ViewModelProviders.of(activity).get(ToHistoryViewModel.class);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        SearchListItemBinding searchListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.search_list_item,parent,false);
        return new SearchViewHolder(searchListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        currentProduct = productList.get(position);
        holder.binding.txtProductName.setText(currentProduct.getProductName());

        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String formattedPrice = formatter.format(currentProduct.getProductPrice());
        holder.binding.txtProductPrice.setText(formattedPrice + " EGP");

        // Load the Product image into ImageView
        String imageUrl = LOCALHOST + currentProduct.getProductImage().replaceAll("\\\\", "/");
        Glide.with(mContext)
                .load(imageUrl)
                .into(holder.binding.imgProductImage);

        // If product is inserted
        if (currentProduct.isFavourite()==1){
            holder.binding.imgFavourite.setImageResource(R.drawable.ic_favorite_pink);
        }

        // If product is added to cart
        if (currentProduct.isInCart()==1) {
            holder.binding.imgCart.setImageResource(R.drawable.ic_shopping_cart_green);
        }
    }

    @Override
    public int getItemCount() {
        if (productList == null) {
            return 0;
        }
        return productList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void clear() {
        int size = productList.size();
        productList.clear();
        notifyItemRangeRemoved(0, size);
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Create view instances
        private final SearchListItemBinding binding;

        private SearchViewHolder(SearchListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            // Register a callback to be invoked when this view is clicked.
            itemView.setOnClickListener(this);
            binding.imgFavourite.setOnClickListener(this);
            binding.imgCart.setOnClickListener(this);
            binding.addToCart.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            // Get position of the product
            currentProduct = productList.get(position);

            switch (v.getId()) {
                case R.id.card_view:
                    // Send product through click
                    clickHandler.onClick(currentProduct);
                    insertProductToHistory();
                    break;
                case R.id.imgFavourite:
                    toggleFavourite();
                    break;
                case R.id.imgCart:
                    toggleProductsInCart();
                    break;
                case R.id.addToCart:
                    addToCart();
                    break;
            }
        }


        private void toggleFavourite() {
            // If favorite is not bookmarked
            if (currentProduct.isFavourite() != 1) {
                binding.imgFavourite.setImageResource(R.drawable.ic_favorite_pink);
                insertFavoriteProduct(() -> {
                    currentProduct.setIsFavourite(true);
                    notifyDataSetChanged();
                });
                showSnackBar("Bookmark Added");
            } else {
                binding.imgFavourite.setImageResource(R.drawable.ic_favorite_border);
                deleteFavoriteProduct(() -> {
                    currentProduct.setIsFavourite(false);
                    notifyDataSetChanged();
                });
                showSnackBar("Bookmark Removed");
            }
        }

        private void toggleProductsInCart() {
            // If Product is not added to cart
            if (currentProduct.isInCart() != 1) {
                binding.imgCart.setImageResource(R.drawable.ic_shopping_cart_green);
                insertToCart(() -> {
                    currentProduct.setIsInCart(true);
                    notifyDataSetChanged();
                });
                showSnackBar("Added To Cart");
            } else {
                binding.imgCart.setImageResource(R.drawable.ic_add_shopping_cart);
                deleteFromCart(() -> {
                    currentProduct.setIsInCart(false);
                    notifyDataSetChanged();
                });
                showSnackBar("Removed From Cart");
            }
        }

        private void addToCart() {
            // If Product is not added to cart
            if (currentProduct.isInCart() != 1) {
                binding.imgCart.setImageResource(R.drawable.ic_shopping_cart_green);
                insertToCart(() -> {
                    currentProduct.setIsInCart(true);
                    notifyDataSetChanged();
                });
                showSnackBar("Added To Cart");
            }
        }

        private void showSnackBar(String text) {
            Snackbar.make(itemView, text, Snackbar.LENGTH_SHORT).show();
        }

        private void insertFavoriteProduct(RequestCallback callback) {
            Favorite favorite = new Favorite(LoginUtils.getInstance(mContext).getUserInfo().getId(), currentProduct.getProductId());
            addFavoriteViewModel.addFavorite(favorite,callback);
        }

        private void deleteFavoriteProduct(RequestCallback callback) {
            removeFavoriteViewModel.removeFavorite(LoginUtils.getInstance(mContext).getUserInfo().getId(), currentProduct.getProductId(),callback);
        }

        private void insertToCart(RequestCallback callback) {
            Cart cart = new Cart(LoginUtils.getInstance(mContext).getUserInfo().getId(), currentProduct.getProductId());
            toCartViewModel.addToCart(cart, callback);
        }

        private void deleteFromCart(RequestCallback callback) {
            fromCartViewModel.removeFromCart(LoginUtils.getInstance(mContext).getUserInfo().getId(), currentProduct.getProductId(),callback);
        }

        private void insertProductToHistory() {
            History history = new History(LoginUtils.getInstance(mContext).getUserInfo().getId(), currentProduct.getProductId());
            toHistoryViewModel.addToHistory(history);
        }
    }
}
