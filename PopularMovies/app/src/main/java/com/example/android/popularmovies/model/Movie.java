package com.example.android.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jorgemendes on 04/02/17.
 */

public class Movie implements Parcelable {

    public int id;
    public String posterUrl;
    public String originalTitle;
    public String releaseDate;
    public String overview;
    public double voteAverage;

    public Movie() {
    }

    protected Movie(Parcel in) {
        id = in.readInt();
        posterUrl = in.readString();
        originalTitle = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(posterUrl);
        parcel.writeString(originalTitle);
        parcel.writeString(releaseDate);
        parcel.writeString(overview);
        parcel.writeDouble(voteAverage);
    }
}
