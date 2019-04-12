package com.example.recipeeer.domain;

import androidx.annotation.NonNull;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

//@Entity(tableName = "recipe")
@Entity(foreignKeys = @ForeignKey(entity = User.class,parentColumns = "id",childColumns = "userId",onDelete = CASCADE),indices = {@Index("recipeid"),@Index("userId")})
public class Recipe {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "recipeid")
    private int id;

    @NonNull
    private String name;

    @NonNull
    private int preparationTime;

    @NonNull
    private String instruction;

    @NonNull
    private int userId;

    public Recipe(int id, @NonNull String name, int preparationTime, @NonNull String instruction, int userId) {
        this.id = id;
        this.name = name;
        this.preparationTime = preparationTime;
        this.instruction = instruction;
        this.userId = userId;
    }

    @Ignore
    public Recipe(@NonNull String name, int preparationTime, @NonNull String instruction, int userId) {
        this.name = name;
        this.preparationTime = preparationTime;
        this.instruction = instruction;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    @NonNull
    public String getInstruction() {
        return instruction;
    }

    public int getUserId() {
        return userId;
    }
}
