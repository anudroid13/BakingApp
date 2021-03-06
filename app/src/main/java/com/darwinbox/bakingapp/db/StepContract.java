package com.darwinbox.bakingapp.db;

import android.provider.BaseColumns;

public class StepContract {

    public StepContract() {
    }

    static class StepEntry implements BaseColumns {

        static final String TABLE_NAME = "steps";

        static final String COLUMN_RECIPE_ID = "recipe_id";
        static final String COLUMN_STEP_ID = "step_id";
        static final String COLUMN_SHORT_DESC = "short_desc";
        static final String COLUMN_DESC = "desc";
        static final String COLUMN_VIDEO_URL = "video";
        static final String COLUMN_THUMB_URL = "thumb";
    }

    static final String SQL_QUERY_CREATE =
            "CREATE TABLE " + StepEntry.TABLE_NAME + " ("
                    + StepEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + StepEntry.COLUMN_RECIPE_ID + " INTEGER NOT NULL,"
                    + StepEntry.COLUMN_STEP_ID + " INTEGER NOT NULL,"
                    + StepEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL,"
                    + StepEntry.COLUMN_DESC + " TEXT NOT NULL,"
                    + StepEntry.COLUMN_VIDEO_URL + " TEXT NOT NULL,"
                    + StepEntry.COLUMN_THUMB_URL + " TEXT NOT NULL"
                    + ")";
}
