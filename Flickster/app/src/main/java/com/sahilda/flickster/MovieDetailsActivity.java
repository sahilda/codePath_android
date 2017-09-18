package com.sahilda.flickster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sahilda.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;
    @BindView(R.id.ivMovieImage) protected ImageView ivMovieImage;
    @BindView(R.id.tvTitle) protected TextView tvTitle;
    @BindView(R.id.tvOverview) protected TextView tvOverview;
    @BindView(R.id.tvPopularity) protected TextView tvPopularity;
    @BindView(R.id.rbVoteAverage) protected RatingBar rbVoteAverage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = (Movie) getIntent().getSerializableExtra("movie");
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        tvTitle.setText(movie.getOriginalTitle());
        tvOverview.setText(movie.getOverview());
        DecimalFormat df = new DecimalFormat("#.00");
        tvPopularity.setText("Popularity: " + df.format(movie.getPopularity()));
        rbVoteAverage.setRating((float) movie.getVoteAverage());
        loadImage(movie.getBackdropPath());
    }

    private void loadImage(final String path) {
        Picasso.with(getApplicationContext())
                .load(path)
                .transform(new RoundedCornersTransformation(10,10))
                .fit()
                .into(ivMovieImage);
    }

}
