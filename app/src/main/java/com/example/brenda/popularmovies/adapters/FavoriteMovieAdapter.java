package com.example.brenda.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.data.FavoriteListContract;
import com.example.brenda.popularmovies.models.Movie;
import com.example.brenda.popularmovies.util.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by brenda on 9/24/17.
 */


public class FavoriteMovieAdapter extends RecyclerView.Adapter<FavoriteMovieAdapter.ImageViewHolder> {
    private static final String TAG = FavoriteMovieAdapter.class.getSimpleName();
    private ListItemClickListener mOnClickListener;
    private static int viewHolderCount;
    private LayoutInflater mLayoutInflater;
    List<Movie> movies;
    Context context;
    Cursor cursor;

    public List<Movie> getData() {
        return movies;
    }


    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public FavoriteMovieAdapter(ListItemClickListener itemClickListener, Context context, List<Movie> movies, Cursor cursor) {
        mOnClickListener = itemClickListener;
        this.movies = movies;
        this.context = context;
        this.cursor = cursor;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
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
    public void onBindViewHolder(FavoriteMovieAdapter.ImageViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
//        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (movies == null)
            return 0;
        return movies.size();
    }

    public void setData(List<Movie> movies) {
        this.movies.clear();
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView listItemImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);

            listItemImageView = (ImageView) itemView.findViewById(R.id.movie_posters_view);
        }

    public void bind (View convertView, Context context, Cursor cursor) {
        String thumbnailURL =  getThumbnailURLFrom(cursor);
        String title = getMoveTitleFrom(cursor);
        String movieId = getMovieId(cursor);
        convertView.setTag(movieId);


        ImageView posterImageView = (ImageView) convertView.findViewById(R.id.movie_posters_view);
        posterImageView.setContentDescription(title);
        String imageBaseURL = NetworkUtils.getPosterImageBaseURL();
        String imageWidthDescription = "w185";
        String fullThumbnailURL = imageBaseURL + imageWidthDescription + thumbnailURL;
        Picasso.with(context).load(fullThumbnailURL).into(posterImageView);
    }

        private String getMoveTitleFrom(Cursor cursor) {
        return  cursor.getString(cursor.getColumnIndex(
                FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_TITLE
        ));
    }

        private String getThumbnailURLFrom(Cursor cursor) {
        return  cursor.getString(cursor.getColumnIndex(
                FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_THUMBNAIL_URL
        ));
    }

    public String getMovieId(Cursor cursor) {
        return  cursor.getString(cursor.getColumnIndex(
                FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_ID
        ));
    }

    }
}
