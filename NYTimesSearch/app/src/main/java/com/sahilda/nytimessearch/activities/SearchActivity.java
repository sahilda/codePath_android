package com.sahilda.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private EditText etQuery;
    private GridView gvResults;
    private Button btnSearch;

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
        etQuery = (EditText) findViewById(R.id.etQuery);
        gvResults = (GridView) findViewById(R.id.gvResults);
        btnSearch = (Button) findViewById(R.id.btnSearch);
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
                loadNextDataFromApi(page);
                return true;
            }
        };
        gvResults.setOnScrollListener(scrollListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
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

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();
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
            }
        });
    }

    public void loadNextDataFromApi(int offset) {
        mSearchQuery.setPage(offset);
        Toast.makeText(this, "" + offset, Toast.LENGTH_SHORT).show();
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
            }
        });

    }

}
