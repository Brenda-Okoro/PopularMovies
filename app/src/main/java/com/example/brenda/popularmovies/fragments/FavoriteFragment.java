package com.example.brenda.popularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.activities.MainActivity;
import com.example.brenda.popularmovies.activities.MovieDetailActivity;
import com.example.brenda.popularmovies.adapters.FavoriteMovieAdapter;
import com.example.brenda.popularmovies.adapters.MovieAdapter;
import com.example.brenda.popularmovies.data.FavoriteListContract;
import com.example.brenda.popularmovies.models.Movie;
import com.example.brenda.popularmovies.models.Review;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by brenda on 9/27/17.
 */

public class FavoriteFragment extends Fragment {
    private RecyclerView mReviewRV;
    private ProgressBar mLoadingBar;
    private FavoriteMovieAdapter mAdapter;

    public FavoriteFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favorite, container, false);

        mReviewRV = (RecyclerView) view.findViewById(R.id.rv_images);
        mLoadingBar = (ProgressBar) view.findViewById(R.id.progressBar);
        String  movieId = FavoriteListContract.FavoriteEntry.COLUMN_MOVIE_ID;
        showFavorites(movieId);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        mReviewRV.setLayoutManager(layoutManager);
        mReviewRV.setHasFixedSize(true);
//        mAdapter = new MovieAdapter(new MovieAdapter.ListItemClickListener() {
//            @Override
//            public void onListItemClick(int clickedItemIndex) {
//                Movie movie = mAdapter.getData().get(clickedItemIndex);
//                Intent intent = new Intent(FavoriteFragment.this, MovieDetailActivity.class);
//                intent.putExtra(getString(R.string.movie_intent_key), movie);
//                startActivity(intent);
//            }
//        }, this, new ArrayList<Movie>(0));
        mReviewRV.setAdapter(mAdapter);


        return view;
    }

    private void showFavorites(String movieId) {


    }
}
