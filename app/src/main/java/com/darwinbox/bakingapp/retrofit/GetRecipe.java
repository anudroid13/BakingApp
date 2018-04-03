package com.darwinbox.bakingapp.retrofit;

import com.darwinbox.bakingapp.models.RecipeModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetRecipe {

    @GET("baking.json")
    Call<ArrayList<RecipeModel>> getRecipe();
}
