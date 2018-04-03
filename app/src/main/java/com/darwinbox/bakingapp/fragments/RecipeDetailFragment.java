package com.darwinbox.bakingapp.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.activities.RecipeDetailActivity;
import com.darwinbox.bakingapp.adapters.RecipeDetailAdapter;
import com.darwinbox.bakingapp.models.Ingredient;
import com.darwinbox.bakingapp.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import static com.darwinbox.bakingapp.activities.RecipeActivity.SELECTED_RECIPES;

public class RecipeDetailFragment extends Fragment {

    ArrayList<Recipe> recipe;
    String recipeName;

    public RecipeDetailFragment() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        RecyclerView recyclerView;
        TextView textView;

        Log.d("anudroid", "detailFragment");
        recipe = new ArrayList<>();

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelableArrayList(SELECTED_RECIPES);
        } else {
            recipe = getArguments().getParcelableArrayList(SELECTED_RECIPES);
        }

//        List<Ingredient> ingredients = recipe.get(0).getIngredients();
        recipeName = recipe.get(0).getName();

        View rootView = inflater.inflate(R.layout.recipe_detail_fragment_body_part,
                container, false);
        textView = rootView.findViewById(R.id.recipe_detail_text);

        textView.setText(recipeName);

        recyclerView = rootView.findViewById(R.id.recipe_detail_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        RecipeDetailAdapter mRecipeDetailAdapter = new RecipeDetailAdapter((RecipeDetailActivity) getActivity());
        recyclerView.setAdapter(mRecipeDetailAdapter);
        mRecipeDetailAdapter.setMasterRecipeData(recipe, getContext());

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelableArrayList(SELECTED_RECIPES, recipe);
        currentState.putString("Title", recipeName);
    }
}
