package com.roza.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hiddenpik on 11.03.2018.
 */

public class VideoAdapter extends ArrayAdapter<Video>{

    public VideoAdapter(@NonNull Context context, List<Video> videos) {
        super(context, 0, videos);
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Video video = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item, parent, false);
        }

        TextView trailerTextView = convertView.findViewById(R.id.trailer_tv);

        trailerTextView.setText(video.getName());



        Log.i("DetailActivity.this", video.getName());

        return convertView;

    }
}
