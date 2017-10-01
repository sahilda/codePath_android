package com.sahilda.bettertwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {

    public String body;
    public long id;
    public String createdAt;
    public String relativeTime;
    public User user;

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("text");
        tweet.id = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.relativeTime = ParseRelativeDate.getRelativeTimeAgo(tweet.createdAt);
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        return tweet;
    }

}
