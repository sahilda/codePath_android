package com.sahilda.bettertwitter.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.sahilda.bettertwitter.R;
import com.sahilda.bettertwitter.TwitterApplication;
import com.sahilda.bettertwitter.apis.TwitterClient;
import com.sahilda.bettertwitter.fragments.HomeTimelineFragment;
import com.sahilda.bettertwitter.fragments.TweetsListFragment;
import com.sahilda.bettertwitter.fragments.TweetsPagerAdapter;
import com.sahilda.bettertwitter.models.Tweet;
import com.sahilda.bettertwitter.models.User;
import com.sahilda.bettertwitter.models.User_Table;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements TweetsListFragment.TweetSelectedListener {

    private User currentUser;
    private final int REQUEST_CODE = 20;
    private TwitterClient client;
    private TweetsPagerAdapter tweetsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        setupToolbar();
        getCurrentUser();

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager(), this);
        vpPager.setAdapter(tweetsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(vpPager);
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Better Twitter");
        toolbar.setLogo(R.drawable.ic_logo);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miCompose:
                composeMessage();
                return true;
            case R.id.miProfile:
                ProfileActivity.openProfileActivity(this, currentUser);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void composeMessage() {
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("user", currentUser);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = (Tweet) data.getExtras().getSerializable("tweet");
            int code = data.getExtras().getInt("code", 0);
            HomeTimelineFragment homeTimelineFragment = (HomeTimelineFragment) getSupportFragmentManager().getFragments().get(0);
            homeTimelineFragment.tweets.add(0, tweet);
            homeTimelineFragment.tweetAdapter.notifyDataSetChanged();
            homeTimelineFragment.rvTweets.smoothScrollToPosition(0);
        }
    }

    private void getCurrentUser() {
        client.getCurrentUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.d("TwitterClient", response.toString());
                    currentUser = User.fromJson(response);
                    currentUser.currentUser = true;
                    currentUser.save();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                if (errorResponse != null) {
                    Log.d("TwitterClient", errorResponse.toString());
                }
                currentUser = SQLite.select()
                        .from(User.class)
                        .where(User_Table.currentUser.is(true))
                        .querySingle();
                Toast.makeText(getApplicationContext(), "API Error", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
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
