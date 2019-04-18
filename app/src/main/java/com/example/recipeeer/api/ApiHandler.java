package com.example.recipeeer.api;

import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHandler {
    public static final String BASE_URL = "https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/";

    public static ApiEndpoint createEndpoint(boolean onlyWithExposed) {
        if (onlyWithExposed) {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                    .build().create(ApiEndpoint.class);
        }
        else {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiEndpoint.class);
        }
    }

}
