package com.darwinbox.bakingapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.darwinbox.bakingapp.R;
import com.darwinbox.bakingapp.adapters.RecipeDetailAdapter;
import com.darwinbox.bakingapp.adapters.RecipeIngredientAdapter;
import com.darwinbox.bakingapp.db.RecipeDBHelper;
import com.darwinbox.bakingapp.models.Recipe;


public class RecipeDetailFragment extends Fragment implements RecipeDetailAdapter.ListItemClickListener {

    private static final String SELECTED_RECIPE_KEY = "selected_recipe_key";
    private Recipe recipe;
    private String recipeName;
    private RecipeIngredientAdapter mRecipeIngredientAdapter;
    private RecipeDetailAdapter mRecipeDetailAdapter;
    private StepSelectedListener mStepSelectedListener;
    private static final String RECIPE_ID = "selected_recipe_widget";

    public RecipeDetailFragment() {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recipe_detail_fragment_body_part,
                container, false);

        RecyclerView recyclerViewIngredient = rootView.findViewById(R.id.recipe_ingredient_recycler);
        RecyclerView recyclerView = rootView.findViewById(R.id.recipe_detail_recycler);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        mRecipeDetailAdapter = new RecipeDetailAdapter(this);
        recyclerView.setAdapter(mRecipeDetailAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerViewIngredient.setLayoutManager(manager);
        mRecipeIngredientAdapter = new RecipeIngredientAdapter();
        recyclerViewIngredient.setAdapter(mRecipeIngredientAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(SELECTED_RECIPE_KEY);
            setRecipe();
            return;
        }
        setRecipeDetails(getActivity().getIntent());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putParcelable(SELECTED_RECIPE_KEY, recipe);
        currentState.putString("Title", recipeName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!(context instanceof StepSelectedListener)){
            throw new IllegalArgumentException("Activity must implements StepSelectedListener ");
        }
        mStepSelectedListener = (StepSelectedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mStepSelectedListener = null;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        mStepSelectedListener.onStepSelected(recipe , clickedItemIndex);
    }

    public interface StepSelectedListener {
        void onStepSelected(Recipe recipe ,int position);
        void setActionBarTitle(String recipe);
    }

    public void onNewIntent(Intent intent) {
        setRecipeDetails(intent);
    }

    private void setRecipeDetails(Intent intent) {
        recipe = intent.getParcelableExtra(SELECTED_RECIPE_KEY);

        if (recipe == null) {
            RecipeDBHelper helper = new RecipeDBHelper(getActivity());
            recipe = helper.getRecipe(intent.getIntExtra(RECIPE_ID, 0));
        }
        setRecipe();
    }

    private void setRecipe() {
        if (recipe != null) {
            recipeName = recipe.getName();
            mRecipeDetailAdapter.setMasterRecipeData(recipe.getSteps());
            mRecipeIngredientAdapter.setMasterRecipeData(recipe.getIngredients());
            mStepSelectedListener.setActionBarTitle(recipeName);
        }
    }
}
