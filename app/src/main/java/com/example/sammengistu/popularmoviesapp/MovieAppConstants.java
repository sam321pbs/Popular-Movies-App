package com.example.sammengistu.popularmoviesapp;

/**
 * Created by SamMengistu on 2/19/16.
 */
public class MovieAppConstants {

    //Api parts
    public static final String PART_BEFORE_API = "?api_key=";

    //MovieInfoGetter network info
    public static final String POPULAR_MOVIES_URL =
        "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key="
            + API_KEY;
    public static final String TOP_RATED_MOVIES_URL =
        "http://api.themoviedb.org/3/movie/top_rated" + PART_BEFORE_API
            + API_KEY;
    public static final String VIDEO = "/videos" + PART_BEFORE_API;
    public static final String MOVIE_INFO_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String REVIEWS = "/reviews" + PART_BEFORE_API;
    public static final String YOUTUBE_BASE_URL = "http://www.youtube.com/watch?v=";
    public static final String FAVORITES = "FAVORITES";

    //SingleFragmentActivity
    public static final String FRAGMENT_STATE = "mFragment";
    public static final String MOVIE_TYPE = "MOVIE_TYPE";
    public static final String MOVIE = "Movie";

    //TABLE INFO
    public static final String TABLE_FAVORITE_MOVIES = "FAVORITE";
    public static final String COLUMN_MOVIE_ID = "MOVIE";
    public static final String COLUMN_TITLE = "TITLE";
    public static final String COLUMN_SYNOPSIS = "SYNOPSIS";
    public static final String COLUMN_RATING = "RATING";
    public static final String COLUMN_RELEASE_DATE = "RELEASE";
    public static final String COLUMN_POSTER = "POSTER";
    public static final String COLUMN_ID = "_ID";

    //Query from database
    public static final String SELECT_FROM = "select * from ";
    public static final String SELECT_COUNT_FROM = "select count(*) from ";
    public static final String QUERY_WHERE = " where ";
    public static final String QUERY_LIMIT = "=? limit 1";

    public static final String CONTENT_PROVIDER_DOES_MOVIE_EXIST_METHOD = "doesMovieIdExist";
    public static final String DOES_EXIST = "Does Exist";

}
