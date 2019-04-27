package com.example.recipeeer.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recipeeer.R;
import com.example.recipeeer.domain.RecipeFromAPI;
import com.example.recipeeer.domain.RecipeListFromAPI;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchedRecipesListAdapter extends RecyclerView.Adapter<SearchedRecipesListAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private RecipeListFromAPI searchedRecipes;
    private final OnSearchListItemClickListener mOnSearchListItemClickListener;

    public SearchedRecipesListAdapter(Context context,  OnSearchListItemClickListener mOnSearchListItemClickListener) {
        inflater = LayoutInflater.from(context);
        this.mOnSearchListItemClickListener = mOnSearchListItemClickListener;
    }

    @NonNull
    @Override
    public SearchedRecipesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_myrecipes_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchedRecipesListAdapter.ViewHolder holder, int position) {
        if (searchedRecipes != null) {
            RecipeFromAPI recipe = searchedRecipes.recipes.get(position);
            holder.recipeNameView.setText(recipe.title);
            holder.preparationTimeView.setText(String.valueOf(recipe.preparationTime +" min"));
        }
        else {
            holder.recipeNameView.setText("No recipe");
        }
    }

    @Override
    public int getItemCount() {
        if (searchedRecipes != null) {
            return searchedRecipes.recipes.size();
        }
        else return 0;
    }

    public void setSearchedRecipes(RecipeListFromAPI searchedRecipes) {
        this.searchedRecipes = searchedRecipes;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView recipeNameView;
        private final TextView preparationTimeView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameView = itemView.findViewById(R.id.recyclerViewItemRecipeNameText);
            preparationTimeView = itemView.findViewById(R.id.recyclerViewItemRecipeTimeText);
            itemView.setOnClickListener(this);        }

        @Override
        public void onClick(View v) {
            RecipeFromAPI recipe = searchedRecipes.recipes.get(getAdapterPosition());
            mOnSearchListItemClickListener.onListItemClick(recipe.recipeID);
        }
    }

    public interface OnSearchListItemClickListener {
        void onListItemClick(String recipeID);
    }
}
