package com.sahilda.flickster.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    protected static class ViewHolder {
        @Nullable @BindView(R.id.tvTitle) protected TextView title;
        @Nullable @BindView(R.id.tvOverview) protected TextView overview;
        @BindView(R.id.ivMovieImage) protected ImageView movieImage;
        @Nullable @BindView(R.id.ivProgress) protected ImageView progress;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
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
            convertView = getInflatedLayoutForType(type);
            viewHolder = new ViewHolder(convertView);
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
