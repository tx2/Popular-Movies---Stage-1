package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.model.MoviesListType;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by jorgemendes on 04/02/17.
 */

public final class NetworkUtils {

    private static final String TMDB_API_KEY = "YOUR_API_KEY";

    private static final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie";

    private static final String POPULAR_MOVIES_URL = TMDB_BASE_URL + "/popular";

    private static final String TOP_MOVIES_URL = TMDB_BASE_URL + "/top_rated";

    private static final String API_KEY_PARAM = "api_key";

    private static final String TMDB_BASE_IMAGES_URL = "https://image.tmdb.org/t/p/";

    /**
     * Builds the URL used to TMDb server using the type of listing required.
     *
     * @param type The type of movie listing to fetch.
     * @return The URL to use to query the TMDb server.
     */
    public static URL buildUrl(MoviesListType type) {
        String stringUrl = "";
        switch (type) {
            case MostPopular:
                stringUrl = POPULAR_MOVIES_URL;
                break;
            case TopRated:
                stringUrl = TOP_MOVIES_URL;
                break;
        }

        Uri builtUri = Uri.parse(stringUrl)
                .buildUpon()
                .appendQueryParameter(API_KEY_PARAM, TMDB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading.
     */
    public static String getResponseFromHTTPUrl(URL url) throws IOException
    {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            return hasInput ? scanner.next() : null;
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * This method returns the base url of poster image with the appropriate size based on the
     * number of movies displayed by row.
     *
     * @param context Context of application.
     * @return Base url for poster images.
     */
    public static String getPosterBaseUrl(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        double halfWidth = metrics.widthPixels / (double)MainActivity.LAYOUT_SPAN_COUNT;

        String imageW = "original";
        if (halfWidth <= 92)
            imageW = "w92";
        else if (halfWidth <= 154)
            imageW = "w154";
        else if (halfWidth <= 185)
            imageW = "w185";
        else if (halfWidth <= 342)
            imageW = "w342";
        else if (halfWidth <= 500)
            imageW = "w500";
        else if (halfWidth <= 780)
            imageW = "w780";

        return TMDB_BASE_IMAGES_URL + imageW;
    }
}
