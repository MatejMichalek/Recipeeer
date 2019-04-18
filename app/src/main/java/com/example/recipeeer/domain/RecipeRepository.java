package com.example.recipeeer.domain;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.recipeeer.api.ApiEndpoint;
import com.example.recipeeer.api.ApiHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Response;

public class RecipeRepository {
    private RecipeDao recipeDao;
    private ApiEndpoint apiEndpointService;

//    private LiveData<List<Recipe>> allMyRecipes;

    public RecipeRepository(Application application) {
        RecipeeerDatabase db = RecipeeerDatabase.getDatabase(application);
        recipeDao = db.recipeDao();
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

    public RecipeListFromAPI getSearchedRecipes(String searchTerm) {
        try {
            apiEndpointService = ApiHandler.createEndpoint(true);
            return new getSearchAsyncTask(apiEndpointService).execute(searchTerm).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }


//        call.enqueue(new Callback<RecipeListFromAPI>() {
//            @Override
//            public void onResponse(Call<RecipeListFromAPI> call, Response<RecipeListFromAPI> response) {
//                Log.i("RESPONSE STATUS CODE",String.valueOf(response.code()));
//                Log.i("RESPONSE BODY",String.valueOf(response.raw().body()));
//            }
//
//            @Override
//            public void onFailure(Call<RecipeListFromAPI> call, Throwable t) {
//                Log.i("RESPONSE STATUS CODE","FAILED");
//
//            }
//        });

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

    private class getSearchAsyncTask extends AsyncTask<String,Void,RecipeListFromAPI>{
        private ApiEndpoint asyncTaskApi;

        public getSearchAsyncTask(ApiEndpoint apiEndpointService) {
            asyncTaskApi = apiEndpointService;
        }


        @Override
        protected RecipeListFromAPI doInBackground(String... strings) {
            Call<RecipeListFromAPI> call = apiEndpointService.getSearchedRecipes(strings[0]);
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
}
