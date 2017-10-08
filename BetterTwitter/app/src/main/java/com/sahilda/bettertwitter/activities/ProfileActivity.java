package com.sahilda.bettertwitter.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sahilda.bettertwitter.R;
import com.sahilda.bettertwitter.fragments.TweetsListFragment;
import com.sahilda.bettertwitter.fragments.UserTimelineFragment;
import com.sahilda.bettertwitter.models.Tweet;
import com.sahilda.bettertwitter.models.User;

public class ProfileActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = (User) getIntent().getSerializableExtra("user");
        setupToolbar();
        populateUserHeadline();
        UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(user.screenName);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, userTimelineFragment);
        ft.commit();
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(user.screenName);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
    }

    private void populateUserHeadline() {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName.setText(user.name);
        tvTagline.setText(user.tagline);
        tvFollowers.setText(user.followersCount + " Followers");
        tvFollowing.setText(user.followingCount + " Following");
        Glide.with(this).load(user.profileImageUrl)
                .into(ivProfileImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static void openProfileActivity(Context context, User user) {
        Intent i = new Intent(context, ProfileActivity.class);
        i.putExtra("user", user);
        context.startActivity(i);
    }

    @Override
    public void onTweetSelected(Tweet tweet) {
        Intent i = new Intent(getApplicationContext(), TweetDetailActivity.class);
        i.putExtra("tweet", tweet);
        startActivity(i);
    }

    @Override
    public void onUserImageSelected(User user) {
        ProfileActivity.openProfileActivity(this, user);
    }

}
