package com.example.sammengistu.popularmoviesapp.asynctasks;

import com.example.sammengistu.popularmoviesapp.MovieAppConstants;
import com.example.sammengistu.popularmoviesapp.MovieInfoGetter;
import com.example.sammengistu.popularmoviesapp.adapters.MoviePosterAdapter;
import com.example.sammengistu.popularmoviesapp.model.Movie;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;


public class GetMoviePostersAndInfo extends AsyncTask<String, Void, List<Movie>> {

    private Activity mActivity;
    private String mMovieTypes;
    private List<Movie> mMovieList;
    private GridView mGridView;

    public GetMoviePostersAndInfo(Activity activity,
                                  String movieType, List<Movie> movieList,
                                  GridView gridView){
        mActivity = activity;
        mMovieTypes = movieType;
        mMovieList = movieList;
        mGridView = gridView;

    }
    @Override
    protected List<Movie> doInBackground(String... params) {

        List<Movie> listOfMovies;

        if (params[0].equals(MovieAppConstants.FAVORITES)) {
            //If favorites get movie favorites from database
            listOfMovies = new MovieInfoGetter().getFavoriteMovies(mActivity);
        } else {
            //download images from internet
            listOfMovies = new MovieInfoGetter().setUpMovieObjects(params[0], mMovieTypes);
        }
        return listOfMovies;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {

        mMovieList = new ArrayList<>(movies);

        List<String> moviePosterURls = new ArrayList<>();

        //Get urls to load posters in adapter
        for (Movie movie : mMovieList) {
            moviePosterURls.add(movie.getMoviePosterUrl());
        }

        mGridView.setAdapter(
            new MoviePosterAdapter(moviePosterURls, mActivity, mMovieTypes, mMovieList,
                mGridView));

    }

    public void setMovieTypes(String movieTypes) {
        mMovieTypes = movieTypes;
    }

    public List<Movie> getMovieList() {
        return mMovieList;
    }
}