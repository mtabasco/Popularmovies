/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.FavoritesContract.MovieEntry;


public class FavsDbHelper extends SQLiteOpenHelper {

    // The name of the database
    private static final String DATABASE_NAME = "favoritesDB.db";

    // If you change the database schema, you must increment the database version
    private static final int VERSION = 2;


    // Constructor
    FavsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }


    /**
     * Called when the tasks database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create tasks table (careful to follow SQL formatting rules)
        final String CREATE_TABLE = "CREATE TABLE "  + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry._ID                + " INTEGER PRIMARY KEY, " +
                        MovieEntry.COLUMN_ID_TMDB + " INTEGER NOT NULL, " +
                        MovieEntry.COLUMN_TITLE    + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_OVERVIEW + " TEXT NULL, " +
                        MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT NULL, " +
                        MovieEntry.COLUMN_POSTER_PATH + " TEXT NULL);";

        db.execSQL(CREATE_TABLE);
    }


    /**
     * This method discards the old table of data and calls onCreate to recreate a new one.
     * This only occurs when the version number for this database (DATABASE_VERSION) is incremented.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }


}
