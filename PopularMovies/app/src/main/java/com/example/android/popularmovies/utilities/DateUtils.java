package com.example.android.popularmovies.utilities;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jorgemendes on 05/02/17.
 */

public final class DateUtils {

    private static final String TAG = DateUtils.class.getSimpleName();

    /**
     * This method returns a year string from an input date string.
     *
     * @param dateString The date string to get the year.
     * @return Year as string.
     */
    public static String getYearFromDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date date = dateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(Calendar.YEAR) + "";
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return dateString;
        }
    }

}
