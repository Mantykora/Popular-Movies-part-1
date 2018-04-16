package com.roza.android.popularmovies;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class CommentAdapter extends ArrayAdapter<Video> {

    public CommentAdapter(Activity context, List<Video> videos) {
        super(context, 0, videos);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Video video = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView contentView = convertView.findViewById(R.id.coment_content_tv);
        TextView authorView = convertView.findViewById(R.id.coment_author_tv);


        return super.getView(position, convertView, parent);
    }
}
