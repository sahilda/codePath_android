package com.sahilda.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class Article {

    public String mWebUrl;
    public String mHeadline;
    public String mThumbNail;

    public Article() {

    }

    public Article(JSONObject jsonObject) {
        try {
            this.mWebUrl = jsonObject.getString("web_url");
            this.mHeadline = jsonObject.getJSONObject("headline").getString("main");
            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0) {
                JSONObject multimediaJson = multimedia.getJSONObject(0);
                this.mThumbNail = "http://www.nytimes.com/" + multimediaJson.getString("url");
            } else {
                this.mThumbNail = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> articles = new ArrayList<>();
        for (int x = 0; x < array.length(); x++) {
            try {
                articles.add(new Article(array.getJSONObject(x)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return articles;
    }

    public String getWebUrl() {
        return mWebUrl;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getThumbNail() {
        return mThumbNail;
    }

}
