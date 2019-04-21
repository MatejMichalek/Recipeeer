package com.example.recipeeer.domain;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class RecipeViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;
    private LiveData<PagingHelper> pagingHelper;
    private LiveData<RecipeListFromAPI> searchedRecipes;

//    private LiveData<List<Recipe>> allMyRecipes;

    public RecipeViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
        pagingHelper = new MutableLiveData<>();
        ((MutableLiveData<PagingHelper>) pagingHelper).setValue(new PagingHelper(0,0));
        searchedRecipes = new MutableLiveData<>();
    }

    public LiveData<RecipeListFromAPI> getSearchedRecipes(String searchTerm) {
        return searchedRecipes;
    }

    public void search(String searchTerm, int page) {
        PagingHelper pagingHelper = this.pagingHelper.getValue();
        RecipeListFromAPI list = recipeRepository.getSearchedRecipes(searchTerm,pagingHelper.getDesiredOffset(page));
        setPagingHelper(list.totalRecipes,list.offset);
        ((MutableLiveData<RecipeListFromAPI>) searchedRecipes).setValue(list);
    }

    public LiveData<List<Favorites>> getFavoritesForUser(int userId) {
        return recipeRepository.getFavoritesForUser(userId);
    }

    public LiveData<List<Recipe>> getAllMyRecipes(String email) {
        return recipeRepository.getAllMyRecipes(email);
    }

    public LiveData<Recipe> getRecipeById(int recipeID) {
        return recipeRepository.getRecipeById(recipeID);
    }

    public int insert(Recipe recipe) {
        return recipeRepository.insert(recipe);
    }

    public void insert(Favorites favorites) {
        recipeRepository.insert(favorites);
    }

    public void delete(Favorites favorites)  {
        recipeRepository.delete(favorites);
    }

    public int delete(int recipeID) {
        return recipeRepository.delete(recipeID);
    }

    public RecipeFromAPI getRecipeFromApi(String recipeID) {
        RecipeFromAPI recipe = recipeRepository.getRecipeFromApi(recipeID);
        return recipe;
    }

    public LiveData<PagingHelper> getPagingHelper() {
        return pagingHelper;
    }

    private void setPagingHelper(int totalNoOfRecipes, int offset) {
        ((MutableLiveData<PagingHelper>) pagingHelper).setValue(new PagingHelper(totalNoOfRecipes,offset));
    }
}
