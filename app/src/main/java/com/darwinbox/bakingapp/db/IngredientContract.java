package com.darwinbox.bakingapp.db;

import android.provider.BaseColumns;

public class IngredientContract {

    public IngredientContract() {
    }

    static class IngredientEntry implements BaseColumns {

        static final String TABLE_NAME = "ingredients";

        static final String COLUMN_RECIPE_ID = "recipe_id";
        static final String COLUMN_QUANTITY = "quantity";
        static final String COLUMN_MEASURE = "measure";
        static final String COLUMN_INGREDIENT = "ingredient";
    }

    static final String SQL_QUERY_CREATE =
            "CREATE TABLE " + IngredientEntry.TABLE_NAME + " ("
                    + IngredientEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + IngredientEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL,"
                    + IngredientEntry.COLUMN_QUANTITY + " REAL NOT NULL,"
                    + IngredientEntry.COLUMN_MEASURE + " TEXT NOT NULL,"
                    + IngredientEntry.COLUMN_INGREDIENT + " TEXT NOT NULL"
                    + ")";
}
