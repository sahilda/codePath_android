package com.sahilda.flickster.models;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class Movie implements Serializable {

    private static String imagePath = "https://image.tmdb.org/t/p/";

    private int id;
    private String posterPath;
    private String backdropPath;
    private String originalTitle;
    private String overview;
    private double voteAverage;
    private String releaseDate;
    private boolean adult;
    private double popularity;
    public PopularValues popular;
    private String videoKey;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.id = jsonObject.getInt("id");
        this.posterPath = jsonObject.getString("poster_path");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.voteAverage = jsonObject.getDouble("vote_average");
        this.releaseDate = jsonObject.getString("release_date");
        this.adult = jsonObject.getBoolean("adult");
        this.popularity = jsonObject.getDouble("popularity");
        this.popular = isPopularFilm() ? PopularValues.POPULAR : PopularValues.NOT_POPULAR;
        populateVideoLink();
    }

    public enum PopularValues {
        POPULAR, NOT_POPULAR;
    }

    private void populateVideoLink() {
        String url = "https://api.themoviedb.org/3/movie/" + id + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray resultsJson = null;

                try {
                    resultsJson = response.getJSONArray("results");
                    Log.d("DEBUG", resultsJson.toString());
                    for (int i = 0; i < resultsJson.length(); i++) {
                        JSONObject videoJson = resultsJson.getJSONObject(i);
                        if (videoJson.getString("site").equals("YouTube")) {
                            videoKey = videoJson.getString("key");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
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

    public String getVideoKey() {
        return videoKey;
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

}
