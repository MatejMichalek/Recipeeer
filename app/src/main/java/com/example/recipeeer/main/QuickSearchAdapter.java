package com.example.recipeeer.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.recipeeer.R;
import com.example.recipeeer.domain.QuickSearchItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QuickSearchAdapter extends RecyclerView.Adapter<QuickSearchAdapter.ViewHolder> {

    private List<QuickSearchItem> items;
    private OnSearchIconClickListener mListener;

    public QuickSearchAdapter(List<QuickSearchItem> items, OnSearchIconClickListener mListener) {
        this.items = items;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public QuickSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recyclerview_quick_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuickSearchAdapter.ViewHolder holder, int position) {
        holder.textView.setText(items.get(position).name);
        holder.imageView.setImageResource(items.get(position).src);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textView;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.recyclerView_quickSearch_text);
            imageView = itemView.findViewById(R.id.recyclerView_quickSearch_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onSearchIconClick(items.get(getAdapterPosition()).name);
        }
    }
}
