package com.example.recipeeer.recipeDetails;

import android.app.Application;

import com.example.recipeeer.db.IngredientRepository;
import com.example.recipeeer.db.RecipeRepository;
import com.example.recipeeer.domain.Favorites;
import com.example.recipeeer.domain.Ingredient;
import com.example.recipeeer.domain.Recipe;
import com.example.recipeeer.domain.RecipeFromAPI;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class DetailsViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
        ingredientRepository = new IngredientRepository(application);
    }

    public LiveData<Recipe> getRecipeById(int recipeID) {
        return recipeRepository.getRecipeById(recipeID);
    }

    public LiveData<List<Ingredient>> getIngredientsForRecipe(int recipeID) {
        return ingredientRepository.getIngredientsForRecipe(recipeID);
    }

    public RecipeFromAPI getRecipeFromApi(String recipeID) {
        RecipeFromAPI recipe = recipeRepository.getRecipeFromApi(recipeID);
        return recipe;
    }

    public LiveData<List<Favorites>> getFavoritesForUser(int userId) {
        return recipeRepository.getFavoritesForUser(userId);
    }

    public void insert(Favorites favorites) {
        recipeRepository.insert(favorites);
    }

    public void delete(Favorites favorites)  {
        recipeRepository.delete(favorites);
    }

    public int delete(int recipeID) {
        return recipeRepository.delete(recipeID);
    }

}
