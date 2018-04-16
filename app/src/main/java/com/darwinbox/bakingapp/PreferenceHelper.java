package com.darwinbox.bakingapp;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceHelper {

    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_RECIPE_ID_WIDGET = "recipe_id_appwidget_";
    private SharedPreferences mSharedPref;

    public PreferenceHelper() {
        this.mSharedPref = PreferenceManager
                .getDefaultSharedPreferences(AppController.getmContext());
    }

    public void saveTitlePref(int appWidgetId, String text) {
        SharedPreferences.Editor prefs = mSharedPref.edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    public String loadTitlePref(int appWidgetId) {
        String titleValue = mSharedPref.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return "EXAMPLE";
        }
    }

    public void deleteWidgetPrefs (int appWidgetId) {
        SharedPreferences.Editor prefs = mSharedPref.edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.remove(PREF_RECIPE_ID_WIDGET + appWidgetId);
        prefs.apply();
    }

    public void saveRecipeIdForWidget(int appWidgetId, String text) {
        SharedPreferences.Editor prefs = mSharedPref.edit();
        prefs.putString(PREF_RECIPE_ID_WIDGET + appWidgetId, text);
        prefs.apply();
    }

    public String loadRecipeIdForWidget(int appWidgetId) {
        return mSharedPref.getString(PREF_RECIPE_ID_WIDGET + appWidgetId, null);
    }
}
