package com.example.sammengistu.popularmoviesapp.fragments;

import com.example.sammengistu.popularmoviesapp.AppNetwork;
import com.example.sammengistu.popularmoviesapp.DbBitmapUtility;
import com.example.sammengistu.popularmoviesapp.MovieAppConstants;
import com.example.sammengistu.popularmoviesapp.R;
import com.example.sammengistu.popularmoviesapp.asynctasks.GetMoviePostersAndInfo;
import com.example.sammengistu.popularmoviesapp.model.Movie;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MovieGalleryFragment extends Fragment {

    @Bind(R.id.movie_grid_view)
    GridView mMoviePosterGridView;

    private List<Movie> mMovieList;
    private String mMovieTypes;

    private Callbacks mCallBacks;

    private GetMoviePostersAndInfo mGetMoviePostersAndInfo;

    private AdapterView.OnItemClickListener mOnItemSelectedListener =
        new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ImageView posterImageView = (ImageView) view;
                BitmapDrawable bitmapDrawable = (BitmapDrawable) posterImageView.getDrawable();

                //Get the movie list from the Asyctask to select the movie
                mMovieList = mGetMoviePostersAndInfo.getMovieList();

                mMovieList.get(position).setPosterImage(
                    DbBitmapUtility.getBytes(bitmapDrawable.getBitmap()));

                //Makes a call to the MovieGalleryActivity to handle the click event
                mCallBacks.onGalleryItemSelected(mMovieList.get(position), mMovieTypes);
            }
        };

    /**
     * Required interface for hosting activity
     * This uses the activity to be the communicator between the fragments when using a tablet when
     * a onGalleryClick() is called
     */
    public interface Callbacks {
        void onGalleryItemSelected(Movie movie, String movieType);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Gets attached to the activity to handle the event
        mCallBacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBacks = null;
    }

    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View galleryView = inflater.inflate(R.layout.fragment_movie_gallery, container, false);

        ButterKnife.bind(this, galleryView);

        mMoviePosterGridView = (GridView) galleryView.findViewById(R.id.movie_grid_view);

        mMoviePosterGridView.setOnItemClickListener(mOnItemSelectedListener);

        //Used when the fragment is first created
        if (mMovieTypes == null){
            mMovieTypes = MovieAppConstants.POPULAR_MOVIES_URL;
        }

        mGetMoviePostersAndInfo = new GetMoviePostersAndInfo(getActivity(),
            MovieAppConstants.POPULAR_MOVIES_URL, mMovieList, mMoviePosterGridView);

        return galleryView;
    }

    /**
     * Loads the view from the appropriate movie posters
     */
    private void loadGalleryView(){

        //if there is no network load favorite movies
        if (AppNetwork.isNetworkAvailable(getActivity()) &&
            !mMovieTypes.equals(MovieAppConstants.FAVORITES)){

            if (mMovieTypes.equals(MovieAppConstants.POPULAR_MOVIES_URL)){
                executeGalleryView(MovieAppConstants.POPULAR_MOVIES_URL);
            }  else{
                executeGalleryView(MovieAppConstants.TOP_RATED_MOVIES_URL);
            }

        } else {

            executeGalleryView(MovieAppConstants.FAVORITES);
        }
    }

    /**
     * Executes the asynctask to load the movie posters
     * @param typeToLoad
     */
    private void executeGalleryView (String typeToLoad){

        mGetMoviePostersAndInfo = new GetMoviePostersAndInfo(getActivity(),
            typeToLoad, mMovieList, mMoviePosterGridView);

        mGetMoviePostersAndInfo.execute(typeToLoad);

        mMovieList = mGetMoviePostersAndInfo.getMovieList();
        mGetMoviePostersAndInfo.setMovieTypes(typeToLoad);
        mMovieTypes = typeToLoad;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                executeGalleryView(MovieAppConstants.POPULAR_MOVIES_URL);
                return true;

            case R.id.action_highest_rated:
                executeGalleryView(MovieAppConstants.TOP_RATED_MOVIES_URL);
                return true;

            case R.id.action_favorite_movies:
                executeGalleryView(MovieAppConstants.FAVORITES);

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Save the fragment's state here
        outState.putString(MovieAppConstants.MOVIE_TYPE, mMovieTypes);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //Restore the fragment's state here
            mMovieTypes = savedInstanceState.getString(MovieAppConstants.MOVIE_TYPE);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        loadGalleryView();
    }
}