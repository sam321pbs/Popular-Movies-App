package com.example.sammengistu.popularmoviesapp.databasefavoritemovies;

import com.example.sammengistu.popularmoviesapp.MovieAppConstants;
import com.example.sammengistu.popularmoviesapp.model.Movie;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Coverts objects to help other classes
 */
public class FavoriteMovieDataBaseConverter {

    /**
     * Converts a movie into a ContentValues Object
     * @param movie - movieId to insert
     */
    public static ContentValues insertFavoriteMovieToDB(Movie movie) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieAppConstants.COLUMN_MOVIE_ID, movie.getMovieID());
            contentValues.put(MovieAppConstants.COLUMN_TITLE, movie.getMovieTitle());
            contentValues.put(MovieAppConstants.COLUMN_SYNOPSIS, movie.getSynopsis());
            contentValues.put(MovieAppConstants.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            contentValues.put(MovieAppConstants.COLUMN_RATING, movie.getRating());
            contentValues.put(MovieAppConstants.COLUMN_POSTER, movie.getPosterImage());

        return contentValues;
    }

    /**
     * Converts a cursor to a list of movie objects
     *
     * @param cursor - where to get all the movies from
     * @return - list of movies
     */
    public static List<Movie> getMoviesFromDb(Cursor cursor) {

        List<Movie> moviesFromDB = new ArrayList<>();

        cursor.moveToFirst();

        //Got from http://stackoverflow.com/questions/10111166/get-all-rows-from-sqlite
        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                String movieId = cursor.getString(cursor
                    .getColumnIndex(MovieAppConstants.COLUMN_MOVIE_ID));

                byte[] moviePosterArray = cursor.getBlob(cursor
                    .getColumnIndex(MovieAppConstants.COLUMN_POSTER));

                String movieTitle = cursor.getString(cursor
                    .getColumnIndex(MovieAppConstants.COLUMN_TITLE));

                String movieRating = cursor.getString(cursor
                    .getColumnIndex(MovieAppConstants.COLUMN_RATING));

                String movieRelease = cursor.getString(cursor
                    .getColumnIndex(MovieAppConstants.COLUMN_RELEASE_DATE));

                String movieSynopsis = cursor.getString(cursor
                    .getColumnIndex(MovieAppConstants.COLUMN_SYNOPSIS));

                Movie movie = new Movie(
                    "", movieTitle, movieSynopsis, movieRating, movieRelease, movieId,
                    MovieAppConstants.FAVORITES);
                movie.setPosterImage(moviePosterArray);

                moviesFromDB.add(movie);
                cursor.moveToNext();
            }
        }
        return moviesFromDB;
    }
}
