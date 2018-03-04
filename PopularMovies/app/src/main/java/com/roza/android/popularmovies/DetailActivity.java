package com.roza.android.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Piotr on 02.03.2018.
 */

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
        String title = intent.getStringExtra(MainActivity.TITLE);
        String overview = intent.getStringExtra(MainActivity.OVERVIEW);
        double userRating = intent.getDoubleExtra(MainActivity.USER_RATING, 1);
        String releaseDate = intent.getStringExtra(MainActivity.RELASE_DATE);
        String posterPath = intent.getStringExtra(MainActivity.POSTER_PATH);

        Log.i("DetailActivity.java", "" + title + overview + userRating + releaseDate + posterPath);

        PosterUrlString = "http://image.tmdb.org/t/p/w185" + posterPath;

        titleTv = (TextView) findViewById(R.id.movie_title_tv);
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
