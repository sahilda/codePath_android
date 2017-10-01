package com.sahilda.bettertwitter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sahilda.bettertwitter.apis.TwitterApplication;
import com.sahilda.bettertwitter.apis.TwitterClient;
import com.sahilda.bettertwitter.models.Tweet;
import com.sahilda.bettertwitter.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TwitterClient client;
    private TweetAdapter tweetAdapter;
    private User currentUser;
    private ArrayList<Tweet> tweets;
    private RecyclerView rvTweets;
    private EndlessRecyclerViewScrollListener scrollListener;
    private long currentMinId = Long.MAX_VALUE - 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = TwitterApplication.getRestClient();
        setupToolbar();
        setupRecyclerView();
        getCurrentUser();
        populateTimeline(currentMinId);
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void composeMessage() {
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("user", currentUser);
        startActivity(i);
    }

    private void setupRecyclerView() {
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(tweetAdapter);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                populateTimeline(currentMinId);
            }
        };
        rvTweets.addOnScrollListener(scrollListener);
    }

    private void getCurrentUser() {
        client.getCurrentUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    Log.d("TwitterClient", response.toString());
                    currentUser = User.fromJson(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                Toast.makeText(getApplicationContext(), "API Error", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }

    private void populateTimeline(long maxId) {
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
                for (int i = 0; i < response.length(); i++) {
                    Tweet tweet = null;
                    try {
                        tweet = Tweet.fromJson(response.getJSONObject(i));
                        setMinId(tweet);
                        tweets.add(tweet);
                        tweetAdapter.notifyItemChanged(tweets.size() - 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                Toast.makeText(getApplicationContext(), "API Error", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }

    private void setMinId(Tweet tweet) {
        if (tweet.id < currentMinId) {
            currentMinId = tweet.id - 1;
        }
    }

    private void resetScrollState() {
        tweets.clear();
        tweetAdapter.notifyDataSetChanged();
        currentMinId = Long.MAX_VALUE - 1;
        scrollListener.resetState();
    }

}
