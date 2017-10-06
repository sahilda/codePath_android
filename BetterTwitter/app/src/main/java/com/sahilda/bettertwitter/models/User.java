package com.sahilda.bettertwitter.models;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.sahilda.bettertwitter.MyDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Table(database = MyDatabase.class)
public class User extends BaseModel implements Serializable {

    @PrimaryKey
    @Column
    public long uid;

    @Column
    public String name;

    @Column
    public String screenName;

    @Column
    public String profileImageUrl;

    @Column
    public String tagline;

    @Column
    public String followersCount;

    @Column
    public String followingCount;

    @Column
    public boolean currentUser = false;

    public static User fromJson(JSONObject jsonObject) throws JSONException {
        User user = new User();

        user.name = jsonObject.getString("name");
        user.uid = jsonObject.getLong("id");
        user.screenName = "@" + jsonObject.getString("screen_name");
        user.profileImageUrl = jsonObject.getString("profile_image_url");

        user.tagline = jsonObject.getString("description");
        user.followersCount = jsonObject.getString("followers_count");
        user.followingCount = jsonObject.getString("friends_count");

        return user;
    }

}
