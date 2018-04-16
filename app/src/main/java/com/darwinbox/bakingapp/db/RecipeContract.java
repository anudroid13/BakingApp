package com.darwinbox.bakingapp.db;

import android.provider.BaseColumns;

public class RecipeContract {

    public RecipeContract() {
    }

    static class RecipeEntry implements BaseColumns {

        static final String TABLE_NAME = "recipes";

        static final String COLUMN_RECIPE_ID = "recipe_id";
        static final String COLUMN_NAME = "name";
        static final String COLUMN_SERVINGS = "servings";
        static final String COLUMN_IMAGE = "image";
    }

    static final String SQL_QUERY_CREATE =
            "CREATE TABLE " + RecipeEntry.TABLE_NAME + " ("
                    + RecipeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + RecipeEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL UNIQUE,"
                    + RecipeEntry.COLUMN_NAME + " TEXT NOT NULL,"
                    + RecipeEntry.COLUMN_SERVINGS + " INTEGER NOT NULL,"
                    + RecipeEntry.COLUMN_IMAGE + " TEXT NOT NULL"
                    + ")";
}
