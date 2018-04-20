package com.roza.android.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roza.android.popularmovies.models.Comment;
import com.roza.android.popularmovies.R;

import java.util.List;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private List<Comment> comments;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Comment comment = comments.get(position);

        holder.contentView.setText(comment.getContent());
        holder.authorView.setText(comment.getAuthor());


    }


    @Override
    public int getItemCount() {

        if (comments != null && !comments.isEmpty()) {

            return comments.size();
        } else {
            return 0;
        }


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String boundString;

        public final TextView contentView;
        public final TextView authorView;

        public ViewHolder(View itemView) {
            super(itemView);

            contentView = itemView.findViewById(R.id.coment_content_tv);
            authorView = itemView.findViewById(R.id.coment_author_tv);
        }
    }

    public CommentAdapter(List<Comment> items) {
        comments = items;
    }
}
