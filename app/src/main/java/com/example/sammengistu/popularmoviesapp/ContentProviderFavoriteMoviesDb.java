package com.example.sammengistu.popularmoviesapp;

import com.example.sammengistu.popularmoviesapp.databasefavoritemovies.FavoriteMovieHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

/**
 * Helps with interacting with the database
 */
public class ContentProviderFavoriteMoviesDb extends ContentProvider {
    private FavoriteMovieHelper dbHelper;
    //specific for our our app, will be specified in manifest
    public static final String FAVORITE_MOVIE_AUTHORITY = "favoriteMovieProviderAuthorities";
    public static final Uri CONTENT_URI = Uri.parse("content://" + FAVORITE_MOVIE_AUTHORITY);

    @Override
    public boolean onCreate() {
        dbHelper = new FavoriteMovieHelper(getContext());
        return true;
    }

    @Override
    public int delete(Uri uri, String where, String[] args) {

        String table = getTableName(uri);
        SQLiteDatabase dataBase = dbHelper.getWritableDatabase();
        return dataBase.delete(table, where, args);
    }

    @Override
    public String getType(Uri arg0) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long value = database.insert(table, null, initialValues);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(value));
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor query = database.rawQuery(
            MovieAppConstants.SELECT_FROM + table, null);

        return query;
    }

    @Override
    public int update(Uri uri, ContentValues values, String whereClause,
                      String[] whereArgs) {
        String table = getTableName(uri);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.update(table, values, whereClause, whereArgs);
    }

    public static String getTableName(Uri uri) {
        String value = uri.getPath();
        value = value.replace("/", "");//we need to remove '/'
        return value;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        Bundle args = new Bundle();

        if (method.equals(MovieAppConstants.CONTENT_PROVIDER_DOES_MOVIE_EXIST_METHOD)) {
            SQLiteDatabase database = dbHelper.getReadableDatabase();
            args.putBoolean(MovieAppConstants.DOES_EXIST, doesMovieIdExistInDb(database, arg));
            return args;
        }
        return null;
    }

    public boolean doesMovieIdExistInDb(SQLiteDatabase database, String arg) {

        return DatabaseUtils.longForQuery(database,
            MovieAppConstants.SELECT_COUNT_FROM +
                MovieAppConstants.TABLE_FAVORITE_MOVIES +
                MovieAppConstants.QUERY_WHERE +
                MovieAppConstants.COLUMN_MOVIE_ID +
                MovieAppConstants.QUERY_LIMIT,
            new String[]{arg}) > 0;
    }
}