package com.example.recipeeer.domain;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

public class RecipeRepository {
    private RecipeDao recipeDao;
//    private LiveData<List<Recipe>> allMyRecipes;

    public RecipeRepository(Application application) {
        RecipeeerDatabase db = RecipeeerDatabase.getDatabase(application);
        recipeDao = db.recipeDao();
    }

    public LiveData<List<Recipe>> getAllMyRecipes(String email) {
        return recipeDao.getAllMyRecipes(email);
    }
}
