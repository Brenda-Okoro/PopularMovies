package com.example.brenda.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by brenda on 8/26/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ImageViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private ListItemClickListener mOnClickListener;
    private static int viewHolderCount;
    private LayoutInflater mLayoutInflater;
    List<Movie> movies;
    Context context;

    public List<Movie> getData() {
        return movies;
    }


    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MovieAdapter(ListItemClickListener itemClickListener, Context context, List<Movie> movies) {
        mOnClickListener = itemClickListener;
        this.movies = movies;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.image_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ImageViewHolder viewHolder = new ImageViewHolder(view);

        viewHolderCount++;
        Log.d(TAG, "onCreateViewHolder: number of ViewHolders created: "
                + viewHolderCount);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        return movies.size();
    }

    public void setData(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView listItemImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            listItemImageView = (ImageView) itemView.findViewById(R.id.movie_posters_view);
        }

        void bind(final int position) {

            Movie movie = movies.get(position);
            Picasso.with(context).load(context.getString(R.string.movie_image_base_url) + movie.getPosterPath()).into(listItemImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnClickListener.onListItemClick(position);
                }
            });
        }
    }
}
