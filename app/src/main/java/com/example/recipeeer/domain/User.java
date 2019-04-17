package com.example.recipeeer.domain;

import androidx.annotation.NonNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = "email",unique = true),@Index("id")})
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String email;

    @NonNull
    @ColumnInfo(name = "username")
    private String name;

    private int gender;

    public User(int id, @NonNull String email, @NonNull String name, int gender) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.gender = gender;
    }

    @Ignore
    public User(@NonNull String email, @NonNull String name, int gender) {
        this.email = email;
        this.name = name;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }
}
