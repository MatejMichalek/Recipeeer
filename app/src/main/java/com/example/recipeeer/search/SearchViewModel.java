package com.example.recipeeer.search;

import android.app.Application;

import com.example.recipeeer.db.RecipeRepository;
import com.example.recipeeer.domain.RecipeListFromAPI;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SearchViewModel extends AndroidViewModel {

    private RecipeRepository recipeRepository;
    private LiveData<PagingHelper> pagingHelper;
    private LiveData<RecipeListFromAPI> searchedRecipes;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        recipeRepository = new RecipeRepository(application);
        pagingHelper = new MutableLiveData<>();
        // initialize paging helper
        ((MutableLiveData<PagingHelper>) pagingHelper).setValue(new PagingHelper(0,0));
        searchedRecipes = new MutableLiveData<>();
    }

    public void search(String searchTerm, int page) {
        PagingHelper helper = pagingHelper.getValue();
        RecipeListFromAPI list = recipeRepository.getSearchedRecipes(searchTerm,helper.getDesiredOffset(page));
        // update values in Paging helper and notify observers
        setPagingHelper(list.totalRecipes,list.offset);
        ((MutableLiveData<RecipeListFromAPI>) searchedRecipes).setValue(list);
    }

    public LiveData<RecipeListFromAPI> getSearchedRecipes() {
        return searchedRecipes;
    }

    public LiveData<PagingHelper> getPagingHelper() {
        return pagingHelper;
    }

    private void setPagingHelper(int totalNoOfRecipes, int offset) {
        ((MutableLiveData<PagingHelper>) pagingHelper).setValue(new PagingHelper(totalNoOfRecipes,offset));
    }
}
