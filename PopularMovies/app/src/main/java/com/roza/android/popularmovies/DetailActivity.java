package com.roza.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.roza.android.popularmovies.utilities.NetworkUtils;
import com.roza.android.popularmovies.utilities.VideosJsonUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;


public class DetailActivity extends Activity {

    ImageView posterView;
    TextView titleTv;
    TextView userRatingTv;
    TextView releaseDateTv;
    TextView overviewTv;
    String PosterUrlString;
    String StringId;

    ListView trailersLstView;

    private static VideoAdapter videoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        trailersLstView = findViewById(R.id.listView);
        configureClickListener();
        trailersLstView.setClickable(true);

        loadVideoData();




        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");







        String title = movie.getTitle();
        String overview = movie.getOverview();
        double userRating = movie.getUserRating();
        String releaseDate = movie.getReleaseDate();
        String posterPath = movie.getPoster();
        int id = movie.getId();
        StringId = String.valueOf(id);;


        Log.i("DetailActivity.java", "" + title + + '\n' + overview + '\n' + userRating + '\n' + releaseDate + '\n' + posterPath + '\n' + id);

        PosterUrlString = "http://image.tmdb.org/t/p/w185" + posterPath;

        titleTv = findViewById(R.id.movie_title_tv);
        titleTv.setText(title);

        userRatingTv = findViewById(R.id.user_rating_tv);
        userRatingTv.setText(Double.toString(userRating));

        releaseDateTv = findViewById(R.id.release_date_tv);
        releaseDateTv.setText(releaseDate);

        overviewTv = findViewById(R.id.overview_tv);
        overviewTv.setText(overview);

        posterView = findViewById(R.id.imageView);

        Picasso.with(this).load(PosterUrlString).into(posterView);









    }

    private void configureClickListener() {
        trailersLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Log.i("DetailActivity", "click " + position);
//                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
//
//
//                Movie movie = movieAdapter.getItem(position);
//                intent.putExtra("movie", movie);
//
//                //https://www.101apps.co.za/index.php/articles/using-android-s-parcelable-class-a-tutorial.html I use this tutorial for parcelable implementation
//
//
//                startActivity(intent);

                Video video = videoAdapter.getItem(position);
                String key = video.getKey();
                String youtubeBaseUri = "http://youtu.be/";

                Log.i("DetailActivity", "" + youtubeBaseUri + key);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtubeBaseUri + key));

                Intent choser = Intent.createChooser(intent, "title");





                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(choser);
                }
            }

        });
    }


    /*
     I used code from this site in my method:
     https://stackoverflow.com/questions/40861136/set-listview-height-programmatically
     */

    public static void updateListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int adapterCount = listAdapter.getCount();
        for (int size = 0; size < adapterCount; size++) {
            View listItem = listAdapter.getView(size, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (totalHeight
                + (listView.getDividerHeight() * (adapterCount)) + 2);
        listView.setLayoutParams(params);
    }

    private void loadVideoData() { new videoTask(this).execute(); }

    public class videoTask extends AsyncTask<String, Void, List<Video>> {

        private Context context;

        public videoTask(Context context) {
            this.context = context;
        }

        List<Video> parsedList;


        @Override
        protected List<Video> doInBackground(String... strings) {

            URL videosUrl = NetworkUtils.buildURL("videos", StringId);

            try {

                String jsonVideoResponse = NetworkUtils.getResponseFromHttpUrl(videosUrl);
                parsedList = VideosJsonUtils.parseVideoJson(jsonVideoResponse);
                Log.d("DetailActivity", jsonVideoResponse);
                Log.i("DetailActivity", "list parsed "  + videosUrl);
                return parsedList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Video> videos) {

            videoAdapter = new VideoAdapter(DetailActivity.this, videos);

            if (videos != null && !videos.equals("")) {
                trailersLstView.setAdapter(videoAdapter);
                videoAdapter.notifyDataSetChanged();
                updateListViewHeight(trailersLstView);

                Log.i("DetailActivity", "Trailer ListView set");
             } else {
                Toast toast= Toast.makeText(DetailActivity.this, "No internet connection", Toast.LENGTH_LONG);
                toast.show();
            }

        }
    }



}
