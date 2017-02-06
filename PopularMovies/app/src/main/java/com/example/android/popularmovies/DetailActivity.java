package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.utilities.DateUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie";

    private TextView mTitleTextView;
    private ImageView mPosterImageView;
    private TextView mYearTextView;
    private TextView mRateTextView;
    private TextView mSynopsisTextView;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleTextView = (TextView) findViewById(R.id.tv_movie_title);
        mPosterImageView = (ImageView) findViewById(R.id.iv_movie_poster);
        mYearTextView = (TextView) findViewById(R.id.tv_movie_year);
        mRateTextView = (TextView) findViewById(R.id.tv_movie_rate);
        mSynopsisTextView = (TextView) findViewById(R.id.tv_movie_synopsis);

        Intent startingIntent = getIntent();
        if (startingIntent.hasExtra(EXTRA_MOVIE)) {
            mMovie = startingIntent.getParcelableExtra(EXTRA_MOVIE);

            populateData();
        }
    }

    private void populateData() {
        mTitleTextView.setText(mMovie.originalTitle);

        Picasso.with(this)
                .load(mMovie.posterUrl)
                .into(mPosterImageView);

        mYearTextView.setText(DateUtils.getYearFromDate(mMovie.releaseDate));
        mRateTextView.setText(mMovie.voteAverage + "/10");
        mSynopsisTextView.setText(mMovie.overview);
    }
}
