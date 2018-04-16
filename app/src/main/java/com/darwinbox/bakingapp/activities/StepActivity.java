package com.darwinbox.bakingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.fragments.RecipeStepDetailFragment;
import com.darwinbox.bakingapp.models.Step;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {

    private static final String SELECTED_RECIPE_STEPS = "steps";
    private static final String SELECTED_RECIPE_STEP_POSITION = "steps_position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        ArrayList<Step> arrayListSteps = intent.getParcelableArrayListExtra(SELECTED_RECIPE_STEPS);
        int position = intent.getIntExtra(SELECTED_RECIPE_STEP_POSITION, 0);

        if (savedInstanceState == null) {
            final RecipeStepDetailFragment fragment = new RecipeStepDetailFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragment.setSteps(arrayListSteps);
            fragment.setPosition(position);
            fragmentManager.beginTransaction()
                    .add(R.id.step_fragment_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            StepActivity.this.finish();
        }
        return true;
    }
}
