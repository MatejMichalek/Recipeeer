package com.example.recipeeer.domain;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    public void insert(User user);

    @Query("SELECT * FROM User WHERE email=:email")
    public LiveData<User> getUserByEmail(String email);

    @Query("SELECT * FROM User")
    public LiveData<List<User>> getAllUsers();
}
