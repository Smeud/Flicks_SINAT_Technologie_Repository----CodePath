package com.sinat.flicks.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;


/**
 * Created by Roseline on 7 oct. 2017.
 */
@Parcel // annotation indicates class is Parcelable

public class Movie {

    // values from API - Fields must be public for parceler
    String title;
    String overview;
    String posterPath; // only the path
    String backdropPath;
    Double voteAverage;

    // no-arg, empty constructor required for Parceler
    public Movie(){}

    // initialize from JSON data
    public Movie(JSONObject movie) throws JSONException {

        title = movie.getString("title");
        overview = movie.getString("overview");
        posterPath = movie.getString("poster_path");
        backdropPath = movie.getString("backdrop_path");
        voteAverage = movie.getDouble("vote_average");
    }

    public String getTitle()
    {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }
}
