package com.darwinbox.bakingapp.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.adapters.RecipeDetailAdapter;
import com.darwinbox.bakingapp.fragments.RecipeDetailFragment;
import com.darwinbox.bakingapp.fragments.RecipeStepDetailFragment;
import com.darwinbox.bakingapp.models.Recipe;
import com.darwinbox.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

import static com.darwinbox.bakingapp.activities.RecipeActivity.SELECTED_INDEX;
import static com.darwinbox.bakingapp.activities.RecipeActivity.SELECTED_RECIPES;
import static com.darwinbox.bakingapp.activities.RecipeActivity.SELECTED_STEPS;

public class RecipeDetailActivity extends AppCompatActivity implements
        RecipeDetailAdapter.ListItemClickListener,
        RecipeStepDetailFragment.ListItemClickListener {

    private ArrayList<Recipe> recipe;
    static String STACK_RECIPE_DETAIL = "STACK_RECIPE_DETAIL";
    static String STACK_RECIPE_STEP_DETAIL = "STACK_RECIPE_STEP_DETAIL";
    public String recipeName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        if (savedInstanceState == null) {

            Bundle selectedRecipeBundle = getIntent().getExtras();

            recipe = new ArrayList<>();
            recipe = selectedRecipeBundle.getParcelableArrayList(SELECTED_RECIPES);
            recipeName = recipe.get(0).getName();

            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(selectedRecipeBundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(STACK_RECIPE_DETAIL)
                    .commit();

            if (findViewById(R.id.recipe_linear_layout).getTag() != null
                    && findViewById(R.id.recipe_linear_layout).getTag()
                    .equals("tablet-land")) {

                final RecipeStepDetailFragment fragment2 = new RecipeStepDetailFragment();
                fragment2.setArguments(selectedRecipeBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container2, fragment2)
                        .addToBackStack(STACK_RECIPE_STEP_DETAIL)
                        .commit();
            }
        } else {
            recipeName = savedInstanceState.getString("Title");
        }

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(recipeName);

        Log.d("anudroid", "second activity");
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                if (findViewById(R.id.fragment_container2) == null) {
                    if (fm.getBackStackEntryCount() > 1) {
                        //go back to "Recipe Detail" screen
                        fm.popBackStack(STACK_RECIPE_DETAIL, 0);
                    } else if (fm.getBackStackEntryCount() > 0) {
                        //go back to "Recipe" screen
                        finish();
                    }
                } else {
                    //go back to "Recipe" screen
                    finish();
                }
            }
        });
    }

    @Override
    public void onListItemClick(List<Step> stepsOut, int clickedItemIndex,
                                String recipeName) {
        final RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setTitle(recipeName);

        Bundle stepBundle = new Bundle();
        stepBundle.putParcelableArrayList(SELECTED_STEPS, (ArrayList<Step>) stepsOut);
        stepBundle.putInt(SELECTED_INDEX, clickedItemIndex);
        stepBundle.putString("Title", recipeName);
        fragment.setArguments(stepBundle);

        if (findViewById(R.id.recipe_linear_layout).getTag() != null && findViewById(R.id.recipe_linear_layout).getTag().equals("tablet-land")) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container2, fragment)
                    .addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Title", recipeName);
    }
}
