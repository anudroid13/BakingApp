package com.darwinbox.bakingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.RecipesIdlingResource;
import com.darwinbox.bakingapp.activities.RecipeDetailActivity;
import com.darwinbox.bakingapp.adapters.RecipeAdapter;
import com.darwinbox.bakingapp.db.RecipeDBHelper;
import com.darwinbox.bakingapp.models.Recipe;
import com.darwinbox.bakingapp.retrofit.GetRecipe;
import com.darwinbox.bakingapp.retrofit.RetroBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeFragment extends Fragment implements RecipeAdapter.ListItemClickListener{

    private static final String SELECTED_RECIPE_KEY = "selected_recipe_key";
    private static final String RECIPE_LIST_KEY = "recipe_list_key";
    private ArrayList<Recipe> mRecipes;
    private RecipeAdapter recipesAdapter;

    public RecipeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipe_fragment_body_part,
                container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recipe_recycler);
        recipesAdapter = new RecipeAdapter(this);
        recyclerView.setAdapter(recipesAdapter);
        int spanCount = 1;
        if (getResources().getBoolean(R.bool.is_tablet)) {
            spanCount = 2;
        }
        GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), spanCount);
        recyclerView.setLayoutManager(mLayoutManager);

        return rootView;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

        final Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
        intent.putExtra(SELECTED_RECIPE_KEY ,mRecipes.get(clickedItemIndex));
        startActivity(intent);
    }

    RecipesIdlingResource recipesIdlingResource;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recipesIdlingResource =  new RecipesIdlingResource();
        if(savedInstanceState != null){
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPE_LIST_KEY);
        }
        if(mRecipes!= null){
            recipesAdapter.setRecipeData(mRecipes);
        }else{
            loadRecipe();
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mRecipes!= null){
            //save the recipes to out bundle
            outState.putParcelableArrayList(RECIPE_LIST_KEY , mRecipes);
        }
    }

    private void loadRecipe(){
        recipesIdlingResource.setIdleState(false);
        GetRecipe iRecipe = RetroBuilder.Retrieve();
        Call<ArrayList<Recipe>> recipe = iRecipe.getRecipe();
        recipe.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call,
                                   @NonNull Response<ArrayList<Recipe>> response) {
                recipesIdlingResource.setIdleState(true);
                mRecipes = response.body();
                recipesAdapter.setRecipeData(mRecipes);

                RecipeDBHelper dbHelper = new RecipeDBHelper(getActivity());
                for (int i = 0; i < mRecipes.size(); i++) {
                    long retValue1 = dbHelper.insertRecipeNames(mRecipes.get(i));

                    //recipe already exists hence continue
                    if(retValue1 < 1){
                        continue;
                    }

                    Recipe recipe = mRecipes.get(i);
                    for (int j = 0; j < mRecipes.get(i).getIngredients().size(); j++) {

                        long retValue = dbHelper.insertIngredients(recipe.getIngredients()
                                .get(j), recipe.getId());
                    }

                    for (int j = 0; j < mRecipes.get(i).getSteps().size(); j++) {
                        long retValue = dbHelper.insertSteps(recipe.getSteps().get(j),
                                recipe.getId());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                recipesIdlingResource.setIdleState(true);
                Log.v("http fail: ", t.getMessage());
                Toast.makeText(getContext() , R.string.loading_error_messages , Toast.LENGTH_SHORT).show();
            }
        });

    }
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (recipesIdlingResource == null) {
            recipesIdlingResource = new RecipesIdlingResource();
        }
        return recipesIdlingResource;
    }
}
