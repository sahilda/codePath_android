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
        TextView title;
        TextView overview;
        ImageView movieImage;
        ImageView progress;
    }

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int orientation = getContext().getResources().getConfiguration().orientation;

        // get the data item for position
        Movie movie = getItem(position);

        ViewHolder viewHolder;
        // check if the existing view is being reused
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder.movieImage = (ImageView) convertView.findViewById(ivMovieImage);
            viewHolder.title = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.overview = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.progress = (ImageView) convertView.findViewById(ivProgress);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(movie.getOriginalTitle());
        viewHolder.overview.setText(movie.getOverview());
        viewHolder.movieImage.setImageResource(0);
        viewHolder.progress.setImageResource(R.drawable.progress_animation);

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            loadImage(movie.getPosterPath(), viewHolder);
        } else {
            loadImage(movie.getBackdropPath(), viewHolder);
        }

        // return the view
        return convertView;
    }

    private void loadImage(final String path, final ViewHolder viewHolder) {
        viewHolder.progress.setVisibility(View.VISIBLE);
        Picasso.with(getContext())
                .load(path)
                .transform(new RoundedCornersTransformation(10,10))
                .fit()
                .into(viewHolder.movieImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (viewHolder.progress != null) {
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
