package com.example.brenda.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.brenda.popularmovies.data.FavoriteListContract.FavoriteEntry;

/**
 * Created by brenda on 9/23/17.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "favorite_movies.db";
    private static final String PRIMARY_KEY_AUTO_INCREMENT = " PRIMARY KEY AUTOINCREMENT, ";
    private static final String TEXT = " TEXT, ";
    private static final String TEXT_NO_COMMA = " TEXT";
    private static final String NOT_NULL = " NOT NULL, ";
    private static final String UNIQUE = " UNIQUE";
    private static final String INTEGER = " INTEGER";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                    FavoriteListContract.FavoriteEntry.TABLE_NAME + " (" +
                    FavoriteListContract.FavoriteEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_ID + " INTEGER, "+
                    FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "+
                    FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_THUMBNAIL_URL + " TEXT NOT NULL, "+
                    FavoriteListContract.FavoriteEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, "+
                    FavoriteListContract.FavoriteEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "+
                    FavoriteListContract.FavoriteEntry.COLUMN_USER_RATING + " REAL NOT NULL);";



    private static final String DROP_TABLE_QUERY =
            " DROP TABLE " + FavoriteEntry.TABLE_NAME+ ";";
    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_QUERY);
    }
}
