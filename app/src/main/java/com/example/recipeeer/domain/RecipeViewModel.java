package com.example.recipeeer.domain;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;
//    private LiveData<List<Recipe>> allMyRecipes;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
    }

    public LiveData<List<Recipe>> getAllMyRecipes(String email) {
        return recipeRepository.getAllMyRecipes(email);
    }

    public int insert(Recipe recipe) {
        return recipeRepository.insert(recipe);
    }
}
