package com.example.recipeeer.db;

import android.app.Application;
import android.os.AsyncTask;

import com.example.recipeeer.domain.Ingredient;

import java.util.List;

import androidx.lifecycle.LiveData;

public class IngredientRepository {

    private IngredientDao ingredientDao;

    public IngredientRepository(Application application) {
        RecipeeerDatabase db = RecipeeerDatabase.getDatabase(application);
        ingredientDao = db.ingredientDao();
    }

    public void insertIngredients(List<Ingredient> ingredients) {
        new insertAsyncTask(ingredientDao).execute(ingredients);
    }

    public LiveData<List<Ingredient>> getIngredientsForRecipe(int recipeID) {
        return ingredientDao.getIngredientsForRecipe(recipeID);
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
