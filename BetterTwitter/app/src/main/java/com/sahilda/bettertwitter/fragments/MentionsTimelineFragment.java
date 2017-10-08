package com.sahilda.bettertwitter.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MentionsTimelineFragment extends TweetsListFragment {

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateTimeline();
    }

    protected void populateTimeline() {
        client.getMentionsTimeline(currentMinId, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                currentMinId = addItems(response);
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                handleError(throwable, errorResponse);
            }
        });
    }

}
