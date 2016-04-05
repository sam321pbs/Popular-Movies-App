package com.example.sammengistu.popularmoviesapp;

import com.example.sammengistu.popularmoviesapp.databasefavoritemovies.FavoriteMovieDataBaseConverter;
import com.example.sammengistu.popularmoviesapp.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This class gets the movie info from the web
 */
public class MovieInfoGetter {

    private final String BASE_URL_FOR_POSTER =
        "http://image.tmdb.org/t/p/";

    private final String IMAGE_SIZE = "w185/";

    //Info from json
    private final String RESULT = "results";
    private final String POSTER_PATH = "poster_path";
    private final String MOVIE_TITLE = "original_title";
    private final String MOVIE_SYNOPSIS = "overview";
    private final String MOVIE_RATING = "vote_average";
    private final String MOVIE_RELEASE_DATE = "release_date";
    private final String MOVIE_ID = "id";
    private final String MOVIE_YOUTUBE_ID = "key";
    private final String REVIEW_AUTHOR = "author";
    private final String REVIEW_CONTENT = "content";

    /**
     * Uses the OKHTTP Library to get the json object from the web
     *
     * @param urlForTypeOfMovies - the destination to get the web info from
     * @return - json object
     */
    private String getJsonFromTMDB(String urlForTypeOfMovies) {
        String jsonFromTMDB = "";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
            .url(urlForTypeOfMovies)
            .build();
        try {

            Response response = client.newCall(request).execute();
            jsonFromTMDB = response.body().string();
        } catch (IOException e) {

        }
        return jsonFromTMDB;
    }

    /**
     * Coverts the JSON object into a movie object with the information it gets from the web
     *
     * @param url - the destination to get the web info from
     * @return - List of movie objects
     */
    public List<Movie> setUpMovieObjects(String url, String movieType) {

        List<Movie> movieListPopular = new ArrayList<>();

        try {
            //Convert Json String to Json Object
            JSONObject moviesJsonObject = new JSONObject(getJsonFromTMDB(url));

            //Get the Json Array
            JSONArray moviesJsonArray = moviesJsonObject.getJSONArray(RESULT);

            for (int i = 0; i < moviesJsonArray.length(); i++) {
                JSONObject jsonObject = moviesJsonArray.getJSONObject(i);

                Movie newMovie = new Movie(
                    BASE_URL_FOR_POSTER + IMAGE_SIZE + jsonObject.getString(POSTER_PATH),
                    jsonObject.getString(MOVIE_TITLE),
                    jsonObject.getString(MOVIE_SYNOPSIS),
                    jsonObject.getString(MOVIE_RATING),
                    jsonObject.getString(MOVIE_RELEASE_DATE),
                    jsonObject.getString(MOVIE_ID),
                    movieType
                );

                movieListPopular.add(newMovie);
            }

        } catch (JSONException e) {

            Log.i("JSON", e.toString());
        }

        return movieListPopular;
    }

    /**
     * Gets the Youtube ids from the web and builds a youtube url
     * @param movieID - specific movie to get youtube url for
     * @return - list of trailers
     */
    public List<String> getTrailerYoutubeUrls(String movieID) {

        List<String> youtubeUrls = new ArrayList<>();

        try {
            JSONObject movieTrailerJSON = new JSONObject(
                getJsonFromTMDB(MovieAppConstants.MOVIE_INFO_BASE_URL + movieID
                    + MovieAppConstants.VIDEO + MovieAppConstants.API_KEY));

            JSONArray movieTrailerArray = movieTrailerJSON.getJSONArray(RESULT);

            for (int i = 0; i < movieTrailerArray.length(); i++) {

                JSONObject jsonObject = movieTrailerArray.getJSONObject(i);

                youtubeUrls.add(
                    buildYoutubeURL(jsonObject.getString(MOVIE_YOUTUBE_ID)));
            }

        } catch (JSONException e) {

        }
        return youtubeUrls;
    }

    public static String buildYoutubeURL(String youtubeMovieID) {
        return MovieAppConstants.YOUTUBE_BASE_URL + youtubeMovieID;
    }

    /**
     * Gets all reviews from the web for a specific movie
     * Then builds a Review object
     * @param movieID - specific movie to get
     * @return - list of reviews
     */
    public List<Review> getReviews(String movieID) {

        List<Review> reviews = new ArrayList<>();

        try {

            JSONObject movieTrailerJSON = new JSONObject(
                getJsonFromTMDB(MovieAppConstants.MOVIE_INFO_BASE_URL + movieID
                    + MovieAppConstants.REVIEWS + MovieAppConstants.API_KEY));

            JSONArray movieTrailerArray = movieTrailerJSON.getJSONArray(RESULT);

            for (int i = 0; i < movieTrailerArray.length(); i++) {

                JSONObject jsonObject = movieTrailerArray.getJSONObject(i);

                Review review = new Review(
                    jsonObject.getString(REVIEW_AUTHOR), jsonObject.getString(REVIEW_CONTENT));

                reviews.add(review);
            }

        } catch (JSONException e) {

        }
        return reviews;
    }

    /**
     * Gets all the favorite movies from the singleton class FavoriteMovies
     * Then gets all the info for those movies
     * @return - list of favorite movies
     */
    public List<Movie> getFavoriteMovies(Context context) {
        Uri contentUri = Uri.withAppendedPath(ContentProviderFavoriteMoviesDb.CONTENT_URI,
            MovieAppConstants.TABLE_FAVORITE_MOVIES);
        return new ArrayList<>(
            FavoriteMovieDataBaseConverter.getMoviesFromDb(context.getContentResolver()
                .query(contentUri, null, null, null, null)));
        }

    /**
     * Makes a review with the author and actual review
     */
    public class Review {

        private String mAuthor;

        private String mContent;

        public Review(String author, String content) {
            mAuthor = author;
            mContent = content;
        }

        public String getAuthor() {
            return mAuthor;
        }

        public String getContent() {
            return mContent;
        }
    }
}
