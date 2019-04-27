package com.example.recipeeer.db;

import com.example.recipeeer.domain.Favorites;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FavoritesDao {
    @Query("SELECT * FROM Favorites WHERE Favorites.userId=:userId")
    LiveData<List<Favorites>> getFavoriteRecipesForUser(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Favorites favorites);

    @Query("DELETE FROM Favorites WHERE Favorites.userId=:userId AND Favorites.recipeId=:recipeId")
    void delete(int userId, String recipeId);
}
