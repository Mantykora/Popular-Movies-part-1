package com.roza.android.popularmovies;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.roza.android.popularmovies.adapters.FavouritesAdapter;
import com.roza.android.popularmovies.data.MovieContract;


public class FavouritesActivity extends Activity {

    private FavouritesAdapter mAdapter;
    private TextView noFavTv;


    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        RecyclerView favouritesRecyclerView;
        noFavTv = findViewById(R.id.no_fav_tv);

        favouritesRecyclerView = (RecyclerView) findViewById(R.id.favourites_rv);
        favouritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        Cursor cursor = getAllFavourites();


        if (cursor != null) {
            mAdapter = new FavouritesAdapter(this, cursor);
            favouritesRecyclerView.setAdapter(mAdapter);
        } else
            noFavTv.setVisibility(View.VISIBLE);
        noFavTv.setText("No favourites yet...");
    }

    public Cursor getAllFavourites() {

        return getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                MovieContract.MovieEntry._ID);
    }


}
