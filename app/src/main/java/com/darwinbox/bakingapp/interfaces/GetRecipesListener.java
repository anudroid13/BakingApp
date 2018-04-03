package com.darwinbox.bakingapp.interfaces;

import com.darwinbox.bakingapp.models.RecipeModel;

import java.util.ArrayList;

public interface GetRecipesListener {

    void onSuccess(ArrayList<RecipeModel> arrayList);

    void onFailure();
}
