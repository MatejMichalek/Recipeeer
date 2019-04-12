package com.example.recipeeer.domain;

import android.support.annotation.NonNull;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity (foreignKeys = @ForeignKey(entity = Recipe.class,parentColumns = "id",childColumns = "recipeId",onDelete = CASCADE))
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String value;

    @NonNull
    private int recipeId;

    public Ingredient(int id, @NonNull String value, int recipeId) {
        this.id = id;
        this.value = value;
        this.recipeId = recipeId;
    }

    @Ignore
    public Ingredient(@NonNull String value, int recipeId) {
        this.value = value;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getValue() {
        return value;
    }

    public int getRecipeId() {
        return recipeId;
    }
}
