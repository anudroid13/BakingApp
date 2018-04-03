package com.darwinbox.bakingapp.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.darwinbox.bakingapp.interfaces.GetRecipesListener;
import com.darwinbox.bakingapp.models.IngredientsModel;
import com.darwinbox.bakingapp.models.RecipeModel;
import com.darwinbox.bakingapp.models.StepsModel;
import com.darwinbox.bakingapp.utils.NetworkUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class GetRecipes extends AsyncTask<String, Void, String> {

    private final GetRecipesListener listener;

    public GetRecipes(GetRecipesListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {

        String res = "";
        try {
            Log.d("anudroid", strings[0]);
            res = NetworkUtil.getResponseFromHttpUrl(new URL(strings[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    protected void onPostExecute(String s) {

        if (s == null) {
            listener.onFailure();
            return;
        }

        ArrayList<RecipeModel> arrayList = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(s);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject json = jsonArray.optJSONObject(i);

                Gson gson = new Gson();
                RecipeModel recipeModel = gson.fromJson(json.toString(), RecipeModel.class);

                JSONArray arrayIngredients = json.optJSONArray("ingredients");
                ArrayList<IngredientsModel> ingredients = new ArrayList<>();
                for (int j = 0; j < arrayIngredients.length(); j++) {
                    JSONObject object = arrayIngredients.getJSONObject(j);

                    IngredientsModel ingredientsModel = gson.fromJson(object.toString(),
                            IngredientsModel.class);
                    ingredients.add(ingredientsModel);
                }

                JSONArray arraySteps = json.optJSONArray("steps");
                ArrayList<StepsModel> steps = new ArrayList<>();
                for (int j = 0; j < arraySteps.length(); j++) {
                    JSONObject object = arraySteps.getJSONObject(j);

                    StepsModel stepsModel = gson.fromJson(object.toString(),
                            StepsModel.class);
                    steps.add(stepsModel);
                }

                recipeModel.setIngredients(ingredients);
                recipeModel.setSteps(steps);
                arrayList.add(recipeModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFailure();
        }

        if (!arrayList.isEmpty()) {
            listener.onSuccess(arrayList);
        } else {
            listener.onFailure();
        }
    }
}