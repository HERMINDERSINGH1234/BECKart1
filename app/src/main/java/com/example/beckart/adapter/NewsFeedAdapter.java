package com.example.beckart.adapter;

import android.content.Context;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.bumptech.glide.Glide;
import com.example.beckart.R;
import com.example.beckart.databinding.NewsfeedListItemBinding;
import com.example.beckart.model.NewsFeed;

import java.util.List;

import static com.example.beckart.utils.Constant.LOCALHOST;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.NewsFeedViewHolder>{

    private Context mContext;
    private List<NewsFeed> newsFeedList;

    public NewsFeedAdapter(Context mContext, List<NewsFeed> newsFeedList) {
        this.mContext = mContext;
        this.newsFeedList = newsFeedList;
    }

    @NonNull
    @Override
    public NewsFeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        NewsfeedListItemBinding newsfeedListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.newsfeed_list_item,parent,false);
        return new NewsFeedViewHolder(newsfeedListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsFeedViewHolder holder, int position) {
        NewsFeed currentNewsFeed = newsFeedList.get(position);

        // Load poster into ImageView
        String posterUrl = LOCALHOST + currentNewsFeed.getImage().replaceAll("\\\\", "/");
        Glide.with(mContext)
                .load(posterUrl)
                .into(holder.binding.poster);
    }

    @Override
    public int getItemCount() {
        if (newsFeedList == null) {
            return 0;
        }
        return newsFeedList.size();
    }

    class NewsFeedViewHolder extends RecyclerView.ViewHolder {

        private final NewsfeedListItemBinding binding;

        private NewsFeedViewHolder(NewsfeedListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
