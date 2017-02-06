package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.util.Log;

import com.example.android.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by jorgemendes on 04/02/17.
 */

public final class MoviesJsonUtils {

    private static final String TAG = MoviesJsonUtils.class.getSimpleName();

    /**
     * This method parses JSON from a web response and returns an array of Movie objects.
     *
     * @param moviesJsonString JSON response from server.
     * @param context Context of application.
     * @return Array of Movie objects.
     * @throws JSONException If JSON data cannot be properly parsed.
     */
    public static Movie[] getMoviesFromJson(String moviesJsonString, Context context)
            throws JSONException {
        final String TMDB_STATUS_CODE = "status_code";
        final String TMDB_STATUS_MESSAGE = "status_message";
        final String TMDB_RESULTS = "results";

        JSONObject moviesJson = new JSONObject(moviesJsonString);

        /* Is there an error? */
        if (moviesJson.has(TMDB_STATUS_CODE)) {
            int errorCode = moviesJson.getInt(TMDB_STATUS_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                case HttpURLConnection.HTTP_NOT_FOUND:
                default:
                    Log.e(TAG, moviesJson.getString(TMDB_STATUS_MESSAGE));
                    return null;
            }
        }

        JSONArray moviesArray = moviesJson.getJSONArray(TMDB_RESULTS);
        Movie[] parsedMovies = new Movie[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieJson = moviesArray.getJSONObject(i);
            parsedMovies[i] = getMovieFromJson(movieJson, context);
        }

        return parsedMovies;
    }

    /**
     * This method parses a Movie JSONObject to a Movie object.
     *
     * @param movieJson JSON object to parse.
     * @param context Context of application.
     * @return Movie object.
     * @throws JSONException If JSON data cannot be properly parsed.
     */
    private static Movie getMovieFromJson(JSONObject movieJson, Context context) throws JSONException {
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_ID = "id";
        final String MOVIE_ORIGINAL_TITLE = "original_title";
        final String MOVIE_VOTE_AVG = "vote_average";

        Movie movie = new Movie();

        if (movieJson.has(MOVIE_ID))
            movie.id = movieJson.getInt(MOVIE_ID);

        if (movieJson.has(MOVIE_POSTER_PATH) && movieJson.getString(MOVIE_POSTER_PATH) != null)
            movie.posterUrl = NetworkUtils.getPosterBaseUrl(context) + movieJson.getString(MOVIE_POSTER_PATH);

        if (movieJson.has(MOVIE_ORIGINAL_TITLE))
            movie.originalTitle = movieJson.getString(MOVIE_ORIGINAL_TITLE);

        if (movieJson.has(MOVIE_RELEASE_DATE))
            movie.releaseDate = movieJson.getString(MOVIE_RELEASE_DATE);

        if (movieJson.has(MOVIE_OVERVIEW))
            movie.overview = movieJson.getString(MOVIE_OVERVIEW);

        if (movieJson.has(MOVIE_VOTE_AVG))
            movie.voteAverage = movieJson.getDouble(MOVIE_VOTE_AVG);

        return movie;
    }

}
