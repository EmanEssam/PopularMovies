package com.appswarrior.www.popularmovies.Network;

import com.appswarrior.www.popularmovies.models.MovieResponse;
import com.appswarrior.www.popularmovies.models.ReviewResponse;
import com.appswarrior.www.popularmovies.models.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hp on 01/07/2017.
 */

public interface ApiInterfaces {
    @GET("top_rated?")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String api_key);

    @GET("popular?")
    Call<MovieResponse> getMostPopularMovies(@Query("api_key") String api_key);

    @GET("{id}/videos?")
    Call<TrailerResponse> getTrailers(@Path("id") String movie_id, @Query("api_key") String api_key);

    @GET("{id}/reviews?")
    Call<ReviewResponse> getReviews(@Path("id") String movie_id, @Query("api_key") String api_key);

}
