package com.example.sammengistu.popularmoviesapp.activities;


import com.example.sammengistu.popularmoviesapp.MovieAppConstants;
import com.example.sammengistu.popularmoviesapp.R;
import com.example.sammengistu.popularmoviesapp.fragments.MovieDetailFragment;
import com.example.sammengistu.popularmoviesapp.fragments.MovieGalleryFragment;
import com.example.sammengistu.popularmoviesapp.model.Movie;
import com.example.sammengistu.popularmoviesapp.model.SingleFragmentActivity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * This class is used to decide which type of device is being used and will handle an event
 * differently based on the type of device
 */
public class MovieGalleryActivity extends SingleFragmentActivity
    implements MovieGalleryFragment.Callbacks {

    @Override
    public Fragment createFragment() {
        return new MovieGalleryFragment();
    }

    //Makes a call to the res/values/refs folder and decides which screen size is being used and
    // which layout to use
    @Override
    protected int getLayoutResId(){
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onGalleryItemSelected(Movie movie, String movieType) {

        if (findViewById(R.id.movie_info_fragment) == null){
            //Start an instance of movieDetailActivity if the view does not contain a movieInfoFragment
            //This portion would be used for small screen sizes (Handsets)
            Intent movieDetailIntent = new Intent(
                this, MovieDetailActivity.class);

            movieDetailIntent.putExtra(
                MovieAppConstants.MOVIE, movie);

            startActivity(movieDetailIntent);

        } else {
            //This portion is used for larger screen sizes since it will contain the fragment portion
            //movieInfoFragment. Use a fragment transaction to update that portion
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment oldDetail = fragmentManager.findFragmentById(R.id.movie_info_fragment);
            Fragment newDetail = MovieDetailFragment.newInstance(
                movie);

            if (oldDetail != null){
                fragmentTransaction.remove(oldDetail);
            }

            fragmentTransaction.add(R.id.movie_info_fragment, newDetail);
            fragmentTransaction.commit();
        }
    }
}
