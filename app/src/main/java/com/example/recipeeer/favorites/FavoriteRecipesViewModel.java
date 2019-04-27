package com.example.recipeeer.favorites;

import android.app.Application;

import com.example.recipeeer.db.RecipeRepository;
import com.example.recipeeer.domain.Favorites;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class FavoriteRecipesViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;

    public FavoriteRecipesViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
    }

    public LiveData<List<Favorites>> getFavoritesForUser(int userId) {
        return recipeRepository.getFavoritesForUser(userId);
    }
}
