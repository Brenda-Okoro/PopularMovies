package com.example.brenda.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.data.FavoriteListContract;
import com.example.brenda.popularmovies.util.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by brenda on 9/24/17.
 */

public class FavoriteMovieAdapter extends CursorAdapter {
    Context context;

    public FavoriteMovieAdapter(Context context, Cursor cursor) {
        super(context, cursor, true);
        this.context = context;

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return inflater.inflate(R.layout.movie_list_item, parent, false );
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
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
