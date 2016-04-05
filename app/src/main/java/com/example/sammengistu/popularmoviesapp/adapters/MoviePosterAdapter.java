package com.example.sammengistu.popularmoviesapp.adapters;

import com.example.sammengistu.popularmoviesapp.DbBitmapUtility;
import com.example.sammengistu.popularmoviesapp.MovieAppConstants;
import com.example.sammengistu.popularmoviesapp.R;
import com.example.sammengistu.popularmoviesapp.model.Movie;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class MoviePosterAdapter extends BaseAdapter {

    private List<String> mMoviesPosterList;
    private Activity mActivity;
    private String mMovieTypes;
    private List<Movie> mMovieList;
    private GridView mGridView;

    public MoviePosterAdapter(List<String> moviePosterURLList, Activity activity,
                              String movieType, List<Movie> movieList,
                              GridView gridView) {
        mMoviesPosterList = moviePosterURLList;
        mActivity = activity;
        mMovieTypes = movieType;
        mMovieList = movieList;
        mGridView = gridView;
    }

    @Override
    public int getCount() {
        return mMoviesPosterList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMoviesPosterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            viewHolder.position = position;
            // if it's not recycled, initialize the view
            LayoutInflater inflater = mActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.movie_poster_layout, parent, false);

            viewHolder.mImageView = (ImageView)
                convertView.findViewById(R.id.movie_poster_image_view);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //Load movies from the database if favorites or load the movie from the internet
        if (mMovieTypes.equals(MovieAppConstants.FAVORITES)) {

            byte[] moviePoster = mMovieList.get(position).getPosterImage();
            viewHolder.mImageView.setImageBitmap(DbBitmapUtility.getImage(moviePoster));

        } else {
            Picasso.with(mActivity)
                .load(mMoviesPosterList.get(position))
                .resize(mGridView.getColumnWidth(),
                    mGridView.getHeight() / 2)
                .centerCrop()
                .placeholder(R.drawable.empty_poster)
                .error(R.drawable.empty_poster)
                .into(viewHolder.mImageView);

        }
        viewHolder.position = position;
        return convertView;
    }

    //Used to recycle views
    static class ViewHolder {
        ImageView mImageView;
        int position;
    }
}