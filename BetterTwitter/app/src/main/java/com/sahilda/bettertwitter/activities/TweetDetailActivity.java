package com.sahilda.bettertwitter.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sahilda.bettertwitter.R;
import com.sahilda.bettertwitter.models.Tweet;

public class TweetDetailActivity extends AppCompatActivity {

    private ImageView ivProfileImage;
    private TextView tvName;
    private TextView tvUserName;
    private TextView tvTime;
    private TextView tvBody;
    private Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        setupToolbar();
        tweet = (Tweet) getIntent().getSerializableExtra("tweet");

        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvBody = (TextView) findViewById(R.id.tvBody);

        tvName.setText((tweet.user.name));
        tvUserName.setText(tweet.user.screenName);
        tvTime.setText(tweet.relativeTime);
        tvBody.setText(tweet.body);
        Glide.with(getApplicationContext())
                .load(tweet.user.profileImageUrl)
                .into(ivProfileImage);
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Details");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
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

}
