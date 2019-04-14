package com.example.recipeeer.domain;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RecipeDao {
    @Insert
    public long insert(Recipe recipe);

    @Query("SELECT r.recipeid,r.name,r.preparationTime,r.instruction,r.userId " +
            "FROM Recipe r, User u WHERE r.userId=u.id AND u.email=:email")
    public LiveData<List<Recipe>> getAllMyRecipes(String email);
}
