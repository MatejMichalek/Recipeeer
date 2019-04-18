package com.example.recipeeer.domain;

import com.google.gson.annotations.SerializedName;

public class RecipeFromAPI {
    @SerializedName("id")
    String recipeID;
    String title;
    @SerializedName("readyInMinutes")
    int preparationTime;

    public RecipeFromAPI(String recipeID, String title, int preparationTime) {
        this.recipeID = recipeID;
        this.title = title;
        this.preparationTime = preparationTime;
    }


}
