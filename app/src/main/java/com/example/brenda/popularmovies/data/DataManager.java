package com.example.brenda.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.models.Movie;

import static com.example.brenda.popularmovies.data.FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_ID;
import static com.example.brenda.popularmovies.data.FavoriteListContract.FavoriteEntry.CONTENT_URI;

/**
 * Created by brenda on 9/28/17.
 */

public class DataManager {
    private static final String TAG = DataManager.class.getSimpleName();

    public DataManager() {
    }

    public static boolean addToFavorites(final Context context, final Movie movie) {

        final ContentValues values = parseMovieIntoContentValues(movie);

        Uri uri = context.getContentResolver().insert(CONTENT_URI,
                values);

        if (uri != null) {
            String addedFavoriteMovieToastMessage = context.getString(R.string.add_favorite_movie_toast_message);
            Toast.makeText(context,
                    addedFavoriteMovieToastMessage
                    , Toast.LENGTH_LONG).show();
            return true;

        } else {
            Toast.makeText(context,
                    "Couldn't Add favorites to Db"
                    , Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public static void removeFromFavorites(final Context context, final Movie movie) {

        context.getContentResolver().delete(
                FavoriteListContract.FavoriteEntry.CONTENT_URI,
                COLUMN_MOVIE_ID + "=" + movie.getId(),
                null
        );
    }

    public static boolean isFavorite(final Context context, final Movie movie) {
        boolean isFavorite = false;

        Cursor cursor = context.getContentResolver().query(
                FavoriteListContract.FavoriteEntry.CONTENT_URI,
                new String[]{COLUMN_MOVIE_ID},
                COLUMN_MOVIE_ID + "=" + movie.getId(),
                null,
                null
        );
        Log.e(TAG, COLUMN_MOVIE_ID + " = " + movie.getId());
        if (cursor != null) {
            isFavorite = cursor.getCount() != 0;
            cursor.close();
        }
        return isFavorite;
    }

    private static ContentValues parseMovieIntoContentValues(Movie movie) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_TITLE, movie.getOriginalTitle());
        contentValues.put(FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_THUMBNAIL_URL, movie.getPosterPath());
        contentValues.put(FavoriteListContract.FavoriteEntry.COLUMN_SYNOPSIS, movie.getOverview());
        contentValues.put(FavoriteListContract.FavoriteEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavoriteListContract.FavoriteEntry.COLUMN_USER_RATING, movie.getVoteAverage());

        return contentValues;
    }
}
