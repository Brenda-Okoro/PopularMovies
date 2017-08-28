package com.example.brenda.popularmovies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.models.Movie;

/**
 * Created by brenda on 8/28/17.
 */

public class MovieDetailActivity extends AppCompatActivity {
    private Movie movie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movie = getIntent().getExtras().getParcelable(getString(R.string.movie_intent_key));
    }

}

