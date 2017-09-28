package com.example.brenda.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.brenda.popularmovies.data.FavoriteListContract.FavoriteEntry.TABLE_NAME;

/**
 * Created by brenda on 9/24/17.
 */

public class FavoriteProvider extends ContentProvider {

    private FavoriteDbHelper mFavoriteDbHelper;

    private static final String TAG = FavoriteProvider.class.getSimpleName();
    public static final int FAVORITE_MOVIES = 100;
    public static final int FAVORITE_MOVIES_WITH_ID = 101;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(FavoriteListContract.AUTHORITY, FavoriteListContract.FAVORITE_MOVIES_PATH, FAVORITE_MOVIES);
        matcher.addURI(FavoriteListContract.AUTHORITY, FavoriteListContract.FAVORITE_MOVIES_PATH + "/#", FAVORITE_MOVIES_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mFavoriteDbHelper = new FavoriteDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mFavoriteDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor returnCursor = null;

        switch (match) {

            case FAVORITE_MOVIES:

                returnCursor = db.query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null

                );
                break;
            case FAVORITE_MOVIES_WITH_ID:
                break;
            default:
                new UnsupportedOperationException("Unknown uri: " + uri);
        }
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);
        Log.d(TAG, "Retrieved " + returnCursor.getCount() + "Favorites from db");
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mFavoriteDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri returnUri = null;
        switch (match) {

            case FAVORITE_MOVIES:
                long id = db.insert(TABLE_NAME, null, values);

                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavoriteListContract.FavoriteEntry
                            .CONTENT_URI, id);
                }

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        Log.d(TAG, "Successfully inserted " + values + "to db");

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mFavoriteDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVORITE_MOVIES:
                int noOfRows = database.delete(TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return noOfRows;

            case FAVORITE_MOVIES_WITH_ID:

                int noOfRows2 = database.delete(TABLE_NAME, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(uri, null);
                return noOfRows2;
            default:
                throw new IllegalArgumentException("Deletion cannot be done on unknown uri" + uri);
        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}