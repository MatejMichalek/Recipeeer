package com.example.recipeeer.domain;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    public void insert(User user);

    @Query("SELECT * FROM User WHERE email=:email")
    public User getCurrentUserByEmail(String email);

    @Query("SELECT * FROM User ORDER BY email DESC")
    public LiveData<List<User>> getAllUsers();

    @Query("DELETE FROM User")
    public void deleteAll();

    @Query("SELECT COUNT(User.email) FROM User WHERE email=:email")
    public Integer userExists(String email);

    @Query("SELECT * FROM User WHERE email=:email")
    public LiveData<User> getUserByEmail(String email);
}
