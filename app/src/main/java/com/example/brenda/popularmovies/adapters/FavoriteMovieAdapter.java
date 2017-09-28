package com.example.brenda.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by brenda on 9/24/17.
 */


public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.ImageViewHolder> {
    private Cursor mCursor;
    private final FavoriteMovieAdapter.ListItemClickListener mOnClickListener;

    public FavoriteMovieAdapter(Cursor cursor, FavoriteMovieAdapter.ListItemClickListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        swapCursor(cursor);
    }

    public void swapCursor(Cursor cursor) {
        this.mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.bind(position);
    }

    public Movie getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        } else if (mCursor != null && mCursor.moveToPosition(position)) {
            return Movie.parseCursorToMovie(mCursor);
        }
        return null;
    }

    @Override
    public int getItemCount() {
        if (mCursor == null)
            return 0;
        return mCursor.getCount();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView listItemImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            listItemImageView = (ImageView) itemView.findViewById(R.id.movie_posters_view);
        }

        void bind(final int position) {
            Context context = itemView.getContext();
            final Movie movie = getItem(position);
            Picasso.with(context)
                    .load(context.getString(R.string.movie_image_base_url) + movie.getPosterPath())
                    .into(listItemImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onListItemClick(position);
                }
            });
        }
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}