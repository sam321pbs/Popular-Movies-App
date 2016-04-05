package com.example.sammengistu.popularmoviesapp.adapters;

import com.example.sammengistu.popularmoviesapp.MovieInfoGetter;
import com.example.sammengistu.popularmoviesapp.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ReviewsListAdapter extends ArrayAdapter<MovieInfoGetter.Review> {

    private Activity mActivity;

    public ReviewsListAdapter(List<MovieInfoGetter.Review> reviews, Activity activity) {
        super(activity, 0, reviews);
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = mActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.single_review, parent, false);

            viewHolder.mTextViewAuthor = (TextView)convertView.findViewById(R.id.review_author);
            viewHolder.mReview = (TextView)convertView.findViewById(R.id.review_content);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        MovieInfoGetter.Review review = getItem(position);

        String author = mActivity
            .getString(R.string.by) + review.getAuthor();
        viewHolder.mTextViewAuthor.setText(author);
        viewHolder.mReview.setText(review.getContent());

        return convertView;
    }
    static class ViewHolder {
        TextView mTextViewAuthor;
        TextView mReview;
    }
}
