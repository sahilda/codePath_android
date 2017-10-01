package com.sahilda.bettertwitter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.sahilda.bettertwitter.apis.TwitterApplication;
import com.sahilda.bettertwitter.apis.TwitterClient;
import com.sahilda.bettertwitter.models.Tweet;
import com.sahilda.bettertwitter.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    private TwitterClient client;
    private User user;
    private Tweet tweet;
    private ImageView imCancel;
    private ImageView imProfile;
    private TextView tvName;
    private TextView tvUserName;
    private EditText etTweetText;
    private ImageView imSubmit;
    private TextView tvCharacterCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        user = (User) getIntent().getSerializableExtra("user");
        client = TwitterApplication.getRestClient();
        setupToolbar();
        setupViews();
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);
    }

    public void setupViews() {
        imCancel = (ImageView) findViewById(R.id.imCancel);
        imProfile = (ImageView) findViewById(R.id.imProfile);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        etTweetText = (EditText) findViewById(R.id.etTweetText);
        imSubmit = (ImageView) findViewById(R.id.imSubmit);
        tvCharacterCount = (TextView) findViewById(R.id.tvCharacterCount);

        tvName.setText(user.name);
        tvUserName.setText(user.screenName);
        Glide.with(this).load(user.profileImageUrl)
                .into(imProfile);
        imCancel.setOnClickListener(hitCancel);
        imSubmit.setOnClickListener(hitSubmit);
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

    private View.OnClickListener hitSubmit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String text = etTweetText.getText().toString();
            client.postTweet(text, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        Log.d("TwitterClient", response.toString());
                        tweet = Tweet.fromJson(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("TwitterClient", errorResponse.toString());
                    throwable.printStackTrace();
                }
            });
        }
    };

    private View.OnClickListener hitCancel = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

}
