package com.example.sammengistu.popularmoviesapp.adapters;

import com.example.sammengistu.popularmoviesapp.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class TrailerListAdapter extends ArrayAdapter<String> {

    private Activity mActivity;

    public TrailerListAdapter(List<String> trailerUrls, Activity activity) {
        super(activity, 0, trailerUrls);
        mActivity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater =  mActivity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.trailer_title, parent, false);

            viewHolder.mTextView = (TextView)convertView.findViewById(R.id.trailer_title_name);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        String trailerTitle = mActivity.getString(R.string.trailer) + (position + 1);
        viewHolder.mTextView.setText(trailerTitle);

        return convertView;
    }

    static class ViewHolder {
        TextView mTextView;
    }
}