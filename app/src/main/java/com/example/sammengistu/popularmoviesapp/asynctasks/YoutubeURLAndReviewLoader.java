package com.example.sammengistu.popularmoviesapp.asynctasks;

import com.example.sammengistu.popularmoviesapp.MovieInfoGetter;
import com.example.sammengistu.popularmoviesapp.adapters.ReviewsListAdapter;
import com.example.sammengistu.popularmoviesapp.adapters.TrailerListAdapter;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.ListView;

import java.util.List;


public class YoutubeURLAndReviewLoader extends AsyncTask<String, Void, Void> {

    private List<String> mMovieTrailerYoutubeLinks;
    private List<MovieInfoGetter.Review> mMovieReviews;
    private ListView mTrailerListView;
    private ListView mReviewsListView;
    private Activity mActivity;

    public YoutubeURLAndReviewLoader(List<String> movieTrailerYoutubeLinks,
                                     List<MovieInfoGetter.Review> movieReviews,
                                     ListView trailerListView, ListView reviewsListView,
                                     Activity activity) {

        mMovieTrailerYoutubeLinks = movieTrailerYoutubeLinks;
        mMovieReviews = movieReviews;
        mTrailerListView = trailerListView;
        mReviewsListView = reviewsListView;
        mActivity = activity;
    }

    @Override
    protected Void doInBackground(String... params) {
        String movieID = params[0];

        mMovieTrailerYoutubeLinks.clear();

        mMovieTrailerYoutubeLinks = new MovieInfoGetter().getTrailerYoutubeUrls(movieID);
        mMovieReviews = new MovieInfoGetter().getReviews(movieID);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

        mTrailerListView.setAdapter(new TrailerListAdapter(mMovieTrailerYoutubeLinks,
            mActivity));
        mReviewsListView.setAdapter(new ReviewsListAdapter(mMovieReviews,
            mActivity));

    }

    public List<String> getMovieTrailerYoutubeLinks() {
        return mMovieTrailerYoutubeLinks;
    }
}
