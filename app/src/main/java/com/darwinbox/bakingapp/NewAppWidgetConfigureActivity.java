package com.darwinbox.bakingapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.darwinbox.bakingapp.db.RecipeDBHelper;
import com.darwinbox.bakingapp.models.Ingredient;
import com.darwinbox.bakingapp.models.Recipe;

import java.util.List;

/**
 * The configuration screen for the {@link NewAppWidget NewAppWidget} AppWidget.
 */
public class NewAppWidgetConfigureActivity extends Activity {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private int recipeId;
    private String recipeName;
    private String ingredientContent = "";
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = NewAppWidgetConfigureActivity.this;

            Log.d("anudroid", "clicked");
            RecipeDBHelper dbHelper = new RecipeDBHelper(NewAppWidgetConfigureActivity.this);

            Log.d("anudroid", "clicked" + recipeId);
            List<Ingredient> ingredients = dbHelper.getIngredients(recipeId);
            if (ingredients != null) {
                for (int i = 0; i < ingredients.size(); i++) {
                    Ingredient ingredient = ingredients.get(i);
                    ingredientContent = ingredientContent.concat(" "
                            + ingredient.getIngredient()
                            + "("
                            + ingredient.getQuantity()
                            + ingredient.getMeasure()
                            + ")"
                            + "\n");
                }
            }

            Log.d("anudroid", "clicked" + ingredientContent);
            
            // When the button is clicked, store the string locally
//            saveTitlePref(context, mAppWidgetId, ingredientContent);

            // It is the responsibility of the configuration activity to update the app widget
            PreferenceHelper helper = new PreferenceHelper();
            helper.saveTitlePref(mAppWidgetId, ingredientContent);
            helper.saveRecipeIdForWidget(mAppWidgetId, recipeId
                    + ":" + recipeName);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            NewAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public NewAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget


    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.new_app_widget_configure);
        RadioGroup mRadioGroup = findViewById(R.id.radio_group);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        RecipeDBHelper dbHelper = new RecipeDBHelper(NewAppWidgetConfigureActivity.this);
        final List<Recipe> recipes = dbHelper.getRecipes();
        Log.d("anudroid" ,"count "+ recipes.size());
        if (recipes != null) {
            mRadioGroup.removeAllViews();
            for (int i = 0; i <recipes.size(); i++) {
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(recipes.get(i).getName());
                radioButton.setId(i);
                mRadioGroup.addView(radioButton);
            }
        }

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (recipes != null) {
                    Log.d("anudroid",i+" index clicked");
                    recipeId = recipes.get(i).getId();
                    recipeName = recipes.get(i).getName();
                }
            }
        });
    }
}

