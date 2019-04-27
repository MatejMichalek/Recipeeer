package com.example.recipeeer.db;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.recipeeer.api.ApiEndpoint;
import com.example.recipeeer.api.ApiHandler;
import com.example.recipeeer.domain.Favorites;
import com.example.recipeeer.domain.Recipe;
import com.example.recipeeer.domain.RecipeFromAPI;
import com.example.recipeeer.domain.RecipeListFromAPI;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Response;

public class RecipeRepository {
    private RecipeDao recipeDao;
    private FavoritesDao favoritesDao;
    private ApiEndpoint apiEndpointService;

//    private LiveData<List<Recipe>> allMyRecipes;

    public RecipeRepository(Application application) {
        RecipeeerDatabase db = RecipeeerDatabase.getDatabase(application);
        recipeDao = db.recipeDao();
        favoritesDao = db.favoritesDao();
    }

    public LiveData<List<Recipe>> getAllMyRecipes(String email) {
        return recipeDao.getAllMyRecipes(email);
    }

    public LiveData<Recipe> getRecipeById(int recipeID) {
        return recipeDao.getRecipeById(recipeID);
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

    public void insert(Favorites favorites) {
        new insertFavoritesAsyncTask(favoritesDao).execute(favorites).getStatus();
    }

    public void delete(Favorites favorites) {
        new deleteFavoritesAsyncTask(favoritesDao).execute(favorites);
    }

    public int delete(int recipeID) {
        try {
            return new deleteAsyncTask(recipeDao).execute(recipeID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return -1;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public RecipeListFromAPI getSearchedRecipes(String searchTerm, int offset) {
        try {

            apiEndpointService = ApiHandler.createEndpoint(true);
            return new getSearchAsyncTask(apiEndpointService).execute(new SearchWrapper(searchTerm,offset)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RecipeFromAPI getRecipeFromApi(String recipeID) {
        try {
            apiEndpointService = ApiHandler.createEndpoint(false);
            return new getRecipeFromApiAsyncTask(apiEndpointService).execute(recipeID).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public LiveData<List<Favorites>> getFavoritesForUser(int userId) {
        return favoritesDao.getFavoriteRecipesForUser(userId);
    }

    private class SearchWrapper{
        String searchTerm;
        int offset;

        SearchWrapper(String searchTerm, int offset) {
            this.searchTerm = searchTerm;
            this.offset = offset;
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

    private static class deleteAsyncTask extends AsyncTask<Integer,Void,Integer> {

        private RecipeDao asyncTaskDao;

        public deleteAsyncTask(RecipeDao recipeDao) {
            asyncTaskDao = recipeDao;
        }

        @Override
        protected Integer doInBackground(Integer... integers) {
            return asyncTaskDao.delete(integers[0]);
        }
    }

    private class getSearchAsyncTask extends AsyncTask<SearchWrapper,Void,RecipeListFromAPI>{
        private ApiEndpoint asyncTaskApi;

        public getSearchAsyncTask(ApiEndpoint apiEndpointService) {
            asyncTaskApi = apiEndpointService;
        }


        @Override
        protected RecipeListFromAPI doInBackground(SearchWrapper... searchWrappers) {
            Call<RecipeListFromAPI> call = apiEndpointService.getSearchedRecipes(searchWrappers[0].searchTerm,searchWrappers[0].offset);
            Log.i("REQUEST URL",call.request().url().toString());

            Response<RecipeListFromAPI> response;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            Log.i("RESPONSE STATUS CODE",String.valueOf(response.code()));
            Log.i("RESPONSE BODY",String.valueOf(response.raw().body()));
            return response.body();
        }
    }

    private class getRecipeFromApiAsyncTask extends AsyncTask<String,Void,RecipeFromAPI>{

        private ApiEndpoint asyncTaskApi;

        public getRecipeFromApiAsyncTask(ApiEndpoint apiEndpointService) {
            asyncTaskApi = apiEndpointService;
        }

        @Override
        protected RecipeFromAPI doInBackground(String... strings) {
            Call<RecipeFromAPI> call = apiEndpointService.getRecipeFromApi(strings[0]);
            Log.i("REQUEST URL",call.request().url().toString());

            Response<RecipeFromAPI> response;
            try {
                response = call.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            Log.i("RESPONSE STATUS CODE",String.valueOf(response.code()));
            Log.i("RESPONSE BODY",String.valueOf(response.raw().body()));
            return response.body();
        }
    }

    private static class insertFavoritesAsyncTask extends AsyncTask<Favorites,Void,Void> {
        private FavoritesDao asyncTask;

        public insertFavoritesAsyncTask(FavoritesDao favoritesDao) {
            asyncTask = favoritesDao;
        }

        @Override
        protected Void doInBackground(Favorites... favorites) {
            asyncTask.insert(favorites[0]);
            return null;
        }
    }

    private static class deleteFavoritesAsyncTask extends AsyncTask<Favorites,Void,Void> {
        private FavoritesDao asyncTask;

        public deleteFavoritesAsyncTask(FavoritesDao favoritesDao) {
            asyncTask = favoritesDao;
        }

        @Override
        protected Void doInBackground(Favorites... favorites) {
            asyncTask.delete(favorites[0].getUserId(),favorites[0].getRecipeId());
            return null;
        }
    }
}
