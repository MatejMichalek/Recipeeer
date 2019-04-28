package com.example.recipeeer.api;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHandler {
    private static final String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/";

    public static ApiEndpoint createEndpoint(boolean onlyWithExposed) {

        if (onlyWithExposed) {
            // serialization only of fields with Expose annotation
            // case for serializing list of recipes without details
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                    .build().create(ApiEndpoint.class);
        }
        else {
            // case for serializing recipe with details
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiEndpoint.class);
        }
    }

}
