package com.example.recipeeer.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity (foreignKeys = @ForeignKey(entity = Recipe.class,parentColumns = "recipeid",childColumns = "recipeId",onDelete = CASCADE),indices = {@Index("id"),@Index("recipeId")})
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    @Expose
    private int id;

    @NonNull
    @SerializedName("originalString")
    private String value;

    @Expose
    private int recipeId;

    public Ingredient(int id, @NonNull String value, int recipeId) {
        this.id = id;
        this.value = value;
        this.recipeId = recipeId;
    }

    @Ignore
    public Ingredient(@NonNull String value) {
        this.value = value;
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

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}
