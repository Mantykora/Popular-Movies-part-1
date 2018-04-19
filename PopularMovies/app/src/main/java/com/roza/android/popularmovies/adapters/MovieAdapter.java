package com.roza.android.popularmovies;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


class MovieAdapter extends ArrayAdapter<Movie> {

    public static String title;
    public static String overview;
    public static double userRating;
    public static String releaseDate;
    public static String baseUrl;


    public MovieAdapter(Activity context, List<Movie> movies) {

        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.image, parent, false);
        }

        ImageView posterView = convertView.findViewById(R.id.image);


        assert movie != null;
        baseUrl = "http://image.tmdb.org/t/p/w185" + movie.poster;

        Picasso.with(getContext()).load(baseUrl).into(posterView);


        title = movie.title;
        overview = movie.overview;
        userRating = movie.userRating;
        releaseDate = movie.releaseDate;


        return convertView;
    }




}
