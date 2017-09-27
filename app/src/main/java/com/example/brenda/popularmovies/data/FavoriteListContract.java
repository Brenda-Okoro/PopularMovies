package com.example.brenda.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by brenda on 9/23/17.
 */

public class FavoriteListContract {
    public static final String AUTHORITY = "com.example.brenda.popularmovies";

    public static Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String FAVORITE_MOVIES_PATH = "favorite_movie";


    public static  final  class FavoriteEntry implements BaseColumns {


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(FAVORITE_MOVIES_PATH).build();
        public static final String TABLE_NAME = "favorite_movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_THUMBNAIL_URL = "thumbnail_url";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_USER_RATING = "user_rating";


    }

}
