package com.sahilda.bettertwitter.apis;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sahilda.bettertwitter.R;

public class TwitterClient extends OAuthBaseClient {

	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance();
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "piK0kEPx1C3t5AkcS63Rk7lFq";
	public static final String REST_CONSUMER_SECRET = "Mm5eYuEOfNLxvaUdN76AH1on5imm3fI7222q0MaPGL1Pyhpbnc";

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}

	public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("/statuses/home_timeline.json");
		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("max_id", maxId);
		client.get(apiUrl, params, handler);
	}

	public void getCurrentUser(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("/account/verify_credentials.json");
		client.get(apiUrl, handler);
	}

	public void postTweet(String tweet, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", tweet);
		client.post(apiUrl, params, handler);
	}

}
