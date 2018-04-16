package com.darwinbox.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.fragments.RecipeDetailFragment;
import com.darwinbox.bakingapp.fragments.RecipeStepDetailFragment;
import com.darwinbox.bakingapp.models.Recipe;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.StepSelectedListener {

    static String STACK_RECIPE_STEP_DETAIL = "STACK_RECIPE_STEP_DETAIL";
    private static final String SELECTED_RECIPE_STEPS = "steps";
    private static final String SELECTED_RECIPE_STEP_POSITION = "steps_position";
    public String recipeName;
    public boolean isTwoPane;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        isTwoPane = findViewById(R.id.step_fragment_container) != null;

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStepSelected(Recipe recipe , int position) {

        if (isTwoPane) {
            final RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragment.setSteps(recipe.getSteps());
            fragment.setPosition(position);
            fragmentManager.beginTransaction()
                    .replace(R.id.step_fragment_container, fragment)
                    .addToBackStack(STACK_RECIPE_STEP_DETAIL)
                    .commit();
        } else {
            Intent launchStepDetailActivityIntent =
                    new Intent(this, StepActivity.class);
            launchStepDetailActivityIntent.putParcelableArrayListExtra(SELECTED_RECIPE_STEPS, recipe.getSteps());
            launchStepDetailActivityIntent.putExtra(SELECTED_RECIPE_STEP_POSITION, position);
            startActivity(launchStepDetailActivityIntent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            RecipeDetailActivity.this.finish();
        }
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if (fragment != null && fragment instanceof RecipeDetailFragment) {
            ((RecipeDetailFragment) fragment).onNewIntent(intent);
        }
    }

    @Override
    public void setActionBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
