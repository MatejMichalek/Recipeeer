package com.example.recipeeer.createRecipe;

import android.app.Application;

import com.example.recipeeer.db.IngredientRepository;
import com.example.recipeeer.db.RecipeRepository;
import com.example.recipeeer.domain.Ingredient;
import com.example.recipeeer.domain.Recipe;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class CreateRecipeViewModel extends AndroidViewModel {
    private RecipeRepository recipeRepository;
    private IngredientRepository ingredientRepository;
    // temporary list of ingredients for new recipe
    private List<Ingredient> ingredientsToAdd;

    public CreateRecipeViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
        ingredientRepository = new IngredientRepository(application);
        ingredientsToAdd = new LinkedList<>();
    }

    public int insert(Recipe recipe) {
        return recipeRepository.insert(recipe);
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

    // finally save list of ingredients for given recipe to the local storage
    public void insertIngredientsForRecipe(int recipeID) {
        setRecipeID(recipeID);
        ingredientRepository.insertIngredients(ingredientsToAdd);
    }

    // update recipeID field in all temporary ingredients
    private void setRecipeID(int recipeID) {
        for (Ingredient ingredient: ingredientsToAdd) {
            ingredient.setRecipeId(recipeID);
        }
    }
}
