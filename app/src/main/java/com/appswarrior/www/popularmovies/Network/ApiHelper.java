package com.appswarrior.www.popularmovies.Network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.appswarrior.www.popularmovies.models.Movie;
import com.appswarrior.www.popularmovies.models.MovieResponse;
import com.appswarrior.www.popularmovies.models.Review;
import com.appswarrior.www.popularmovies.models.ReviewResponse;
import com.appswarrior.www.popularmovies.models.Trailer;
import com.appswarrior.www.popularmovies.models.TrailerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hp on 01/07/2017.
 */

public class ApiHelper {
    public static GetMovies getMovies;
    public static GetTrailers getTrailers;
    public static GetReviews getReviews;

    public static void getTopRatedMovies(final Context context) {
        Retrofit retrofit = new Retrofit.Builder()        // Retrofit : library to make a network request to get the data from the api
                .baseUrl(Constants.BASE_URL)                       // Retrofit initialization
                .addConverterFactory(GsonConverterFactory.create()) // Retrofit initialization
                .build(); // Retrofit initialization

        ApiInterfaces service = retrofit.create(ApiInterfaces.class);
        Call<MovieResponse> movies = service.getTopRatedMovies(Constants.API_KEY);

        movies.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) { // onresponse
                getMovies.moviesList(response.body().getResults(), context);
            }


            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                getMovies.onMovieRequestFailed(t.getMessage());
            }
        });
    }

    public static void getMostPopularMovies(final Context context) {
        Retrofit retrofit = new Retrofit.Builder()        // Retrofit : library to make a network request to get the data from the api
                .baseUrl(Constants.BASE_URL)                       // Retrofit initialization
                .addConverterFactory(GsonConverterFactory.create()) // Retrofit initialization
                .build(); // Retrofit initialization

        ApiInterfaces service = retrofit.create(ApiInterfaces.class);
        Call<MovieResponse> movies = service.getMostPopularMovies(Constants.API_KEY);

        movies.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) { // onresponse
                getMovies.moviesList(response.body().getResults(), context);
            }


            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                getMovies.onMovieRequestFailed(t.getMessage());
            }
        });
    }

    public static void getTrailers(final Context context, String movie_id) {
        Retrofit retrofit = new Retrofit.Builder()        // Retrofit : library to make a network request to get the data from the api
                .baseUrl(Constants.BASE_URL)                       // Retrofit initialization
                .addConverterFactory(GsonConverterFactory.create()) // Retrofit initialization
                .build(); // Retrofit initialization

        ApiInterfaces service = retrofit.create(ApiInterfaces.class);
        Call<TrailerResponse> trailers = service.getTrailers(movie_id, Constants.API_KEY);

        trailers.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                getTrailers.TrailersList(response.body().getResults(), context);
//                Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {

                getTrailers.onTrailersRequestFailed(t.getMessage());
            }
        });
    }

    public static void getReviews(final Context context, String movie_id) {
        Retrofit retrofit = new Retrofit.Builder()        // Retrofit : library to make a network request to get the data from the api
                .baseUrl(Constants.BASE_URL)                       // Retrofit initialization
                .addConverterFactory(GsonConverterFactory.create()) // Retrofit initialization
                .build(); // Retrofit initialization

        ApiInterfaces service = retrofit.create(ApiInterfaces.class);
        Call<ReviewResponse> reviews = service.getReviews(movie_id, Constants.API_KEY);

        reviews.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                getReviews.ReviewsList(response.body().getResults(), context);
            }


            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {

                getReviews.onReviewsRequestFailed(t.getMessage());
            }
        });
    }

    public interface GetMovies {
        void moviesList(List<Movie> movies, Context context);
        void onMovieRequestFailed(String error);
    }

    public interface GetTrailers {
        void TrailersList(List<Trailer> trailers, Context context);

        void onTrailersRequestFailed(String error);

    }

    public interface GetReviews {
        void ReviewsList(List<Review> reviews, Context context);

        void onReviewsRequestFailed(String error);

    }

    public static void setGetMoviesInterface(GetMovies Movies) {
        getMovies = Movies;
    }

    public static void setGetReviewsInterface(GetTrailers trailers) {
        getTrailers = trailers;
    }

    public static void setGetTrailersInterface(GetReviews reviews) {
        getReviews = reviews;
    }

}
