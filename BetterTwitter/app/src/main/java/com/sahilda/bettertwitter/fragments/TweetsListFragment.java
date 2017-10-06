package com.sahilda.bettertwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.sahilda.bettertwitter.R;
import com.sahilda.bettertwitter.TwitterApplication;
import com.sahilda.bettertwitter.adapters.EndlessRecyclerViewScrollListener;
import com.sahilda.bettertwitter.adapters.TweetAdapter;
import com.sahilda.bettertwitter.apis.TwitterClient;
import com.sahilda.bettertwitter.models.Tweet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class TweetsListFragment extends Fragment implements TweetAdapter.TweetAdapterListener {

    protected TwitterClient client;
    protected TweetAdapter tweetAdapter;
    protected List<Tweet> tweets;
    protected RecyclerView rvTweets;
    protected View view;
    protected EndlessRecyclerViewScrollListener scrollListener;
    protected SwipeRefreshLayout swipeContainer;
    protected long currentMinId = Long.MAX_VALUE - 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragments_tweets_list, container, false);
        setupRecyclerView();
        setupScrollListener();
        setupSwipeRefresh();
        setupSwipeRefreshListener();
        return view;
    }

    private void setupRecyclerView() {
        rvTweets = (RecyclerView) view.findViewById(R.id.rvTweet);
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(tweets, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTweets.setLayoutManager(linearLayoutManager);
        rvTweets.setAdapter(tweetAdapter);
        /*ItemClickSupport.addTo(rvTweets).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent i = new Intent(getContext(), TweetDetailActivity.class);
                        i.putExtra("tweet", tweets.get(position));
                        startActivity(i);
                    }
                }
        );*/
    }

    private void setupSwipeRefresh() {
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(
                R.color.colorTwitterLogoBlue,
                R.color.colorWhite,
                R.color.colorGrey
        );
    }

    abstract void setupSwipeRefreshListener();

    abstract void setupScrollListener();

    public long addItems(JSONArray response) {
        Log.d("TwitterClient", response.toString());
        long currentMinId = Long.MAX_VALUE - 1;
        for (int i = 0; i < response.length(); i++) {
            Tweet tweet = null;
            try {
                tweet = Tweet.fromJson(response.getJSONObject(i));
                currentMinId = getMinId(tweet, currentMinId);
                tweet.user.save();
                tweet.save();
                tweets.add(tweet);
                tweetAdapter.notifyItemChanged(tweets.size() - 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return currentMinId;
    }

    public void handleError(Throwable throwable, JSONObject errorResponse) {
        if (errorResponse != null) {
            Log.d("TwitterClient", errorResponse.toString());
        }
        if (tweets.size() == 0) {
            tweets.addAll(getTweetsFromDB());
            tweetAdapter.notifyDataSetChanged();
        }
        Toast.makeText(getContext(), "API Error", Toast.LENGTH_SHORT).show();
        throwable.printStackTrace();
    }

    private List<Tweet> getTweetsFromDB() {
        List<Tweet> tweets = SQLite.select()
                .from(Tweet.class)
                .queryList();
        return tweets;
    }

    private long getMinId(Tweet tweet, long currentMinId) {
        if (tweet.id < currentMinId) {
            return tweet.id - 1;
        }
        return currentMinId;
    }

    protected void resetScrollState() {
        tweets.clear();
        tweetAdapter.notifyDataSetChanged();
        currentMinId = Long.MAX_VALUE - 1;
        scrollListener.resetState();
    }

    @Override
    public void onItemSelected(View view, int position) {
        Tweet tweet = tweets.get(position);
        Toast.makeText(getContext(), tweet.body, Toast.LENGTH_SHORT).show();
    }

}
