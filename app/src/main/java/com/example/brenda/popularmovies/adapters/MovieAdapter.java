package com.example.brenda.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.brenda.popularmovies.MainActivity;
import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.models.Movie;

import java.util.List;

/**
 * Created by brenda on 8/26/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ImageViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    private ListItemClickListener mOnClickListener;
    private static int viewHolderCount;
    private LayoutInflater mLayoutInflater;
    private int mNumberItems;
    List<Movie> movies;
    Context context;


    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public MovieAdapter(int numberOfItems, ListItemClickListener listener) {
        mNumberItems = numberOfItems;
        mOnClickListener = listener;
        viewHolderCount = 0;
    }

    public MovieAdapter(Context context, List<Movie> movies) {
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
        return mNumberItems;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        ImageView listItemImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            listItemImageView = (ImageView) itemView.findViewById(R.id.movie_posters_view);
            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            //listItemImageView.setImageURI();
        }


        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);

        }
    }
}
