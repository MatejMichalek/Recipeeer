package com.example.recipeeer.favorites;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recipeeer.R;
import com.example.recipeeer.domain.Favorites;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private List<Favorites> favorites;
    private final OnListItemClickListener mOnListItemClickListener;

    public FavoritesListAdapter(Context context, OnListItemClickListener mOnListItemClickListener) {
        inflater = LayoutInflater.from(context);
        this.mOnListItemClickListener = mOnListItemClickListener;
    }

    @NonNull
    @Override
    public FavoritesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_myrecipes_item,parent,false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesListAdapter.ViewHolder holder, int position) {
        if (favorites != null) {
            Favorites favoriteRecipe = favorites.get(position);
            holder.recipeNameView.setText(favoriteRecipe.getTitle());
            holder.preparationTimeView.setText(String.valueOf(favoriteRecipe.getPreparationTime()+" min"));
        }
        else {
            holder.recipeNameView.setText("No recipe");
        }
    }

    @Override
    public int getItemCount() {
        if (favorites != null) {
            return favorites.size();
        }
        else return 0;
    }

    public void setFavorites(List<Favorites> favorites) {
        this.favorites = favorites;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView recipeNameView;
        private final TextView preparationTimeView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameView = itemView.findViewById(R.id.recyclerViewItemRecipeNameText);
            preparationTimeView = itemView.findViewById(R.id.recyclerViewItemRecipeTimeText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Favorites recipe = favorites.get(getAdapterPosition());
            mOnListItemClickListener.onListItemClick(recipe.getRecipeId());
        }
    }

    public interface OnListItemClickListener {
        void onListItemClick(String recipeID);
    }
}
