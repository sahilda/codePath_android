package com.sahilda.bettertwitter.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.sahilda.bettertwitter.MyDatabase;
import com.sahilda.bettertwitter.utils.ParseRelativeDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Table(database = MyDatabase.class)
public class Tweet extends BaseModel implements Serializable {

    @Column
    @PrimaryKey
    public long id;

    @Column
    public String body;

    @Column
    public String createdAt;

    @Column
    public String relativeTime;

    @Column
    @ForeignKey
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
