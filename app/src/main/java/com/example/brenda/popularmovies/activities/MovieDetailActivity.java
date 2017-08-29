package com.example.brenda.popularmovies.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

/**
 * Created by brenda on 8/28/17.
 */

public class MovieDetailActivity extends AppCompatActivity {
    private static final String TAG = MovieDetailActivity.class.getSimpleName();
    private Movie movie;
    private ImageView mMoviePosterThumbnail;
    private TextView mOriginalTitle;
    private TextView mOverview;
    private TextView mReleaseDate;
    private TabLayout mMovieDetailsTab;
    private TextView mMovieRating;
    private ViewPager viewpager;
    private Toolbar toolbar;
    private NestedScrollView scrollView;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movie = getIntent().getExtras().getParcelable(getString(R.string.movie_intent_key));

        Log.d(TAG, "Detail movie is " + movie);

        initViews();
        enableActionBar();

        loadDataIntoViews(movie);
    }

    private void enableActionBar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            collapsingToolbarLayout.setTitle(" ");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemId = item.getItemId();
        if (selectedItemId == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadDataIntoViews(Movie movie) {
        double userRating = movie.getVoteAverage();
        mMovieRating.setText(userRating + "");
        mOriginalTitle.setText(movie.getOriginalTitle());
        mOverview.setText(movie.getOverview());
        mReleaseDate.setText(movie.getReleaseDate().substring(0, 4));
        Picasso.with(this).load(getString(R.string.movie_image_base_url) + movie.getPosterPath()).into(mMoviePosterThumbnail);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mReleaseDate = (TextView) findViewById(R.id.tv_movie_language_date);
        mOriginalTitle = (TextView) findViewById(R.id.movie_title);
        mMovieRating = (TextView) findViewById(R.id.movie_ratings);
        mMovieDetailsTab = (TabLayout) findViewById(R.id.movie_details_tab);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mMoviePosterThumbnail = (ImageView) findViewById(R.id.movie_poster_thumbnail);
        mOverview = (TextView) findViewById(R.id.overview);
        viewpager = (ViewPager) findViewById(R.id.pager);
        scrollView = (NestedScrollView) findViewById(R.id.nested_scrollview);
        if (scrollView != null) {
            scrollView.setFillViewport(true);
        }
        mMovieDetailsTab.setupWithViewPager(viewpager);

    }
}

