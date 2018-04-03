package com.darwinbox.bakingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.darwinbox.bakingapp.adapters.RecipeAdapter;
import com.darwinbox.bakingapp.asynctasks.GetRecipes;
import com.darwinbox.bakingapp.interfaces.GetRecipesListener;
import com.darwinbox.bakingapp.interfaces.OnItemClickListener;
import com.darwinbox.bakingapp.models.RecipeModel;
import com.darwinbox.bakingapp.utils.NetworkUtil;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements GetRecipesListener {

    @BindView(R.id.item_list)
    RecyclerView recyclerView;
    private ProgressDialog pDialog;
    private RecipeAdapter adapter;
    private ArrayList<RecipeModel> recipeModels;
    private final String RECIPE_LIST = "movie_list";
    private final OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemSelected(int position) {
            RecipeModel model = recipeModels.get(position);

            Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
            intent.putParcelableArrayListExtra("ingredients", model.getIngredients());
            intent.putParcelableArrayListExtra("steps", model.getSteps());
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(MainActivity.this);
        init();

        if (savedInstanceState != null) {
            recipeModels = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
            if (recipeModels != null && !recipeModels.isEmpty()) {
                adapter.setArrayList(recipeModels);
            }
        } else {
            getData();
        }
    }

    private void init() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage(getString(R.string.wait));
        pDialog.setCancelable(false);

        int gridColumnCount = getResources().getInteger(R.integer.grid_column_count);

        recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this,
                gridColumnCount));
        adapter = new RecipeAdapter(listener);
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        showpDialog();
        URL url = NetworkUtil.buildUrl();

        if (url != null) {
            if (NetworkUtil.isNetworkAvailable(MainActivity.this)) {
                try {
                    GetRecipes task = new GetRecipes(MainActivity.this);
                    task.execute(String.valueOf(url));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onSuccess(ArrayList<RecipeModel> arrayList) {

        recipeModels = arrayList;
        adapter.setArrayList(arrayList);
        hidepDialog();
    }

    @Override
    public void onFailure() {
        hidepDialog();
        Toast.makeText(this, R.string.server_error, Toast.LENGTH_SHORT).show();
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        hidepDialog();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (null != recipeModels && !recipeModels.isEmpty()) {
            outState.putParcelableArrayList(RECIPE_LIST, recipeModels);
        }
    }
}
