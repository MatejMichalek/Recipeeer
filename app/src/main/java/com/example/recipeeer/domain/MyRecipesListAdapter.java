package com.example.recipeeer.domain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.recipeeer.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecipesListAdapter extends RecyclerView.Adapter<MyRecipesListAdapter.RecipeViewHolder> {

    private final LayoutInflater inflater;
    private List<Recipe> myRecipes;

    public MyRecipesListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public MyRecipesListAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_myrecipes_item,parent,false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipesListAdapter.RecipeViewHolder holder, int position) {
        if (myRecipes != null) {
            Recipe recipe = myRecipes.get(position);
            holder.recipeNameView.setText(recipe.getName());
            holder.authorNameView.setText(String.valueOf(recipe.getUserId()));
            holder.preparationTimeView.setText(String.valueOf(recipe.getPreparationTime()));
        }
        else {
            holder.recipeNameView.setText("No recipe");
        }
    }

    @Override
    public int getItemCount() {
        if (myRecipes != null) {
            return myRecipes.size();
        }
        else return 0;
    }

    public void setMyRecipes(List<Recipe> recipes) {
        myRecipes = recipes;
        notifyDataSetChanged();
    }


    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final TextView recipeNameView;
        private final TextView authorNameView;
        private final TextView preparationTimeView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            recipeNameView = itemView.findViewById(R.id.recyclerViewItemRecipeNameText);
            authorNameView = itemView.findViewById(R.id.recyclerViewItemRecipeUserNameText);
            preparationTimeView = itemView.findViewById(R.id.recyclerViewItemRecipeTimeText);
        }
    }
}
