package com.example.beckart.adapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.beckart.R;
import com.example.beckart.databinding.HelpListItemBinding;
import com.example.beckart.model.Help;

import java.util.List;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.HelpCenterHolder>{

    private List<Help> helpList;

    public HelpAdapter(List<Help> helpList) {
        this.helpList = helpList;
    }

    @NonNull
    @Override
    public HelpCenterHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        HelpListItemBinding helpListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.help_list_item,parent,false);
        return new HelpCenterHolder(helpListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpCenterHolder holder, int position) {
        Help currentHelp = helpList.get(position);
        holder.binding.txtQuestion.setText(currentHelp.getQuestion());
        holder.binding.txtAnswer.setText(String.valueOf(currentHelp.getAnswer()));

    }

    @Override
    public int getItemCount() {
        if (helpList == null) {
            return 0;
        }
        return helpList.size();
    }

    class HelpCenterHolder extends RecyclerView.ViewHolder{

        private final HelpListItemBinding binding;

        private HelpCenterHolder(HelpListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
