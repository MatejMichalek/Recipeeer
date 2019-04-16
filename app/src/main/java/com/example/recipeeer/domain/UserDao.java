package com.example.recipeeer.domain;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert
    public void insert(User user);

    @Query("SELECT * FROM User WHERE email=:email")
    public User getUserByEmail(String email);

    @Query("SELECT * FROM User ORDER BY email DESC")
    public LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM User")
    public void deleteAll();
}
