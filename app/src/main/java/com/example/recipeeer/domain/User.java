package com.example.recipeeer.domain;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index(value = "email",unique = true))
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String email;

    @NonNull
    @ColumnInfo(name = "username")
    private String name;

    private int age;

    @NonNull
    private int gender;

    public User(int id, @NonNull String email, @NonNull String name, int age, @NonNull int gender) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    @Ignore
    public User(@NonNull String email, @NonNull String name, int age, int gender) {
        this.email = email;
        this.name = name;
        this.age = age;
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

    public int getAge() {
        return age;
    }

    @NonNull
    public int getGender() {
        return gender;
    }
}
