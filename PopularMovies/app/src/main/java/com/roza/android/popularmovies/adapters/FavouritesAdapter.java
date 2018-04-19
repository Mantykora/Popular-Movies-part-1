package com.roza.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roza.android.popularmovies.data.MovieContract;

import java.util.List;


public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public FavouritesAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public FavouritesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favourites_list_item, parent, false);
        return new FavouritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouritesViewHolder holder, int position) {
        // move mCursor
        if (!mCursor.moveToPosition(position))
            return;

        //update view holder
        String title = mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.MOVIE_TITLE));

        //display movie title
        holder.titleTextView.setText(title);


    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    //swap cursor with fresh one
    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }

    }

    class FavouritesViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public FavouritesViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.movie_title_fav_rv_tv);
        }
    }
}
