package com.example.recipeeer.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RecipeListFromAPI {
    @Expose
    @SerializedName("results")
    public List<RecipeFromAPI> recipes;

    @Expose
    public int offset;

    @Expose
    @SerializedName("number")
    public int noOfRecipes;

    @Expose
    @SerializedName("totalResults")
    public int totalRecipes;


    public RecipeListFromAPI() {
        recipes = new ArrayList<>();
    }

//    public static RecipeListFromAPI parseJSON(String response) {
//        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
//        return gson.fromJson(response,RecipeListFromAPI.class);
//    }
}
