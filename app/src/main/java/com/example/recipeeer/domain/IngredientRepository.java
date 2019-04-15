package com.example.recipeeer.domain;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

public class IngredientRepository {

    private IngredientDao ingredientDao;

    public IngredientRepository(Application application) {
        RecipeeerDatabase db = RecipeeerDatabase.getDatabase(application);
        ingredientDao = db.ingredientDao();
    }

    public void insertIngredients(List<Ingredient> ingredients) {
        new insertAsyncTask(ingredientDao).execute(ingredients);
    }

    private static class insertAsyncTask extends AsyncTask<List<Ingredient>,Void,Void> {
        private IngredientDao asyncTaskDao;

        public insertAsyncTask(IngredientDao ingredientDao) {
            asyncTaskDao = ingredientDao;
        }

        @Override
        protected Void doInBackground(List<Ingredient>... lists) {
            asyncTaskDao.insertIngredients(lists[0]);
            return null;
        }
    }
}
