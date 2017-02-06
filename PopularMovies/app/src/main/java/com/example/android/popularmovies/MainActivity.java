package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.interfaces.ListItemClickListener;
import com.example.android.popularmovies.model.Movie;
import com.example.android.popularmovies.model.MoviesListType;
import com.example.android.popularmovies.utilities.MoviesJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements ListItemClickListener {

    public static final int LAYOUT_SPAN_COUNT = 2;

    private static final String SAVE_MOVIE_SELECTION = "movie_selection";
    private static final String SAVE_MOVIES = "movies";

    private TextView mErrorLoadingTextView;
    private ProgressBar mProgressBar;

    private RecyclerView mMoviesList;
    private MovieAdapter mMoviesAdapter;

    private MoviesListType mSelectedListType;
    private Movie[] mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mErrorLoadingTextView = (TextView) findViewById(R.id.tv_error_message_display);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);

        GridLayoutManager layoutManager = new GridLayoutManager(this, LAYOUT_SPAN_COUNT);
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.setHasFixedSize(true);

        if (savedInstanceState == null || !savedInstanceState.containsKey(SAVE_MOVIES)) {
            mSelectedListType = MoviesListType.MostPopular;
            loadMovies();
        } else {
            mSelectedListType = (MoviesListType) savedInstanceState.getSerializable(SAVE_MOVIE_SELECTION);
            configureMoviesList((Movie[]) savedInstanceState.getParcelableArray(SAVE_MOVIES));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SAVE_MOVIE_SELECTION, mSelectedListType);
        outState.putParcelableArray(SAVE_MOVIES, mMovies);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        switch (mSelectedListType) {
            case MostPopular:
                MenuItem mostPopularItem = menu.findItem(R.id.most_popular_action);
                mostPopularItem.setChecked(true);
                break;
            case TopRated:
                MenuItem topRatedItem = menu.findItem(R.id.top_rated_action);
                topRatedItem.setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_action:
                loadMovies();
                return true;
            case R.id.most_popular_action:
                if (!item.isChecked()) {
                    mSelectedListType = MoviesListType.MostPopular;
                    loadMovies();
                }
                item.setChecked(true);
                return true;
            case R.id.top_rated_action:
                if (!item.isChecked()) {
                    mSelectedListType = MoviesListType.TopRated;
                    loadMovies();
                }
                item.setChecked(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadMovies() {
        NetworkInfo netInfo = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
            new FetchMoviesTask().execute(mSelectedListType);
        else
            showErrorMessage();
    }

    private void configureMoviesList(Movie[] movies) {
        if (movies != null && movies.length > 0) {
            mMovies = movies;
            showMoviesList();
            mMoviesAdapter = new MovieAdapter(mMovies, this);
            mMoviesList.setAdapter(mMoviesAdapter);
        } else {
            showErrorMessage();
        }
    }

    private void showMoviesList() {
        mErrorLoadingTextView.setVisibility(View.INVISIBLE);
        mMoviesList.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mMoviesList.setVisibility(View.INVISIBLE);
        mErrorLoadingTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if (mMovies != null && mMovies.length > clickedItemIndex) {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_MOVIE, mMovies[clickedItemIndex]);
            startActivity(intent);
        }
    }

    private class FetchMoviesTask extends AsyncTask<MoviesListType, Void, Movie[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mErrorLoadingTextView.setVisibility(View.INVISIBLE);
            mMoviesList.setVisibility(View.INVISIBLE);
        }

        @Override
        protected Movie[] doInBackground(MoviesListType... moviesListTypes) {
            if (moviesListTypes[0] == null)
                return null;

            try {
                URL moviesRequestUrl = NetworkUtils.buildUrl(moviesListTypes[0]);
                String jsonMoviesResponse = NetworkUtils.getResponseFromHTTPUrl(moviesRequestUrl);

                Movie[] movies = MoviesJsonUtils.getMoviesFromJson(jsonMoviesResponse, MainActivity.this);
                return movies;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movies != null && movies.length > 0) {
                configureMoviesList(movies);
            } else {
                mMovies = null;
                showErrorMessage();
            }
        }
    }
}
