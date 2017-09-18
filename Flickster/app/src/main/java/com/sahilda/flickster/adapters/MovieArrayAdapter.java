package com.sahilda.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sahilda.flickster.R;
import com.sahilda.flickster.models.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

import static com.sahilda.flickster.R.id.ivMovieImage;
import static com.sahilda.flickster.R.id.ivProgress;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static class ViewHolder {
        protected TextView title;
        protected TextView overview;
        protected ImageView movieImage;
        protected ImageView progress;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public int getViewTypeCount() {
        return Movie.PopularValues.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).popular.ordinal();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        // get the data item for position
        Movie movie = getItem(position);
        int type = getItemViewType(position);
        // check if the existing view is being reused
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = getInflatedLayoutForType(type);
            setupConvertView(type, convertView, viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        setupViewHolder(type, viewHolder, movie);

        // return the view
        return convertView;
    }

    private View getInflatedLayoutForType(int type) {
        if (type == Movie.PopularValues.POPULAR.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie_popular, null);
        } else if (type == Movie.PopularValues.NOT_POPULAR.ordinal()) {
            return LayoutInflater.from(getContext()).inflate(R.layout.item_movie, null);
        } else {
            return null;
        }
    }

    private void setupConvertView(int type, View view, ViewHolder viewHolder) {
        viewHolder.movieImage = (ImageView) view.findViewById(ivMovieImage);
        viewHolder.title = (TextView) view.findViewById(R.id.tvTitle);
        viewHolder.overview = (TextView) view.findViewById(R.id.tvOverview);
        viewHolder.progress = (ImageView) view.findViewById(ivProgress);
    }

    private void setupViewHolder(int type, ViewHolder viewHolder, Movie movie) {
        if (type == Movie.PopularValues.POPULAR.ordinal()) {
            setupPopularViewHolder(viewHolder, movie);
        } else if (type == Movie.PopularValues.NOT_POPULAR.ordinal()) {
            setupStandardViewHolder(viewHolder, movie);
        }
    }

    private void setupPopularViewHolder(ViewHolder viewHolder, Movie movie) {
        viewHolder.movieImage.setImageResource(0);
        loadImage(movie.getBackdropPath(), viewHolder);
    }

    private void setupStandardViewHolder(ViewHolder viewHolder, Movie movie) {
        viewHolder.title.setText(movie.getOriginalTitle());
        viewHolder.overview.setText(movie.getOverview());
        viewHolder.movieImage.setImageResource(0);

        int orientation = getContext().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            loadImage(movie.getPosterPath(), viewHolder);
        } else {
            loadImage(movie.getBackdropPath(), viewHolder);
        }
    }

    private void loadImage(final String path, final ViewHolder viewHolder) {
        viewHolder.progress.setVisibility(View.VISIBLE);
        viewHolder.progress.setImageResource(R.drawable.progress_animation);
        Picasso.with(getContext())
                .load(path)
                .transform(new RoundedCornersTransformation(10,10))
                .fit()
                .into(viewHolder.movieImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (viewHolder.progress != null) {
                            viewHolder.progress.setImageResource(0);
                            viewHolder.progress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError() {
                        Log.d("Image Error", "error loading image for " + path);
                    }
                });
    }

}
