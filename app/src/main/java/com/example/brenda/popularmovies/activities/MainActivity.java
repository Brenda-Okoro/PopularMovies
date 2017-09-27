package com.example.brenda.popularmovies.activities;

import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.brenda.popularmovies.adapters.MovieAdapter;
import com.example.brenda.popularmovies.fragments.FavoriteFragment;
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


public class MainActivity extends AppCompatActivity {
    public static final String KEY_MOVIES = "movies";
    public static final String KEY_SELECTED_MOVIE = "mMovie";
    private static final String KEY_ACTION_BAR_TITLE = "title";
    private static final String KEY_FAVORITE_MOVIE_CURSOR = "cursor";
    public static final int LOADER_ID = 100;

    private static final String TAG = MainActivity.class.getSimpleName();
    private MovieAdapter mAdapter;
    private RecyclerView mMovieList;
    private ProgressBar progressBar;
    private AlertDialog alertDialog;
    private Subscription subscription;
    private TextView mErrorMessageTextView;
    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mMovieList = (RecyclerView) findViewById(R.id.rv_images);
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        showMovies(MovieFilter.POPULAR);

        if (!isPhoneConnectedToInternet()) {
            showMessage(R.string.no_network_message);

//        } else {
//
//            if (actionBarTitleWasSaved(savedInstanceState)) {
//                String actionBarTitle = savedInstanceState.getString(KEY_ACTION_BAR_TITLE);
//                setActionBarTitle(actionBarTitle);
//            }
//
//            if (favoriteMovieCursorWasSaved(savedInstanceState)) {
//                mAdapter = null;
//                getSupportLoaderManager().initLoader(LOADER_ID, null, this);
//
//            } else {
                if (savedInstanceState != null) {
                    if (savedInstanceState.containsKey(getString(R.string.movie_poster_data_key))) {
                        movies = savedInstanceState.getParcelableArrayList(getString(R.string.movie_poster_data_key));
                        mAdapter.setData(movies);
                    }
                } else {
                    showMovies(MovieFilter.POPULAR);
                }
            }

        }

    private boolean favoriteMovieCursorWasSaved(Bundle savedInstanceState) {
        return savedInstanceState.containsKey(KEY_FAVORITE_MOVIE_CURSOR);
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
                showMovies(MovieFilter.POPULAR);
                return true;
            case R.id.sort_by_highest_rated:
                showMovies(MovieFilter.HIGH_RATED);
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
                        mMovieList.setVisibility(View.VISIBLE);
                    }
                });
    }

    private boolean actionBarTitleWasSaved(Bundle savedInstanceState) {
        return savedInstanceState.containsKey(KEY_ACTION_BAR_TITLE);
    }

    private void sortMoviesByFavorite() {
        String favoriteMovies = getString(R.string.sort_by_favorite);
        setActionBarTitle(favoriteMovies);
        FavoriteFragment favoriteFragment = new FavoriteFragment();
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.main_activity, favoriteFragment);
        fragmentTransaction.commit();
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
        mMovieList.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setText(errorMessage);
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Movie selectedMovie;
//        boolean currentAdapterIsForFavoriteMovies = (mAdapter == null);
//
//        if (currentAdapterIsForFavoriteMovies) {
//            Cursor currentCursor = (Cursor) mFavoriteMovieAdapter.getItem(position);
//            selectedMovie = MovieParser.parserMovie(currentCursor);
//
//
//        } else {
//            selectedMovie = mAdapter.getItemCount(position);
//            getSupportLoaderManager().destroyLoader(LOADER_ID);
//        }
//
//        Context context = this;
//        Class classToBeStartedViaIntent = MovieDetailActivity.class;
//        Intent movieDetailsIntent = new Intent(context, classToBeStartedViaIntent);
//        movieDetailsIntent.putExtra(KEY_SELECTED_MOVIE, selectedMovie);
//
//        startActivity(movieDetailsIntent);
//    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        return new CursorLoader(this) {
//
//            Cursor favoriteMovieCursor;
//
//            @Override
//            protected void onStartLoading() {
//                super.onStartLoading();
//                progressBar.setVisibility(View.VISIBLE);
//
//                if (favoriteMovieCursor == null)
//                    forceLoad();
//                else
//                    deliverResult(favoriteMovieCursor);
//            }
//
//            @Override
//            public Cursor loadInBackground() {
//
//                favoriteMovieCursor =
//                        getContext().getContentResolver().query(
//                                FavoriteListContract.FavoriteEntry.CONTENT_URI, null,
//                                null, null, null
//                        );
//
//                return favoriteMovieCursor;
//            }
//
//            @Override
//            protected void onStopLoading() {
//                super.onStopLoading();
//            }
//        };
//    }

//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        progressBar.setVisibility(View.INVISIBLE);
//        if (data != null) {
//            int count = data.getCount();
//            if (count == 0) {
//                String noFavoriteMoviesInfoMessage = getString(R.string.info_no_favorite_movie);
//                showErrorMessage(noFavoriteMoviesInfoMessage);
//            } else {
//                mFavoriteMoviesCursor = data;
//                Context context = MainActivity.this;
//
//                mFavoriteMovieAdapter = new FavoriteMovieAdapter(context, mFavoriteMoviesCursor);
//                mAdapter = null;
//
//                mMovieList.setAdapter(mFavoriteMovieAdapter);
//                mFavoriteMovieAdapter.notifyDataSetChanged();
//
//            }
//
//        } else {
//            String databaseErrorMessage = getString(R.string.error_unable_to_fetch_favorite_movies);
//            showErrorMessage(databaseErrorMessage);
//
//        }
//    }

//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//
//    }

}

