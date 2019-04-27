package com.example.recipeeer.api;

import com.example.recipeeer.domain.RecipeFromAPI;
import com.example.recipeeer.domain.RecipeListFromAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndpoint {

    String HOST = "X-RapidAPI-Host: spoonacular-recipe-food-nutrition-v1.p.rapidapi.com";
    String KEY = "X-RapidAPI-Key: 78217df556mshae8b7604e3c431ap14cd62jsne18679b4e3cb";

    @GET("recipes/search?number=16&instructionsRequired=true")
    @Headers({HOST,KEY})
    Call<RecipeListFromAPI> getSearchedRecipes(@Query("query") String searchTerm, @Query("offset") int offset);

    @GET("recipes/{recipeID}/information")
    @Headers({HOST,KEY})
    Call<RecipeFromAPI> getRecipeFromApi(@Path("recipeID") String recipeID);
}
