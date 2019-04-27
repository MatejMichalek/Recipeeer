package com.example.recipeeer.db;

import com.example.recipeeer.domain.User;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insert(User user);

    @Query("SELECT * FROM User WHERE email=:email")
    LiveData<User> getUserByEmail(String email);

    @Query("UPDATE User SET username=:updatedValue WHERE email=:email")
    void updateUsername(String email, String updatedValue);

    @Query("UPDATE User SET gender=:gender WHERE email=:email")
    void updateUserGender(String email, int gender);
}
