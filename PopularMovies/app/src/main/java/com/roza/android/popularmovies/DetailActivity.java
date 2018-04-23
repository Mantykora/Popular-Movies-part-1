package com.roza.android.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.roza.android.popularmovies.adapters.CommentAdapter;
import com.roza.android.popularmovies.adapters.VideoAdapter;
import com.roza.android.popularmovies.data.MovieContract;
import com.roza.android.popularmovies.data.MovieDbHelper;
import com.roza.android.popularmovies.models.Comment;
import com.roza.android.popularmovies.models.Movie;
import com.roza.android.popularmovies.models.Video;
import com.roza.android.popularmovies.utilities.CommentsJsonUtils;
import com.roza.android.popularmovies.utilities.NetworkUtils;
import com.roza.android.popularmovies.utilities.VideosJsonUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
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

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Comment> commentsList;

    private LikeButton likeButton;

    private static SQLiteDatabase mDb;

    private int isLiked = 0;

    private Cursor cursor;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        trailersLstView = findViewById(R.id.listView);


        configureClickListener();
        trailersLstView.setClickable(true);

        likeButton = findViewById(R.id.heart_button);

        commentsList = new ArrayList<>();

        recyclerView = findViewById(R.id.comment_rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        adapter = new CommentAdapter(commentsList);


        Intent intent = getIntent();
        movie = intent.getParcelableExtra("movie");


        final String title = movie.getTitle();
        String overview = movie.getOverview();
        double userRating = movie.getUserRating();
        String releaseDate = movie.getReleaseDate();
        String posterPath = movie.getPoster();
        final int id = movie.getId();
        StringId = String.valueOf(id);

//        if (savedInstanceState != null) {
//
//            Movie movieFromParcelable = savedInstanceState.getParcelable("movieParcelable");
//            Log.i("DetailActivity.java", "" + movieFromParcelable);
//
//            titleTv.setText(movieFromParcelable.getTitle());
//            userRatingTv.setText(Double.toString(movieFromParcelable.getUserRating()));
//            releaseDateTv.setText(movieFromParcelable.getReleaseDate());
//            overviewTv.setText(movieFromParcelable.getOverview());
//            String posterUrl = "http://image.tmdb.org/t/p/w185" + movieFromParcelable.getPoster();
//            Picasso.with(this).load(posterUrl).into(posterView);
//
//
//        }


        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                isLiked = 1;

                addMovieToDatabase(id, title, isLiked);

                likeButton.setLiked(true);

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                isLiked = 0;

                removeMovieFromDatabase();

                likeButton.setLiked(false);

            }
        });


        //Create a database
        MovieDbHelper dbHelper = new MovieDbHelper(this);

        //for adding movies
        mDb = dbHelper.getWritableDatabase();


        Log.i("DetailActivity.java", "" + title + +'\n' + overview + '\n' + userRating + '\n' + releaseDate + '\n' + posterPath + '\n' + StringId);

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




        loadVideoData();

        cursor = getMovieWithId();

        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            int cursorInt = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_LIKE));
            Log.i("detailCursor", "" + cursorInt);
            if (cursorInt == 1) likeButton.setLiked(true);

        }



    }


    public Cursor getMovieWithId() {
        Uri movieUri =
                MovieContract.MovieEntry.CONTENT_URI
                        .buildUpon()
                        .appendPath(StringId)
                        .build();

        return getContentResolver().query(
                movieUri,
                null,
                null,
                null,
                MovieContract.MovieEntry._ID);
    }


    private void configureClickListener() {
        trailersLstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Log.i("DetailActivity", "click " + position);
//
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

    private void loadVideoData() {
        new videoTask(this).execute();
        new CommentTask(this).execute();
    }

    public class CommentTask extends AsyncTask<String, Void, List<Comment>> {


        private Context context;

        public CommentTask(Context context) {
            this.context = context;
        }


        List<Comment> parsedCommentList;

        @Override
        protected List<Comment> doInBackground(String... strings) {
            URL commentsUrl = NetworkUtils.buildURL("reviews", StringId);

            try {

                String jsonCommentResponse = NetworkUtils.getResponseFromHttpUrl(commentsUrl);
                parsedCommentList = CommentsJsonUtils.parseCommentJson(jsonCommentResponse);
                Log.d("DetailActivity", jsonCommentResponse);
                Log.i("DetailActivity", "list parsed " + commentsUrl);
                return parsedCommentList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {

            if (comments != null && !comments.equals("")) {
                adapter = new CommentAdapter(comments);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


                Log.i("DetailActivity", "RecyclerView set");
            } else {
                Toast toast = Toast.makeText(DetailActivity.this, "No internet connection", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }


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
                Log.i("DetailActivity", "list parsed " + videosUrl);
                return parsedList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(List<Video> videos) {


            if (videos != null && !videos.equals("")) {
                videoAdapter = new VideoAdapter(DetailActivity.this, videos);
                trailersLstView.setAdapter(videoAdapter);
                videoAdapter.notifyDataSetChanged();
                updateListViewHeight(trailersLstView);

                Log.i("DetailActivity", "Trailer ListView set");
            } else {
                Toast toast = Toast.makeText(DetailActivity.this, "No internet connection", Toast.LENGTH_LONG);
                toast.show();
            }

        }
    }


    private void addMovieToDatabase(int movieId, String movieTitle, int movieLike) {

        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry._ID, movieId);
        cv.put(MovieContract.MovieEntry.MOVIE_TITLE, movieTitle);
        cv.put(MovieContract.MovieEntry.MOVIE_LIKE, movieLike);

        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, cv);



    }


    private int removeMovieFromDatabase() {

        Uri uri = MovieContract.MovieEntry.CONTENT_URI
                .buildUpon()
                .appendPath(StringId)
                .build();

        return getContentResolver().delete(uri, null, null);


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("movieParcelable", movie);
        Log.i("DetailActivity", "" + movie);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        Movie movieFromParcelable = savedInstanceState.getParcelable("movieParcelable");
        Log.i("DetailActivity.java", "retore movie: " + movieFromParcelable);

        titleTv.setText(movieFromParcelable.getTitle());
        userRatingTv.setText(Double.toString(movieFromParcelable.getUserRating()));
        releaseDateTv.setText(movieFromParcelable.getReleaseDate());
        overviewTv.setText(movieFromParcelable.getOverview());
        String posterUrl = "http://image.tmdb.org/t/p/w185" + movieFromParcelable.getPoster();
       Picasso.with(this).load(posterUrl).into(posterView);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("DetailActivity", "restart!");
    }


}
