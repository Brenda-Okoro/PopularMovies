package com.example.brenda.popularmovies.networks;

import com.example.brenda.popularmovies.models.MovieListResponse;
import com.example.brenda.popularmovies.models.ReviewListResponse;
import com.example.brenda.popularmovies.models.TrailerListResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by brenda on 8/28/17.
 */

public interface MovieService {
    @GET("movie/popular")
    Observable<MovieListResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Observable<MovieListResponse> getHighRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Observable<TrailerListResponse> getTrailers(@Path("id") long movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Observable<ReviewListResponse> getReviews(@Path("id") long movieId, @Query("api_key") String apiKey);
}
