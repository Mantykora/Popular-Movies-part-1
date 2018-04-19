package com.roza.android.popularmovies.utilities;

import com.roza.android.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class MovieJsonUtils {

    public static List<Movie> parseJson(String json) throws JSONException {

        final String ORIGINAL_TITLE = "original_title";
        final String MOVIE_POSTER = "poster_path";
        final String PLOT_OVERVIEW = "overview";
        final String USER_RATING = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String MOVIE_ID = "id";

        final String RESULTS = "results";

        JSONObject allMoviesObject = new JSONObject(json);

        JSONArray resultsArray = allMoviesObject.getJSONArray(RESULTS);

        final List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < resultsArray.length(); i++) {

            Movie movie = new Movie();

            JSONObject movieObject = resultsArray.getJSONObject(i);
            movie.setTitle(movieObject.getString(ORIGINAL_TITLE));
            movie.setPoster(movieObject.getString(MOVIE_POSTER));
            movie.setOverview(movieObject.getString(PLOT_OVERVIEW));
            movie.setUserRating(movieObject.optDouble(USER_RATING));
            movie.setReleaseDate(movieObject.getString(RELEASE_DATE));
            movie.setId(movieObject.getInt(MOVIE_ID));

            movies.add(movie);
        }


        return movies;
    }
}
