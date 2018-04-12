package com.roza.android.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by hiddenpik on 25.03.2018.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.roza.android.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();

        public static final String TABLE_NAME = "movies";

        public static final String MOVIE_ID = "movieId";

        public static final String MOVIE_TITLE = "movieTitle";






    }
}
