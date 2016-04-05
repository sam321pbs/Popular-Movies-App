package com.example.sammengistu.popularmoviesapp.databasefavoritemovies;

import com.example.sammengistu.popularmoviesapp.MovieAppConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Helps create and update the FavoriteMovieDatabase
 */
public class FavoriteMovieHelper extends SQLiteOpenHelper{


    private static final String DB_NAME = "favorite.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_DB =
        "CREATE TABLE " + MovieAppConstants.TABLE_FAVORITE_MOVIES +" (" +
            MovieAppConstants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MovieAppConstants.COLUMN_MOVIE_ID + " REAL, " +
            MovieAppConstants.COLUMN_TITLE + " REAL, " +
            MovieAppConstants.COLUMN_SYNOPSIS + " REAL, " +
            MovieAppConstants.COLUMN_RATING + " REAL, " +
            MovieAppConstants.COLUMN_RELEASE_DATE + " REAL, " +
            MovieAppConstants.COLUMN_POSTER + " BLOB)";

    public FavoriteMovieHelper(Context helperContext){
        super(helperContext, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_DB);
    }
}
