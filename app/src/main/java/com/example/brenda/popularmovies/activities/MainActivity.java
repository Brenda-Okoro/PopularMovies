package com.example.brenda.popularmovies.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.adapters.FavoriteMovieAdapter;
import com.example.brenda.popularmovies.adapters.MovieAdapter;
import com.example.brenda.popularmovies.data.FavoriteListContract;
import com.example.brenda.popularmovies.models.Movie;
import com.example.brenda.popularmovies.models.MovieListResponse;
import com.example.brenda.popularmovies.networks.MovieClient;
import com.example.brenda.popularmovies.util.MovieFilter;
import com.example.brenda.popularmovies.util.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String KEY_MOVIES = "movies";
    public static final String KEY_SELECTED_MOVIE = "mMovie";
    private static final String KEY_ACTION_BAR_TITLE = "title";
    private static final String KEY_FAVORITE_MOVIE_CURSOR = "cursor";
    public static final int LOADER_ID = 100;
    private static final String TAG = MainActivity.class.getSimpleName();
    private MovieAdapter mAdapter;
    private FavoriteMovieAdapter mFavoriteMovieAdapter;
    private static final String POSITION = "position";
    private static final String TOP_VIEW = "top_view";
    private RecyclerView mMovieList;
    private ProgressBar progressBar;
    private GridLayoutManager layoutManager;
    private AlertDialog alertDialog;
    private Subscription subscription;
    private TextView mErrorMessageTextView;
    private int positionIndex = -1;
    private int topView = -1;
    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mMovieList = (RecyclerView) findViewById(R.id.rv_images);
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        mMovieList.setLayoutManager(layoutManager);
        mMovieList.setHasFixedSize(true);
        mAdapter = new MovieAdapter(new MovieAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedItemIndex) {
                Movie movie = mAdapter.getData().get(clickedItemIndex);
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra(getString(R.string.movie_intent_key), movie);
                startActivity(intent);
            }
        }, this, new ArrayList<Movie>(0));
        mMovieList.setAdapter(mAdapter);

        mFavoriteMovieAdapter = new FavoriteMovieAdapter(null,
                new FavoriteMovieAdapter.ListItemClickListener() {
                    @Override
                    public void onListItemClick(int clickedItemIndex) {
                        Movie movie = mFavoriteMovieAdapter.getItem(clickedItemIndex);
                        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                        intent.putExtra(getString(R.string.movie_intent_key), movie);
                        startActivity(intent);
                    }
                });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        showMovies(MovieFilter.POPULAR);

        if (!isPhoneConnectedToInternet()) {
            showMessage(R.string.no_network_message);

        } else {

            if (actionBarTitleWasSaved(savedInstanceState)) {
                String actionBarTitle = savedInstanceState.getString(KEY_ACTION_BAR_TITLE);
                setActionBarTitle(actionBarTitle);
            }
            if (savedInstanceState != null) {
                positionIndex = savedInstanceState.getInt(POSITION);
                topView = savedInstanceState.getInt(TOP_VIEW);
            } else {
                showMovies(MovieFilter.POPULAR);
            }

        }
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, positionIndex);
        outState.putInt(TOP_VIEW, topView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        positionIndex = layoutManager.findFirstVisibleItemPosition();
        View startView = mMovieList.getChildAt(0);
        topView = (startView == null) ? 0 : (startView.getTop() - mMovieList.getPaddingTop());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (positionIndex != -1) {
            layoutManager.scrollToPositionWithOffset(positionIndex, topView);
        }

    }

    private boolean favoriteMovieCursorWasSaved(Bundle savedInstanceState) {
        return savedInstanceState != null && savedInstanceState.containsKey(KEY_FAVORITE_MOVIE_CURSOR);
    }

    private void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_option_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_by_most_popular:
                String popularMovies = getString(R.string.sort_by_most_popular);
                showMovies(MovieFilter.POPULAR);
                setActionBarTitle(popularMovies);
                return true;
            case R.id.sort_by_highest_rated:
                String highRatedMovies = getString(R.string.sort_by_highest_rated);
                showMovies(MovieFilter.HIGH_RATED);
                setActionBarTitle(highRatedMovies);
                return true;
            case R.id.sort_by_favorite:
                sortMoviesByFavorite();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isPhoneConnectedToInternet() {
        return NetworkUtils.isPhoneConnectedToInternet(this);
    }

    private void showMovies(MovieFilter filter) {
        progressBar.setVisibility(View.VISIBLE);
        mMovieList.setVisibility(View.GONE);
        subscription = MovieClient.getInstance(this)
                .getMovies(filter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MovieListResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted was called ");

                        progressBar.setVisibility(GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError was called " + e);

                        progressBar.setVisibility(GONE);
                    }

                    @Override
                    public void onNext(MovieListResponse movieListResponse) {
                        Log.d(TAG, "We got this from the server " + movieListResponse.getMovies().toString());
                        mAdapter.setData(movieListResponse.getMovies());
                        mMovieList.setAdapter(mAdapter);
                        mMovieList.setVisibility(View.VISIBLE);
                        mErrorMessageTextView.setVisibility(GONE);
                    }
                });
    }

    private boolean actionBarTitleWasSaved(Bundle savedInstanceState) {
        return savedInstanceState != null && savedInstanceState.containsKey(KEY_ACTION_BAR_TITLE);
    }

    private void sortMoviesByFavorite() {
        String favoriteMovies = getString(R.string.sort_by_favorite);
        setActionBarTitle(favoriteMovies);
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
    }

    private void showMessage(int messageId) {
        alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage(getString(messageId));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    private void showErrorMessage(String errorMessage) {
        mMovieList.setVisibility(View.GONE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setText(errorMessage);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this) {

            Cursor favoriteMovieCursor;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (favoriteMovieCursor == null)
                    forceLoad();
                else
                    deliverResult(favoriteMovieCursor);
            }

            @Override
            public Cursor loadInBackground() {

                favoriteMovieCursor =
                        getContext().getContentResolver().query(
                                FavoriteListContract.FavoriteEntry.CONTENT_URI, null,
                                null, null, null
                        );

                return favoriteMovieCursor;
            }

            @Override
            protected void onStopLoading() {
                super.onStopLoading();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null) {
            int count = data.getCount();
            if (count == 0) {
                String noFavoriteMoviesInfoMessage = getString(R.string.info_no_favorite_movie);
                showErrorMessage(noFavoriteMoviesInfoMessage);
            } else {

                mFavoriteMovieAdapter.swapCursor(data);
                mMovieList.setAdapter(mFavoriteMovieAdapter);
                mErrorMessageTextView.setVisibility(View.GONE);
                mMovieList.setVisibility(View.VISIBLE);
            }

        } else {
            String databaseErrorMessage = getString(R.string.error_unable_to_fetch_favorite_movies);
            showErrorMessage(databaseErrorMessage);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mFavoriteMovieAdapter.swapCursor(null);
    }
}
