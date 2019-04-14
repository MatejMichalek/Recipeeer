package com.example.recipeeer.domain;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

public class RecipeRepository {
    private RecipeDao recipeDao;
//    private LiveData<List<Recipe>> allMyRecipes;

    public RecipeRepository(Application application) {
        RecipeeerDatabase db = RecipeeerDatabase.getDatabase(application);
        recipeDao = db.recipeDao();
    }

    public LiveData<List<Recipe>> getAllMyRecipes(String email) {
        return recipeDao.getAllMyRecipes(email);
    }

    public int insert(Recipe recipe) {
        try {
            return (int)(long) new insertAsyncTask(recipeDao).execute(recipe).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return -1;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static class insertAsyncTask extends AsyncTask<Recipe,Void,Long>{

        private RecipeDao asyncTaskDao;

        public insertAsyncTask(RecipeDao recipeDao) {
            asyncTaskDao = recipeDao;
        }

        @Override
        protected Long doInBackground(Recipe... recipes) {
            return asyncTaskDao.insert(recipes[0]);
        }
    }
}
