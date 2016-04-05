package com.example.sammengistu.popularmoviesapp.activities;

import com.example.sammengistu.popularmoviesapp.fragments.MovieDetailFragment;
import com.example.sammengistu.popularmoviesapp.model.SingleFragmentActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * This class is used if the user is using a handset device
 */
public class MovieDetailActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment() {
        Bundle data = getIntent().getExtras();

        return MovieDetailFragment.newInstance(data);
    }
}
