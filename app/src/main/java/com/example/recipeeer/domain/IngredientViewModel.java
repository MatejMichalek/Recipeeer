package com.example.recipeeer.domain;

import android.app.Application;

import com.example.recipeeer.db.IngredientRepository;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class IngredientViewModel extends AndroidViewModel {

    private final IngredientRepository ingredientRepository;
    private List<Ingredient> ingredientsToAdd;

    public IngredientViewModel(@NonNull Application application) {
        super(application);
        ingredientRepository = new IngredientRepository(application);
        ingredientsToAdd = new LinkedList<>();
    }

    public int addIngredient(Ingredient ingredient) {
        ingredientsToAdd.add(ingredient);
        return ingredientsToAdd.size();
    }

    public int removeIngredient(int index) {
        if (ingredientsToAdd.size() > 0)
            ingredientsToAdd.remove(index);
        return ingredientsToAdd.size();
    }

    public LiveData<List<Ingredient>> getIngredientsForRecipe(int recipeID) {
        return ingredientRepository.getIngredientsForRecipe(recipeID);
    }

    public List<Ingredient> getIngredientsToAdd() {
        return ingredientsToAdd;
    }

    public void insertIngredientsForRecipe(int recipeID) {
        setRecipeID(recipeID);
        ingredientRepository.insertIngredients(ingredientsToAdd);
    }

    private void setRecipeID(int recipeID) {
        for (Ingredient ingredient: ingredientsToAdd) {
            ingredient.setRecipeId(recipeID);
        }
    }
}
