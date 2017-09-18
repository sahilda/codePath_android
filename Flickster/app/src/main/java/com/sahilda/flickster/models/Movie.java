package com.sahilda.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Movie implements Serializable {

    private static String imagePath = "https://image.tmdb.org/t/p/";

    private String posterPath;
    private String backdropPath;
    private String originalTitle;
    private String overview;
    private double voteAverage;
    private String releaseDate;
    private boolean adult;
    private double popularity;
    public PopularValues popular;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.voteAverage = jsonObject.getDouble("vote_average");
        this.releaseDate = jsonObject.getString("release_date");
        this.adult = jsonObject.getBoolean("adult");
        this.popularity = jsonObject.getDouble("popularity");
        this.popular = isPopularFilm() ? PopularValues.POPULAR : PopularValues.NOT_POPULAR;
    }

    public static ArrayList<Movie> fromJsonArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<>();

        for (int x = 0; x < array.length(); x++) {
            try {
                results.add(new Movie(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

    public enum PopularValues {
        POPULAR, NOT_POPULAR;
    }

    public String getPosterPath() {
        return String.format("%sw185%s", imagePath, posterPath);
    }

    public String getBackdropPath() {
        return String.format("%sw1000%s", imagePath, backdropPath);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public boolean isAdult() {
        return adult;
    }

    public double getPopularity() {
        return popularity;
    }

    public boolean isPopularFilm() {
        return (voteAverage > 7.0);
    }

}
