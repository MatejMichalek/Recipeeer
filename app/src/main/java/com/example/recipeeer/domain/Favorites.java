package com.example.recipeeer.domain;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;

import static androidx.room.ForeignKey.CASCADE;


@Entity(primaryKeys = {"userId","recipeId"},foreignKeys = @ForeignKey(entity = User.class,parentColumns = "id",childColumns = "userId",onDelete = CASCADE),
        indices = {@Index("recipeId"),@Index("userId")})
public class Favorites {

    private int userId;
    @NonNull
    private String recipeId;
    private String title;
    private int preparationTime;

    public Favorites(int userId, String recipeId, String title, int preparationTime) {
        this.userId = userId;
        this.recipeId = recipeId;
        this.title = title;
        this.preparationTime = preparationTime;
    }

    @Ignore
    public Favorites(int userId, @NonNull String recipeId) {
        this.userId = userId;
        this.recipeId = recipeId;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public String getTitle() {
        return title;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public int getUserId() {
        return userId;
    }
}
