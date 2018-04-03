package com.darwinbox.bakingapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.activities.RecipeActivity;
import com.darwinbox.bakingapp.adapters.RecipeAdapter;
import com.darwinbox.bakingapp.models.Recipe;
import com.darwinbox.bakingapp.retrofit.GetRecipe;
import com.darwinbox.bakingapp.retrofit.RetroBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.darwinbox.bakingapp.activities.RecipeActivity.ALL_RECIPES;

public class RecipeFragment extends Fragment {

    public RecipeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView;

        Log.d("anudroid", "here");
        View rootView = inflater.inflate(R.layout.recipe_fragment_body_part,
                container, false);

        recyclerView = rootView.findViewById(R.id.recipe_recycler);
        final RecipeAdapter recipesAdapter = new RecipeAdapter((RecipeActivity) getActivity());
        recyclerView.setAdapter(recipesAdapter);

        if (rootView.getTag() != null && rootView.getTag().equals("phone-land")) {
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), 4);
            recyclerView.setLayoutManager(mLayoutManager);
        } else {
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(mLayoutManager);
        }

        GetRecipe iRecipe = RetroBuilder.Retrieve();
        Call<ArrayList<Recipe>> recipe = iRecipe.getRecipe();

        recipe.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call,
                                   @NonNull Response<ArrayList<Recipe>> response) {
                Integer statusCode = response.code();
                Log.v("status code: ", statusCode.toString());

                ArrayList<Recipe> recipes = response.body();

                Bundle recipesBundle = new Bundle();
                recipesBundle.putParcelableArrayList(ALL_RECIPES, recipes);

                recipesAdapter.setRecipeData(recipes, getContext());
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v("http fail: ", t.getMessage());
            }
        });

        return rootView;
    }
}
