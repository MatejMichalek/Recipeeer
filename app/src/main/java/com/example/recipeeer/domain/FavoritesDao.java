package com.example.recipeeer.domain;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FavoritesDao {
    @Query("SELECT * FROM Favorites WHERE Favorites.userId=:userId")
    public LiveData<List<Favorites>> getFavoriteRecipesForUser(int userId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Favorites favorites);

    @Query("DELETE FROM Favorites WHERE Favorites.userId=:userId AND Favorites.recipeId=:recipeId")
    public void delete(int userId, String recipeId);
}
