package com.darwinbox.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.adapters.RecipeAdapter;
import com.darwinbox.bakingapp.models.Recipe;

import java.util.ArrayList;

public class RecipeActivity extends AppCompatActivity implements
        RecipeAdapter.ListItemClickListener {

    public static String ALL_RECIPES = "All_Recipes";
    public static String SELECTED_RECIPES = "Selected_Recipes";
    public static String SELECTED_STEPS = "Selected_Steps";
    public static String SELECTED_INDEX = "Selected_Index";

    @Override
    public void onListItemClick(Recipe clickedItemIndex) {

        Bundle selectedRecipeBundle = new Bundle();
        ArrayList<Recipe> selectedRecipe = new ArrayList<>();
        selectedRecipe.add(clickedItemIndex);
        selectedRecipeBundle.putParcelableArrayList(SELECTED_RECIPES, selectedRecipe);

        final Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtras(selectedRecipeBundle);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe);
    }
}
