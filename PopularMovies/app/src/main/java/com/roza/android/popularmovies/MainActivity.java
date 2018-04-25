package com.roza.android.popularmovies;


import android.os.Parcelable;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.roza.android.popularmovies.adapters.MovieAdapter;
import com.roza.android.popularmovies.models.Movie;
import com.roza.android.popularmovies.utilities.MovieJsonUtils;
import com.roza.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<ArrayList<Movie>> {

    String orderPopular = "popular";
    String orderVoteAverage = "top_rated";
    String SORT_ORDER = orderPopular;
    String MOVIE_ID = "";
    ArrayList<Movie> moviesForPacelable;

    static private MovieAdapter movieAdapter;


    private static GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gridView = findViewById(R.id.grid_view);


        loadMovieData();



        getSupportLoaderManager().initLoader(0, null, this).forceLoad();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);


                Movie movie = movieAdapter.getItem(position);
                intent.putExtra("movie", movie);

                //https://www.101apps.co.za/index.php/articles/using-android-s-parcelable-class-a-tutorial.html I used this tutorial for parcelable implementation


                startActivity(intent);

            }
        });


    }

    private void loadMovieData() {
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int i, Bundle bundle) {


        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            ArrayList<Movie> parsedList;

            @Override
            public ArrayList<Movie> loadInBackground() {

                Log.d("MainActivity", "Load in background");
                URL movieUrl = NetworkUtils.buildURL(SORT_ORDER, MOVIE_ID);

                Log.i("MainActivity", "movieUrl" + movieUrl);

                try {
                    String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                    parsedList = MovieJsonUtils.parseJson(jsonMovieResponse);
                    Log.i("MainActivity", "Json list parsed");
                    return parsedList;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }

            }
        };


    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {

        movieAdapter = new MovieAdapter(MainActivity.this, movies);
        if (movies != null && !movies.equals("")) {
            gridView.setAdapter(movieAdapter);
            movieAdapter.notifyDataSetChanged();
            Log.i("Main Activity", "gridView set");
        } else {
            Toast toast = Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG);
            toast.show();
        }
        moviesForPacelable = movies;
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_popularity:
                SORT_ORDER = orderPopular;
                loadMovieData();
                return true;
            case R.id.sort_vote_average:
                SORT_ORDER = orderVoteAverage;
                loadMovieData();
                return true;
            case R.id.favourites:
                Intent intent = new Intent(MainActivity.this, FavouritesActivity.class);
                startActivity(intent);

                return true;
            default:
                return super.onOptionsItemSelected(item);


        }



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("movies", moviesForPacelable);
        outState.putInt("moviesPosition", gridView.getFirstVisiblePosition());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        moviesForPacelable = savedInstanceState.getParcelableArrayList("movies");
        int moviesPosition = savedInstanceState.getInt("moviesPosition");
        Log.i("MainActivity", "Saved instance state" + moviesForPacelable);
        MovieAdapter adapter = new MovieAdapter(MainActivity.this, moviesForPacelable);
        if (adapter != null) {
            Log.i("MainActivity","" + moviesPosition);
            gridView.setAdapter(adapter);
            gridView.setSelection(moviesPosition);


        }

    }
}
