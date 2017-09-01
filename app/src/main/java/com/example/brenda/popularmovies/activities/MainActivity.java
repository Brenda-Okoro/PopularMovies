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

import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.adapters.MovieAdapter;
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
    private static final String TAG = MainActivity.class.getSimpleName();
    private MovieAdapter mAdapter;
    private RecyclerView mImageList;
    private ProgressBar progressBar;
    private AlertDialog alertDialog;
    private Subscription subscription;
    private List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageList = (RecyclerView) findViewById(R.id.rv_images);

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        mImageList.setLayoutManager(layoutManager);
        mImageList.setHasFixedSize(true);
        mAdapter = new MovieAdapter(new MovieAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedItemIndex) {
                Movie movie = mAdapter.getData().get(clickedItemIndex);
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra(getString(R.string.movie_intent_key), movie);
                startActivity(intent);
            }
        }, this, new ArrayList<Movie>(0));
        mImageList.setAdapter(mAdapter);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        showMovies(MovieFilter.POPULAR);

        if (!isPhoneConnectedToInternet()) {
            showMessage(R.string.no_network_message);
        } else {
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isPhoneConnectedToInternet() {
        return NetworkUtils.isPhoneConnectedToInternet(this);
    }

    private void showMovies(MovieFilter filter) {
        progressBar.setVisibility(View.VISIBLE);
        mImageList.setVisibility(View.GONE);
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
                        Log.e(TAG, "onError was called "+e);

                        progressBar.setVisibility(GONE);
                    }

                    @Override
                    public void onNext(MovieListResponse movieListResponse) {
                        Log.d(TAG, "We got this from the server " + movieListResponse.getMovies().toString());
                        mAdapter.setData(movieListResponse.getMovies());
                        mImageList.setVisibility(View.VISIBLE);
                    }
                });
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

}

