package com.example.recipeeer.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipeListFromAPI {
    @SerializedName("results")
    List<RecipeFromAPI> recipes;

    int offset;

    @SerializedName("number")
    int noOfRecipes;

    @SerializedName("totalResults")
    int totalRecipes;


    public RecipeListFromAPI() {
        recipes = new ArrayList<>();
    }

    public static RecipeListFromAPI parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(response,RecipeListFromAPI.class);
    }
}
