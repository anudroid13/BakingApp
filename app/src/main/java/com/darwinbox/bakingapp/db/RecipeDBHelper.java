package com.darwinbox.bakingapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.darwinbox.bakingapp.db.IngredientContract.IngredientEntry;
import com.darwinbox.bakingapp.db.RecipeContract.RecipeEntry;
import com.darwinbox.bakingapp.db.StepContract.StepEntry;
import com.darwinbox.bakingapp.models.Ingredient;
import com.darwinbox.bakingapp.models.Recipe;
import com.darwinbox.bakingapp.models.Step;

import java.util.ArrayList;
import java.util.List;

public class RecipeDBHelper {

    private Context mContext;

    public RecipeDBHelper(Context mContext) {
        this.mContext = mContext;
    }

    public long insertRecipeNames (Recipe recipe) {

        SQLiteHelper sqLiteHelper = new SQLiteHelper(mContext);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(RecipeEntry.COLUMN_RECIPE_ID, recipe.getId());
        cv.put(RecipeEntry.COLUMN_NAME, recipe.getName());
        cv.put(RecipeEntry.COLUMN_SERVINGS, recipe.getServings());
        cv.put(RecipeEntry.COLUMN_IMAGE, recipe.getImage());

        try {
            return db.insert(RecipeEntry.TABLE_NAME, null,  cv);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long insertIngredients (Ingredient ingredient, int recipeId) {

        SQLiteHelper sqLiteHelper = new SQLiteHelper(mContext);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(IngredientEntry.COLUMN_RECIPE_ID, recipeId);
        cv.put(IngredientEntry.COLUMN_INGREDIENT, ingredient.getIngredient());
        cv.put(IngredientEntry.COLUMN_QUANTITY, ingredient.getQuantity());
        cv.put(IngredientEntry.COLUMN_MEASURE, ingredient.getMeasure());

        try {
            return db.insert(IngredientEntry.TABLE_NAME, null,  cv);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long insertSteps (Step step, int recipeId) {

        SQLiteHelper sqLiteHelper = new SQLiteHelper(mContext);
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(StepEntry.COLUMN_RECIPE_ID, recipeId);
        cv.put(StepEntry.COLUMN_STEP_ID, step.getId());
        cv.put(StepEntry.COLUMN_DESC, step.getDescription());
        cv.put(StepEntry.COLUMN_SHORT_DESC, step.getShortDescription());
        cv.put(StepEntry.COLUMN_VIDEO_URL, step.getVideoUrl());
        cv.put(StepEntry.COLUMN_THUMB_URL, step.getThumbnailUrl());

        try {
            return db.insert(StepEntry.TABLE_NAME, null,  cv);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Recipe> getRecipes () {

        SQLiteHelper helper = new SQLiteHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();

        List<Recipe> recipes = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = db.query(RecipeEntry.TABLE_NAME, null, null,
                    null, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Recipe recipe = new Recipe();
                    recipe.setName(cursor.getString(cursor.getColumnIndex
                            (RecipeEntry.COLUMN_NAME)));
                    recipe.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex
                            (RecipeEntry.COLUMN_RECIPE_ID))));
                    recipe.setImage(cursor.getString(cursor.getColumnIndex
                            (RecipeEntry.COLUMN_IMAGE)));
                    recipe.setServings(Integer.parseInt(cursor.getString(cursor.getColumnIndex
                            (RecipeEntry.COLUMN_SERVINGS))));

                    recipes.add(recipe);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }
        return recipes;
    }

    public List<Ingredient> getIngredients (int recipeId) {
        SQLiteHelper helper = new SQLiteHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();

        List<Ingredient> ingredients = new ArrayList<>();

        Cursor cursor = null;
        try {
            String whereClause = IngredientEntry.COLUMN_RECIPE_ID + "= ?";
            String[] whereArgs = new String[]{String.valueOf(recipeId)};

            cursor = db.query(IngredientEntry.TABLE_NAME, null, whereClause,
                    whereArgs, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Ingredient ingredient = new Ingredient();
                    ingredient.setIngredient(cursor.getString(cursor.getColumnIndex
                            (IngredientEntry.COLUMN_INGREDIENT)));
                    ingredient.setQuantity(Double.parseDouble(cursor.getString(cursor.getColumnIndex
                            (IngredientEntry.COLUMN_QUANTITY))));
                    ingredient.setMeasure(cursor.getString(cursor.getColumnIndex
                            (IngredientEntry.COLUMN_MEASURE)));

                    ingredients.add(ingredient);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }
        return ingredients;
    }

    private List<Step> getSteps(int recipeId) {
        SQLiteHelper helper = new SQLiteHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();

        List<Step> steps = new ArrayList<>();

        Cursor cursor = null;
        try {
            String whereClause = StepEntry.COLUMN_RECIPE_ID + "= ?";
            String[] whereArgs = new String[]{String.valueOf(recipeId)};

            cursor = db.query(StepEntry.TABLE_NAME, null, whereClause,
                    whereArgs, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    Step step = new Step();
                    step.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex
                            (StepEntry.COLUMN_RECIPE_ID))));
                    step.setDescription(cursor.getString(cursor.getColumnIndex
                            (StepEntry.COLUMN_DESC)));
                    step.setShortDescription(cursor.getString(cursor.getColumnIndex
                            (StepEntry.COLUMN_SHORT_DESC)));
                    step.setVideoUrl(cursor.getString(cursor.getColumnIndex
                            (StepEntry.COLUMN_VIDEO_URL)));
                    step.setThumbnailUrl(cursor.getString(cursor.getColumnIndex
                            (StepEntry.COLUMN_THUMB_URL)));
                    steps.add(step);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }
        return steps;
    }

    public Recipe getRecipe (int recipeId) {

        SQLiteHelper helper = new SQLiteHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();

        Recipe recipe = null;
        Cursor cursor = null;
        try {
            String whereClause = RecipeEntry.COLUMN_RECIPE_ID + "= ?";
            String[] whereArgs = new String[]{String.valueOf(recipeId)};

            cursor = db.query(RecipeEntry.TABLE_NAME, null, whereClause,
                    whereArgs, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToNext();
                recipe = new Recipe();
                recipe.setName(cursor.getString(cursor.getColumnIndex
                        (RecipeEntry.COLUMN_NAME)));
                recipe.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex
                        (RecipeEntry.COLUMN_RECIPE_ID))));
                recipe.setImage(cursor.getString(cursor.getColumnIndex
                        (RecipeEntry.COLUMN_IMAGE)));
                recipe.setServings(Integer.parseInt(cursor.getString(cursor.getColumnIndex
                        (RecipeEntry.COLUMN_SERVINGS))));
                recipe.setIngredients((ArrayList<Ingredient>) getIngredients(recipeId));
                recipe.setSteps((ArrayList<Step>) getSteps(recipeId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return recipe;
    }

    public String getRecipeName (int recipeId) {

        SQLiteHelper helper = new SQLiteHelper(mContext);
        SQLiteDatabase db = helper.getWritableDatabase();

        String recipeName = null;
        Cursor cursor = null;
        try {
            String[] columns = {RecipeEntry.COLUMN_NAME};
            String whereClause = RecipeEntry.COLUMN_RECIPE_ID + "= ?";
            String[] whereArgs = new String[]{String.valueOf(recipeId)};

            cursor = db.query(RecipeEntry.TABLE_NAME, columns, whereClause,
                    whereArgs, null, null, null);

            if (cursor != null && cursor.getCount() > 0) {
                recipeName = cursor.getString(cursor.getColumnIndex
                        (RecipeEntry.COLUMN_NAME));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
                db.close();
            }
        }

        return recipeName;
    }
}
