package com.roza.android.popularmovies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.roza.android.popularmovies.utilities.JsonUtils;
import com.roza.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String orderPopular = "popular";
    String orderVoteAverage = "top_rated";
    String SORT_ORDER = orderPopular;

    static private MovieAdapter movieAdapter;
    private static GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        gridView = findViewById(R.id.grid_view);

        loadMovieData();


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);


                Movie movie = movieAdapter.getItem(position);
                intent.putExtra("movie", movie);

                //https://www.101apps.co.za/index.php/articles/using-android-s-parcelable-class-a-tutorial.html I use this tutorial for parcelable implementation


                startActivity(intent);

            }
        });


    }

    private void loadMovieData() {
        new MovieTask(this).execute();
    }

    public class MovieTask extends AsyncTask<String, Void, List<Movie>> {
        ProgressDialog progressDialog;
        private Context context;
        List<Movie> parsedList;

        public MovieTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<Movie> doInBackground(String... strings) {


            URL movieUrl = NetworkUtils.buildURL(SORT_ORDER);

            Log.i("MainActivity", "movieUrl" + movieUrl);

            try {
                String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(movieUrl);
                parsedList = JsonUtils.parseJson(jsonMovieResponse);
                Log.i("MainActivity", "Json list parsed");
                return parsedList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading movies...");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(List<Movie> list) {


            movieAdapter = new MovieAdapter(MainActivity.this, list);
            progressDialog.cancel();
            if (list != null && !list.equals("")) {
                gridView.setAdapter(movieAdapter);
                movieAdapter.notifyDataSetChanged();
                Log.i("Main Activity", "gridView set");
            } else {
                Toast toast = Toast.makeText(MainActivity.this, "No internet connection", Toast.LENGTH_LONG);
                toast.show();
            }

        }
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
            default:
                return super.onOptionsItemSelected(item);


        }
    }

}
