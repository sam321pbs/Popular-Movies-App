package com.example.sammengistu.popularmoviesapp.fragments;

import com.example.sammengistu.popularmoviesapp.AppNetwork;
import com.example.sammengistu.popularmoviesapp.ContentProviderFavoriteMoviesDb;
import com.example.sammengistu.popularmoviesapp.DbBitmapUtility;
import com.example.sammengistu.popularmoviesapp.MovieAppConstants;
import com.example.sammengistu.popularmoviesapp.MovieInfoGetter;
import com.example.sammengistu.popularmoviesapp.R;
import com.example.sammengistu.popularmoviesapp.asynctasks.YoutubeURLAndReviewLoader;
import com.example.sammengistu.popularmoviesapp.databasefavoritemovies.FavoriteMovieDataBaseConverter;
import com.example.sammengistu.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailFragment extends Fragment {

    private List<String> mMovieTrailerYoutubeLinks;
    private List<MovieInfoGetter.Review> mMovieReviews;
    private boolean mIsFavorite;
    private Movie mSelectedMovie;
    private String mMovieType;

    @Bind(R.id.movie_poster_id)
    ImageView mMoviePosterImageView;
    @Bind(R.id.movie_title)
    TextView mMovieTitle;
    @Bind(R.id.movie_synopsis)
    TextView mMovieSynopsis;
    @Bind(R.id.movie_rating)
    TextView mUserRating;
    @Bind(R.id.movie_release_date)
    TextView mReleaseDate;
    @Bind(android.R.id.list)
    ListView mTrailerListView;
    @Bind(R.id.review_listView)
    ListView mReviewsListView;
    @Bind(R.id.favorite_button)
    Button mFavoriteButton;

    private YoutubeURLAndReviewLoader mYoutubeURLAndReviewLoader;

    private View.OnClickListener mFavoriteButtonListener =
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Updates the database when favorite button is clicked
                if (mIsFavorite) {
                    mIsFavorite = false;
                    mFavoriteButton.setText(R.string.mark_as_favorite);

                    Uri contentUri = Uri.withAppendedPath(ContentProviderFavoriteMoviesDb.CONTENT_URI,
                        MovieAppConstants.TABLE_FAVORITE_MOVIES);

                    getActivity().getContentResolver().delete(contentUri,
                        MovieAppConstants.COLUMN_MOVIE_ID + " = ?", new String[]{mSelectedMovie.getMovieID()});

                } else {
                    mIsFavorite = true;
                    mFavoriteButton.setText(R.string.favorite);

                    mSelectedMovie.setPosterImage(DbBitmapUtility.getBytes(
                        ((BitmapDrawable) mMoviePosterImageView.getDrawable()).getBitmap()));

                    Uri contentUri = Uri.withAppendedPath(ContentProviderFavoriteMoviesDb.CONTENT_URI,
                        MovieAppConstants.TABLE_FAVORITE_MOVIES);

                    getActivity().getContentResolver().insert(contentUri,
                        FavoriteMovieDataBaseConverter.insertFavoriteMovieToDB(mSelectedMovie));
                }
            }
        };

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        setHasOptionsMenu(true);

        mMovieTrailerYoutubeLinks = new ArrayList<>();
        mMovieReviews = new ArrayList<>();

        Bundle data = getArguments().getBundle(MovieAppConstants.MOVIE);


        if (data != null) {
            mSelectedMovie = data.getParcelable(MovieAppConstants.MOVIE);
        } else {
            //Make an empty movie if there is an error
            mSelectedMovie = new Movie("", "", "", "", "", "", "");
        }

        if (mSelectedMovie != null) {
            mMovieType = mSelectedMovie.getMovieType();
        } else {
            mMovieType = MovieAppConstants.FAVORITES;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View detailView = inflater.inflate(R.layout.movie_info_fragment, container, false);

        ButterKnife.bind(this, detailView);

        mFavoriteButton.setOnClickListener(mFavoriteButtonListener);

        mMovieTitle.setText(mSelectedMovie.getMovieTitle());

        mMovieSynopsis.setText(mSelectedMovie.getSynopsis());

        String movieRatingString = mSelectedMovie.getRating() + getString(R.string.over_ten_rating);
        mUserRating.setText(movieRatingString);

        mReleaseDate.setText(mSelectedMovie.getReleaseDate());

        setUpPosterView();

        mTrailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mMovieTrailerYoutubeLinks = new ArrayList<>(
                    mYoutubeURLAndReviewLoader.getMovieTrailerYoutubeLinks());


                startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mMovieTrailerYoutubeLinks.get(position))));
            }
        });

        mTrailerListView.setFocusable(false);
        mReviewsListView.setFocusable(false);

        return detailView;
    }

    /**
     * Attaches the poster to the imageView either from the database or from a url
     */
    private void setUpPosterView() {
        try {

            if (mMovieType.equals(MovieAppConstants.FAVORITES)) {
                mMoviePosterImageView.setImageBitmap(
                    DbBitmapUtility.getImage(mSelectedMovie.getPosterImage())
                );

            } else {
                if (AppNetwork.isNetworkAvailable(getActivity())) {
                    Picasso.with(getActivity())
                        .load(mSelectedMovie.getMoviePosterUrl())
                        .resize(350, 500)
                        .centerCrop()
                        .placeholder(R.drawable.empty_poster)
                        .error(R.drawable.empty_poster)
                        .into(mMoviePosterImageView);
                }
            }

        } catch (IllegalArgumentException e) {

        }
    }

    private void loadTrailersAndReviews() {
        mYoutubeURLAndReviewLoader = new YoutubeURLAndReviewLoader(mMovieTrailerYoutubeLinks,
            mMovieReviews,
            mTrailerListView,
            mReviewsListView,
            getActivity());
        mYoutubeURLAndReviewLoader.execute(mSelectedMovie.getMovieID());
    }


    /**
     * Takes in a movie and puts it in a bundle and gets used to create a MovieDetailFragment
     * @param movie - movie info to use
     * @return - MovieDetailFragment
     */
    public static MovieDetailFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable(MovieAppConstants.MOVIE, movie);

        return MovieDetailFragment.newInstance(args);
    }

    /**
     * Takes in a bundle that is a movie and uses it to create an instance of MovieDetailFragment
     * @param movie - movie info to use
     * @return - MovieDetailFragment
     */
    public static MovieDetailFragment newInstance(Bundle movie) {
        Bundle args = new Bundle();
        args.putBundle(MovieAppConstants.MOVIE, movie);

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        movieDetailFragment.setArguments(args);

        return movieDetailFragment;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_share:

                mMovieTrailerYoutubeLinks = new ArrayList<>(
                    mYoutubeURLAndReviewLoader.getMovieTrailerYoutubeLinks());

                //Can share trailers if there are trailers
                if (mMovieTrailerYoutubeLinks.size() > 0) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, getActivity().getString(R.string.text_message_trailer)
                        + mMovieTrailerYoutubeLinks.get(0));
                    sendIntent.setType(getActivity().getString(R.string.type_of_text_intent_to_send));
                    startActivity(sendIntent);
                } else {
                    Toast.makeText(getActivity(), R.string.no_trailers_offline,
                        Toast.LENGTH_SHORT).show();
                }

                return true;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        new UpdateFavoriteButton().execute();
        if (AppNetwork.isNetworkAvailable(getActivity())
            && mMovieTrailerYoutubeLinks.size() == 0) {
            loadTrailersAndReviews();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mYoutubeURLAndReviewLoader.cancel(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
        outState.putParcelable(MovieAppConstants.MOVIE, mSelectedMovie);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            mSelectedMovie = savedInstanceState.getParcelable(MovieAppConstants.MOVIE);
        }
    }

    private class UpdateFavoriteButton extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            Uri contentUri = Uri.withAppendedPath(ContentProviderFavoriteMoviesDb.CONTENT_URI,
                MovieAppConstants.TABLE_FAVORITE_MOVIES);

            ContentResolver cr = getActivity().getContentResolver();

            Bundle doesExist = cr.call(contentUri,
                MovieAppConstants.CONTENT_PROVIDER_DOES_MOVIE_EXIST_METHOD,
                mSelectedMovie.getMovieID(), null);

        return doesExist.getBoolean(MovieAppConstants.DOES_EXIST);
        }

        @Override
        protected void onPostExecute(Boolean doesMovieExist) {
            //Updates the favorite button if this title is apart of favorite movies database
            if (doesMovieExist) {
                mFavoriteButton.setText(R.string.favorite);
                mIsFavorite = true;

            } else {
                mFavoriteButton.setText(R.string.mark_as_favorite);
                mIsFavorite = false;
            }
        }
    }
}
