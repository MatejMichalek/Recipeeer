package com.example.recipeeer.domain;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class,Recipe.class,Ingredient.class},version = 1)
public abstract class RecipeeerDatabase extends RoomDatabase {

    private static volatile RecipeeerDatabase INSTANCE;

    static RecipeeerDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeeerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RecipeeerDatabase.class,
                            "recipeeer_db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract UserDao userDao();
    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();
}
