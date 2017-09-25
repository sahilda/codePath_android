package com.sahilda.nytimessearch.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.sahilda.nytimessearch.ArticleArrayAdapter;
import com.sahilda.nytimessearch.EndlessScrollListener;
import com.sahilda.nytimessearch.R;
import com.sahilda.nytimessearch.apis.NYTimesAPI;
import com.sahilda.nytimessearch.fragments.FiltersFragment;
import com.sahilda.nytimessearch.models.Article;
import com.sahilda.nytimessearch.models.SearchQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private GridView gvResults;
    private ArrayList<Article> mArticles;
    private ArticleArrayAdapter mAdapter;
    private SearchQuery mSearchQuery;
    private EndlessScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViewsAndVariables();
    }

    public void setupViewsAndVariables() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        mArticles = new ArrayList<>();
        mAdapter = new ArticleArrayAdapter(this, mArticles);
        mSearchQuery = new SearchQuery();
        gvResults.setAdapter(mAdapter);

        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                Article article = mArticles.get(position);
                i.putExtra("article", Parcels.wrap(article));
                startActivity(i);
            }
        });

        scrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (!isOnline()) {
                    String message = "Cannot load more data. No Internet connection detected.";
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } else {
                    loadNextDataFromApi(page);
                }
                return true;
            }
        };
        gvResults.setOnScrollListener(scrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                performSearch(query);
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filters) {
            showFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFilterDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FiltersFragment filtersFragment = FiltersFragment.newInstance(mSearchQuery);
        filtersFragment.show(fm, "fragment_filters");
    }

    private void performSearch(String query) {
        if (!isOnline()) {
            String message = "No Internet connection detected.";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } else {
            mSearchQuery.setQuery(query);
            NYTimesAPI.getArticles(mSearchQuery, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONArray articleJsonResults = null;
                    try {
                        articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                        mArticles.clear();
                        scrollListener.resetState();
                        mAdapter.addAll(Article.fromJSONArray(articleJsonResults));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("RESPONSE", errorResponse.toString());
                    try {
                        String message = errorResponse.getString("message");
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        // do nothing
                    }
                }
            });
        }
    }

    public void loadNextDataFromApi(int offset) {
        mSearchQuery.setPage(offset);
        NYTimesAPI.getArticles(mSearchQuery, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray articleJsonResults = null;
                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    mArticles.addAll(Article.fromJSONArray(articleJsonResults));
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("RESPONSE", errorResponse.toString());
                try {
                    String message = errorResponse.getString("message");
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    // do nothing
                }
            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }

}
