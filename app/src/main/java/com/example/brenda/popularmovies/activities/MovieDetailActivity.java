package com.example.brenda.popularmovies.activities;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.data.DataManager;
import com.example.brenda.popularmovies.data.FavoriteListContract;
import com.example.brenda.popularmovies.fragments.OverviewFragment;
import com.example.brenda.popularmovies.fragments.ReviewFragment;
import com.example.brenda.popularmovies.fragments.TrailerFragment;
import com.example.brenda.popularmovies.models.Movie;
import com.example.brenda.popularmovies.util.ZoomOutTransformer;
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
    private ScreenSlidePagerAdapter mPagerAdapter;
    private Toolbar toolbar;
    private FloatingActionButton mMarkAsFavorite;
    private NestedScrollView scrollView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CoordinatorLayout coordinatorLayout;
    boolean isCurrentMovieFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movie = getIntent().getExtras().getParcelable(getString(R.string.movie_intent_key));

        Log.d(TAG, "Detail movie is " + movie);

        initViews();
        enableActionBar();
        loadDataIntoViews(movie);
        isCurrentMovieFavourite = DataManager.isFavorite(this, movie);
        updateFABIcon();
        setUpListener();
    }

    private void updateFABIcon() {
        if (isCurrentMovieFavourite) {
            mMarkAsFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            mMarkAsFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
    }

    private void setUpListener() {
        mMarkAsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isCurrentMovieFavourite) {
                    DataManager.removeFromFavorites(MovieDetailActivity.this, movie);
                    isCurrentMovieFavourite = false;
                    updateFABIcon();
                } else {
                    DataManager.addToFavorites(MovieDetailActivity.this, movie);
                    isCurrentMovieFavourite = true;
                    updateFABIcon();
                }
            }
        });
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
        mReleaseDate.setText(movie.getReleaseDate().substring(0, 4));
        Picasso.with(this).load(getString(R.string.movie_image_base_url) + movie.getPosterPath()).into(mMoviePosterThumbnail);
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        coordinatorLayout = (CoordinatorLayout) findViewById (R.id.coordinate_layout);
        mReleaseDate = (TextView) findViewById(R.id.tv_movie_language_date);
        mOriginalTitle = (TextView) findViewById(R.id.movie_title);
        mMovieRating = (TextView) findViewById(R.id.movie_ratings);
        mMovieDetailsTab = (TabLayout) findViewById(R.id.movie_details_tab);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mMoviePosterThumbnail = (ImageView) findViewById(R.id.movie_poster_thumbnail);
        mOverview = (TextView) findViewById(R.id.overview);
        mMarkAsFavorite = (FloatingActionButton) findViewById(R.id.mark_as_favourite_btn);
        viewpager = (ViewPager) findViewById(R.id.pager);
        scrollView = (NestedScrollView) findViewById(R.id.nested_scrollview);
        if (scrollView != null) {
            scrollView.setFillViewport(true);
        }
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewpager.setPageTransformer(true, new ZoomOutTransformer());
        viewpager.setAdapter(mPagerAdapter);
        mMovieDetailsTab.setupWithViewPager(viewpager);

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private static final int NUM_PAGES = 3;

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    bundle.putString(getString(R.string.overview_data_key), movie.getOverview());
                    Fragment overviewFragment = new OverviewFragment();
                    overviewFragment.setArguments(bundle);
                    return overviewFragment;
                case 1:
                    bundle.putLong(getString(R.string.trailer_data_key), movie.getId());
                    Fragment trailerFragment = new TrailerFragment();
                    trailerFragment.setArguments(bundle);
                    return trailerFragment;
                case 2:
                    bundle.putLong(getString(R.string.review_data_key), movie.getId());
                    Fragment reviewFragment = new ReviewFragment();
                    reviewFragment.setArguments(bundle);
                    return reviewFragment;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Overview";
                case 1:
                    return "Trailers";
                case 2:
                    return "Reviews";

            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}


