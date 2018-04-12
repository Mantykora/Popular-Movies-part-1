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
import com.roza.android.popularmovies.data.MovieContract;
import com.roza.android.popularmovies.data.MovieDbHelper;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        trailersLstView = findViewById(R.id.listView);


        configureClickListener();
        trailersLstView.setClickable(true);

        likeButton = findViewById(R.id.heart_button);

        commentsList = new ArrayList<>();

        recyclerView =  findViewById(R.id.comment_rv);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);



        adapter = new CommentRecyclerViewAdapter(commentsList);











        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");







        final String title = movie.getTitle();
        String overview = movie.getOverview();
        double userRating = movie.getUserRating();
        String releaseDate = movie.getReleaseDate();
        String posterPath = movie.getPoster();
        final int id = movie.getId();
        StringId = String.valueOf(id);;

        //TODO jak już coś kurwa zalajkujesz serce powinno zostać czerwone a nie wracać po wróceniu do listy filmow do stanu pierwotnego. Zczytać z bazy czy jaki chuj
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                addMovieToDatabase(id, title);

                likeButton.setLiked(true);

            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        //Create a database
        MovieDbHelper dbHelper = new MovieDbHelper(this);

        //for adding movies
        mDb = dbHelper.getWritableDatabase();

        Log.i("DetailActivity.java", "" + title + + '\n' + overview + '\n' + userRating + '\n' + releaseDate + '\n' + posterPath + '\n' + StringId);

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

    private void loadVideoData() { new videoTask(this).execute();
    new CommentTask(this).execute();}

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
                Log.i("DetailActivity", "list parsed "  + commentsUrl);
                return parsedCommentList;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Comment> comments) {

            if (comments != null && !comments.equals(""))
            {
                adapter = new CommentRecyclerViewAdapter(comments);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


                Log.i("DetailActivity", "RecyclerView set");
            } else {
                Toast toast= Toast.makeText(DetailActivity.this, "No internet connection", Toast.LENGTH_LONG);
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
                Log.i("DetailActivity", "list parsed "  + videosUrl);
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
                Toast toast= Toast.makeText(DetailActivity.this, "No internet connection", Toast.LENGTH_LONG);
                toast.show();
            }

        }
    }


    //TODO 1 jeśli lista jest pusta zeby nie wyjebało aplikcji
    //query mDb to get favourites from the table
//    public static boolean isCursorEmpty() {
//        if (cursor != null) {
//            return true;
//        }
//        else {
//            return false;
//        }
//    }

    public static Cursor getAllFavourites() {

        return mDb.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MovieContract.MovieEntry._ID);
    }


    private long addMovieToDatabase(int movieId, String movieTitle) {

        ContentValues cv = new ContentValues();
        cv.put(MovieContract.MovieEntry.MOVIE_ID, movieId);
        cv.put(MovieContract.MovieEntry.MOVIE_TITLE, movieTitle);

        return mDb.insert(MovieContract.MovieEntry.TABLE_NAME, null, cv);
    }





}
