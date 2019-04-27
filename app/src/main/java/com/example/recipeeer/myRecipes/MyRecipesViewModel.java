package com.example.recipeeer.myRecipes;

import android.app.Application;

import com.example.recipeeer.db.RecipeRepository;
import com.example.recipeeer.domain.Recipe;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MyRecipesViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;

    public MyRecipesViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
    }

    public LiveData<List<Recipe>> getAllMyRecipes(String email) {
        return recipeRepository.getAllMyRecipes(email);
    }
}
