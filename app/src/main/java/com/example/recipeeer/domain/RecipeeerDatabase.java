package com.example.recipeeer.domain;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class,Recipe.class,Ingredient.class},version = 1,exportSchema = false)
public abstract class RecipeeerDatabase extends RoomDatabase {

    private static volatile RecipeeerDatabase INSTANCE;

    public static RecipeeerDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RecipeeerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            RecipeeerDatabase.class,
                            "recipeeer_db")
//                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract UserDao userDao();
    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();

    private static RoomDatabase.Callback
            sRoomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void,Void,Void> {

        private final UserDao userDao;
        private final RecipeDao recipeDao;

        public PopulateDbAsync(RecipeeerDatabase instance) {
            userDao = instance.userDao();
            recipeDao = instance.recipeDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Recipe recipe = new Recipe("My first recipe",85,"cdcsdcasda",97);
            recipeDao.insert(recipe);
            recipe = new Recipe("My second recipe",40,"cdcsddfsq dsa ",97);
            recipeDao.insert(recipe);
            recipe = new Recipe("Recipe from somebody else",550,"asfa sldasn doals kdaos dka d",95);
            recipeDao.insert(recipe);
            recipe = new Recipe("My third recipe",400,"af asf ajuohjnfgf dsa ",97);
            recipeDao.insert(recipe);
//            userDao.deleteAll();
//            User user = new User("MyEmail","Matej",21,1);
//            userDao.insert(user);
//            user = new User("MyEmail2","Lukas",41,1);
//            userDao.insert(user);
            return null;
        }
    }
}
