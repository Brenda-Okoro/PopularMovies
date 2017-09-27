package com.example.brenda.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.adapters.ReviewAdapter;
import com.example.brenda.popularmovies.models.Review;
import com.example.brenda.popularmovies.models.ReviewListResponse;
import com.example.brenda.popularmovies.networks.MovieClient;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by brenda on 9/21/17.
 */

public class ReviewFragment extends Fragment {

    private RecyclerView reviewRV;
    private ProgressBar loadingBar;
    private List<Review> reviews;
    private Subscription subscription;

    public ReviewFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);
        reviewRV = (RecyclerView) view.findViewById(R.id.reviewRV);
        loadingBar = (ProgressBar) view.findViewById(R.id.review_loading_bar);

        long movieId = getArguments().getLong(getString(R.string.review_data_key));
        showReviews(movieId);
        return view;
    }

    private void showReviews(long movieId) {
        subscription = MovieClient.getInstance(getContext())
                .getReviews(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ReviewListResponse>() {
                    @Override
                    public void onCompleted() {
                        loadingBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(ReviewListResponse reviewListResponse) {
                        reviews = reviewListResponse.getReviews();
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
                        reviewRV.setLayoutManager(mLayoutManager);
                        reviewRV.setItemAnimator(new DefaultItemAnimator());
                        reviewRV.setAdapter(new ReviewAdapter(ReviewFragment.this.getActivity(), reviews));
                    }
                });
    }

}
