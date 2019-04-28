package com.example.recipeeer.db;

import android.content.Context;
import android.os.AsyncTask;

import com.example.recipeeer.domain.Favorites;
import com.example.recipeeer.domain.Ingredient;
import com.example.recipeeer.domain.Recipe;
import com.example.recipeeer.domain.User;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, Recipe.class, Ingredient.class, Favorites.class},version = 1,exportSchema = false)
public abstract class RecipeeerDatabase extends RoomDatabase {

    private static volatile RecipeeerDatabase INSTANCE;

    public static RecipeeerDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeeerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RecipeeerDatabase.class,
                            "Recipeeer")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract UserDao userDao();
    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();
    public abstract FavoritesDao favoritesDao();

}
