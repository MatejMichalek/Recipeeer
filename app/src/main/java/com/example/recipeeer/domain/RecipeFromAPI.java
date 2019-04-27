package com.example.recipeeer.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeFromAPI {
    @SerializedName("id")
    @Expose
    public String recipeID;
    @Expose
    public String title;
    @SerializedName("readyInMinutes")
    @Expose
    public int preparationTime;

    @SerializedName("sourceName")
    public String publisher;

    public String instructions;

    @SerializedName("image")
    public String imageURL;

    @SerializedName("extendedIngredients")
    public List<Ingredient> ingredients;

    public RecipeFromAPI(String recipeID, String title, int preparationTime) {
        this.recipeID = recipeID;
        this.title = title;
        this.preparationTime = preparationTime;
    }

    public RecipeFromAPI(String recipeID, String title, int preparationTime, String publisher, String instructions, String imageURL) {
        this.recipeID = recipeID;
        this.title = title;
        this.preparationTime = preparationTime;
        this.publisher = publisher;
        this.instructions = instructions;
        this.imageURL = imageURL;
    }
}
