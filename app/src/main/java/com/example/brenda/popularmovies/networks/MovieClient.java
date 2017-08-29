package com.example.brenda.popularmovies.networks;

import android.content.Context;

import com.example.brenda.popularmovies.R;
import com.example.brenda.popularmovies.models.MovieListResponse;
import com.example.brenda.popularmovies.util.MovieFilter;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

/**
 * Created by brenda on 8/28/17.
 */

public class MovieClient {
    private static MovieClient instance;
    private MovieService movieService;
    private String apiKey;

    private MovieClient(Context context) {
        final Gson gson =
                new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(context.getString(R.string.base_url))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        movieService = retrofit.create(MovieService.class);
        apiKey = context.getString(R.string.api_key);
    }

    public static MovieClient getInstance(Context context) {
        if (instance == null) {
            instance = new MovieClient(context);
        }
        return instance;
    }

    public Observable<MovieListResponse> getMovies(MovieFilter filter) {
        if (filter == MovieFilter.HIGH_RATED) {
            return movieService.getHighRatedMovies(apiKey);
        }
        return movieService.getPopularMovies(apiKey);
    }
}
