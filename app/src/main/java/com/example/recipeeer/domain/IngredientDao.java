package com.example.recipeeer.domain;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface IngredientDao {
    @Insert
    public void insertIngredients(List<Ingredient> ingredients);

    @Query("SELECT * FROM Ingredient WHERE recipeId=:recipeID")
    public LiveData<List<Ingredient>> getIngredientsForRecipe(int recipeID);
}
