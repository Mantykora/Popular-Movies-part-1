package com.roza.android.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;





public class DetailActivity extends Activity {

    ImageView posterView;
    TextView titleTv;
    TextView userRatingTv;
    TextView releaseDateTv;
    TextView overviewTv;
    String PosterUrlString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("movie");


        String title = movie.getTitle();
        String overview = movie.getOverview();
        double userRating = movie.getUserRating();
        String releaseDate = movie.getReleaseDate();
        String posterPath = movie.getPoster();

        Log.i("DetailActivity.java", "" + title + overview + userRating + releaseDate + posterPath);

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
}
