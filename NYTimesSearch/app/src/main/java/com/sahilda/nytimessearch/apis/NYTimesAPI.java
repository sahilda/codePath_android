package com.sahilda.nytimessearch.apis;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sahilda.nytimessearch.models.SearchQuery;

public class NYTimesAPI {

    private static AsyncHttpClient client = new AsyncHttpClient();
    private static String apiKey = "e2ebf58384d14c0992a2f44decb059db";

    public static void getArticles(SearchQuery searchQuery, JsonHttpResponseHandler responseHandler) {
        String url = "https://api.nytimes.com/svc/search/v2/articlesearch.json";

        RequestParams params = new RequestParams();
        params.put("api-key", apiKey);
        params.put("page", searchQuery.getPage());
        params.put("q", searchQuery.getQuery());

        if (searchQuery.getBeginDate() != null) {
            params.put("begin_date", searchQuery.getBeginDate());
        }
        if (searchQuery.getNewsDeskType().size() != 0) {
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < searchQuery.getNewsDeskType().size(); i++ ) {
                String type = searchQuery.getNewsDeskType().get(i).getType();
                if (i == 0) {
                    sb.append("\"" + type + "\"");
                } else {
                    sb.append(" \"" + type + "\"");
                }
            }
            sb.append(")");
            params.put("news_desk", sb.toString());
        }
        if (searchQuery.getSortType() != null) {
            params.put("sort", searchQuery.getSortType().getType());
        }

        client.get(url, params, responseHandler);
    }

}
