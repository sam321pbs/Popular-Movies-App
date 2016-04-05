package com.example.sammengistu.popularmoviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String mMoviePosterUrl;
    private String mMovieTitle;
    private String mSynopsis;
    private String mRating;
    private String mReleaseDate;
    private String mMovieID;
    private byte[] mPosterImage;
    private String mMovieType;

    public Movie(String moviePosterUrl, String movieTitle, String synopsis,
                 String rating, String releaseDate, String movieID,
                 String movieType) {
        mMoviePosterUrl = moviePosterUrl;
        mMovieTitle = movieTitle;
        mSynopsis = synopsis;
        mRating = rating;
        mReleaseDate = releaseDate;
        mMovieID = movieID;
        mPosterImage = null;
        mMovieType = movieType;
    }

    protected Movie(Parcel in) {
        mMoviePosterUrl = in.readString();
        mMovieTitle = in.readString();
        mSynopsis = in.readString();
        mRating = in.readString();
        mReleaseDate = in.readString();
        mMovieID = in.readString();
        mPosterImage = in.createByteArray();
        mMovieType = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getMoviePosterUrl() {
        return mMoviePosterUrl;
    }

    public String getMovieTitle() {
        return mMovieTitle;
    }

    public String getSynopsis() {
        return mSynopsis;
    }

    public String getRating() {
        return mRating;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getMovieID() {
        return mMovieID;
    }

    public byte[] getPosterImage() {
        return mPosterImage;
    }

    public void setPosterImage(byte[] posterImage) {
        mPosterImage = posterImage;
    }

    public String getMovieType() {
        return mMovieType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMoviePosterUrl);
        dest.writeString(mMovieTitle);
        dest.writeString(mSynopsis);
        dest.writeString(mRating);
        dest.writeString(mReleaseDate);
        dest.writeString(mMovieID);
        dest.writeByteArray(mPosterImage);
        dest.writeString(mMovieType);
    }
}
