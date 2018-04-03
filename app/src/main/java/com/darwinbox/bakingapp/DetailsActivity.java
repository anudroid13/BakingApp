package com.darwinbox.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.darwinbox.bakingapp.adapters.IngredientsAdapter;
import com.darwinbox.bakingapp.adapters.RecipeAdapter;
import com.darwinbox.bakingapp.adapters.StepsAdapter;
import com.darwinbox.bakingapp.interfaces.OnItemClickListener;
import com.darwinbox.bakingapp.models.IngredientsModel;
import com.darwinbox.bakingapp.models.RecipeModel;
import com.darwinbox.bakingapp.models.StepsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview_ingredients)
    RecyclerView recyclerViewIngredients;
    @BindView(R.id.recyclerview_steps)
    RecyclerView recyclerViewSteps;
    private IngredientsAdapter ingredientsAdapter;
    private StepsAdapter stepsAdapter;
    private ArrayList<StepsModel> steps;
    private final OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemSelected(int position) {
            StepsModel model = steps.get(position);
            Intent intent = new Intent(DetailsActivity.this, MediaActivity.class);
            intent.putExtra("model", model);
            startActivity(intent);

//            Intent intent = new Intent(DetailsActivity.this,
//                    DetailsActivity.class);
//            intent.putParcelableArrayListExtra("ingredients", model.getIngredients());
//            intent.putParcelableArrayListExtra("steps", model.getSteps());
//            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(DetailsActivity.this);
        init();

        setData();
    }

    private void setData() {
        Intent intent = getIntent();
        if (intent != null) {
            ArrayList<IngredientsModel> ingredientsModels =
                    intent.getParcelableArrayListExtra("ingredients");
            ingredientsAdapter.setArrayList(ingredientsModels);

            steps = intent.getParcelableArrayListExtra("steps");
            stepsAdapter.setArrayList(steps);
        }
    }

    private void init() {
        LinearLayoutManager layoutManager = new
                LinearLayoutManager(DetailsActivity.this);
        recyclerViewIngredients.setLayoutManager(layoutManager);
        ingredientsAdapter = new IngredientsAdapter();
        recyclerViewIngredients.setAdapter(ingredientsAdapter);

        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(DetailsActivity.this);
        recyclerViewSteps.setLayoutManager(linearLayoutManager);
        stepsAdapter = new StepsAdapter(listener);
        recyclerViewSteps.setAdapter(stepsAdapter);
    }
}
