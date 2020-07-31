package com.example.beckart.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.beckart.R;
import java.util.List;

public class WordAdapter extends ArrayAdapter<String> {


    public WordAdapter(Context context, List<String> words) {
        super(context, 0, words);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.word_list_item, parent, false);
        }

        final String currentWord = getItem(position);

        TextView name = listItemView.findViewById(R.id.txtWord);
        name.setText(currentWord);

        return listItemView;
    }
}
