package com.example.recipeeer.db;

import com.example.recipeeer.domain.Ingredient;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface IngredientDao {
    @Insert
    void insertIngredients(List<Ingredient> ingredients);

    @Query("SELECT * FROM Ingredient WHERE recipeId=:recipeID")
    LiveData<List<Ingredient>> getIngredientsForRecipe(int recipeID);
}
