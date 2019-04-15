package com.example.recipeeer.domain;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface IngredientDao {
    @Insert
    public void insertIngredients(List<Ingredient> ingredients);
}
