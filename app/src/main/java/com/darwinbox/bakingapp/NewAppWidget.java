package com.darwinbox.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.darwinbox.bakingapp.activities.RecipeDetailActivity;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link NewAppWidgetConfigureActivity NewAppWidgetConfigureActivity}
 */
public class NewAppWidget extends AppWidgetProvider {

    private static final String RECIPE_ID = "selected_recipe_widget";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        PreferenceHelper helper = new PreferenceHelper();

        String recipeString = helper.loadRecipeIdForWidget(appWidgetId);
        if (recipeString == null) {
            return;
        }
        String recipeId = null;
        String recipeName = null;
        if (recipeString != null) {
            String[] values = recipeString.split(":");
            if (values.length == 2) {
                recipeId = values[0];
                recipeName = values[1];
            }
        }
        // Construct the RemoteViews object
        CharSequence widgetText = recipeName + "\n" + helper.loadTitlePref(appWidgetId);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);
        Log.d("anudroid", "" + recipeId);
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(RECIPE_ID, Integer.parseInt(recipeId));
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        PreferenceHelper helper = new PreferenceHelper();
        for (int appWidgetId : appWidgetIds) {
            helper.deleteWidgetPrefs(appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

