package com.example.brenda.popularmovies.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.brenda.popularmovies.data.FavoriteListContract;
import com.google.gson.annotations.SerializedName;

import static android.R.attr.id;
import static com.example.brenda.popularmovies.data.FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_ID;
import static com.example.brenda.popularmovies.data.FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_THUMBNAIL_URL;
import static com.example.brenda.popularmovies.data.FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_TITLE;
import static com.example.brenda.popularmovies.data.FavoriteListContract.FavoriteEntry.COLUMN_RELEASE_DATE;
import static com.example.brenda.popularmovies.data.FavoriteListContract.FavoriteEntry.COLUMN_SYNOPSIS;
import static com.example.brenda.popularmovies.data.FavoriteListContract.FavoriteEntry.COLUMN_USER_RATING;

/**
 * Created by brenda on 8/28/17.
 */

public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @SerializedName("id")
    private long id;
    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("overview")
    private String overview;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("release_date")
    private String releaseDate;

    private Movie(Parcel in) {
        posterPath = in.readString();
        originalTitle = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
        id = in.readLong();
    }

    public Movie() {
    }

    public Movie(long id, String posterPath, String originalTitle, String overview, double voteAverage, String releaseDate) {
        this.id = id;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public static Movie parseCursorToMovie(Cursor cursor) {
        String overview = cursor.getString(cursor.getColumnIndex(COLUMN_SYNOPSIS));
        String releaseDate = cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE));
        long id = cursor.getInt(cursor.getColumnIndex(COLUMN_MOVIE_ID));
        String originalTitle = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_TITLE));
        String thumbnailPath = cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_THUMBNAIL_URL));
        Double voteAverage = cursor.getDouble(cursor.getColumnIndex(COLUMN_USER_RATING));

        return new Movie(id, thumbnailPath, originalTitle, overview, voteAverage, releaseDate);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", posterPath='" + posterPath + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", overview='" + overview + '\'' +
                ", voteAverage=" + voteAverage +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeLong(id);
    }

    public ContentValues toFavoritesContentValues() {
        ContentValues values = new ContentValues();
        values.put(FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_ID, this.id);
        values.put(FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_THUMBNAIL_URL, this.posterPath);
        values.put(FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_TITLE, this.originalTitle);
        values.put(FavoriteListContract.FavoriteEntry.COLUMN_SYNOPSIS, this.overview);
        values.put(FavoriteListContract.FavoriteEntry.COLUMN_RELEASE_DATE, this.releaseDate);
        values.put(FavoriteListContract.FavoriteEntry.COLUMN_USER_RATING, this.voteAverage);

        return values;
    }
}
