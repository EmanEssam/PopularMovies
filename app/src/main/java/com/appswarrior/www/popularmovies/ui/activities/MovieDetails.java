package com.appswarrior.www.popularmovies.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.appswarrior.www.popularmovies.Network.ApiHelper;
import com.appswarrior.www.popularmovies.Network.Constants;
import com.appswarrior.www.popularmovies.R;
import com.appswarrior.www.popularmovies.adapters.ReviewsAdapter;
import com.appswarrior.www.popularmovies.adapters.TrailersAdapter;
import com.appswarrior.www.popularmovies.models.Movie;
import com.appswarrior.www.popularmovies.models.Review;
import com.appswarrior.www.popularmovies.models.Trailer;
import com.appswarrior.www.popularmovies.utils.ConnectivityReceiver;
import com.appswarrior.www.popularmovies.utils.MyApplication;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MovieDetails extends AppCompatActivity implements ApiHelper.GetTrailers, ApiHelper.GetReviews, ConnectivityReceiver.ConnectivityReceiverListener {
    private ImageView mMoviePoster;
    private Movie movie;
    private RatingBar mRatingBar;
    private TextView mOverview;
    private TextView mMovieTitle;
    private TextView mRelease_date;
    private ImageView mShareBtn, mBackArrow;
    private ImageView mFavorite;
    private RecyclerView mTrailersRv;
    private RecyclerView.LayoutManager mLayoutManager;
    private TrailersAdapter mTrailerAdapter;
    private RecyclerView mReviewsRv;
    private ReviewsAdapter mReviewsAdapter;
    private RecyclerView.LayoutManager mReviewsLayoutManager;
    private ProgressBar mTrailersPb;
    private ProgressBar mReviewsPb;
    private TextView mTrailersEmptyView, mReviewsEmptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().hide();
        initViews();
        movie = getIntent().getExtras().getParcelable(Constants.MOVIE);
        checkConnection();
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mReviewsLayoutManager = new LinearLayoutManager(this);
        mReviewsRv.setHasFixedSize(true);
        mTrailersRv.setLayoutManager(mLayoutManager);
        mReviewsRv.setLayoutManager(mReviewsLayoutManager);
        mTrailersRv.setHasFixedSize(true);
        mOverview.setText(movie.getOverview());
        mMovieTitle.setText(movie.getOriginalTitle());
        mRelease_date.setText(movie.getReleaseDate());
        Glide.with(this).load(Constants.IMAGE_PATH_OVER_SIZE + movie.getBackdropPath()).placeholder(R.drawable.placeholder).into(mMoviePoster);
        mRatingBar.setRating(Float.valueOf(String.valueOf(movie.getVoteAverage() / 2)));
        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://www.themoviedb.org/movie/" + movie.getId();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Movie");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void initViews() {
        mMoviePoster = (ImageView) findViewById(R.id.movie_back_poster);
        mRatingBar = (RatingBar) findViewById(R.id.rating);
        mOverview = (TextView) findViewById(R.id.movie_overview);
        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        mRelease_date = (TextView) findViewById(R.id.release_date);
        mShareBtn = (ImageView) findViewById(R.id.share_img);
        mTrailersRv = (RecyclerView) findViewById(R.id.trailers_rv);
        mReviewsRv = (RecyclerView) findViewById(R.id.reviews_rv);
        mTrailersPb = (ProgressBar) findViewById(R.id.trailers_progress_bar);
        mReviewsPb = (ProgressBar) findViewById(R.id.reviews_progress_bar);
        mTrailersEmptyView = (TextView) findViewById(R.id.trailers_empty_view);
        mReviewsEmptyView = (TextView) findViewById(R.id.reviews_empty_view);
        mBackArrow = (ImageView) findViewById(R.id.back_arrow_img);
    }

    private void hideTrailersProgressbar() {
        mTrailersPb.setVisibility(View.GONE);
        mTrailersRv.setVisibility(View.VISIBLE);
    }

    private void hideReviewsProgressbar() {
        mReviewsPb.setVisibility(View.GONE);
        mReviewsRv.setVisibility(View.VISIBLE);
    }

    private void showTrailersProgressbar() {
        mTrailersPb.setVisibility(View.VISIBLE);
        mTrailersRv.setVisibility(View.GONE);
    }

    private void showReviewsProgressbar() {
        mReviewsPb.setVisibility(View.VISIBLE);
        mReviewsRv.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            showReviewsProgressbar();
            showTrailersProgressbar();
            mReviewsEmptyView.setVisibility(View.GONE);
            mTrailersEmptyView.setVisibility(View.GONE);
            ApiHelper.getTrailers(this, String.valueOf(movie.getId()));
            ApiHelper.setGetTrailersInterface(this);
            ApiHelper.getReviews(this, String.valueOf(movie.getId()));
            ApiHelper.setGetReviewsInterface(this);
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            hideReviewsProgressbar();
            hideTrailersProgressbar();
            mReviewsEmptyView.setVisibility(View.VISIBLE);
            mTrailersEmptyView.setVisibility(View.VISIBLE);
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.activity_movie_details), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    @Override
    public void TrailersList(List<Trailer> trailers, Context context) {
        hideTrailersProgressbar();
        if (trailers.size() == 0) {
            mTrailersEmptyView.setVisibility(View.VISIBLE);
        }
        mTrailerAdapter = new TrailersAdapter(context, trailers);
        mTrailersRv.setAdapter(mTrailerAdapter);
        mTrailerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTrailersRequestFailed(String error) {
        mTrailersPb.setVisibility(View.GONE);
        mTrailersRv.setVisibility(View.GONE);
        mTrailersEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void ReviewsList(List<Review> reviews, Context context) {
        hideReviewsProgressbar();
        if (reviews.size() == 0) {
            mReviewsEmptyView.setVisibility(View.VISIBLE);
        }
        mReviewsAdapter = new ReviewsAdapter(context, reviews);
        mReviewsRv.setAdapter(mReviewsAdapter);
        mReviewsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReviewsRequestFailed(String error) {
        mReviewsPb.setVisibility(View.GONE);
        mReviewsRv.setVisibility(View.GONE);
        mReviewsEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }
}
