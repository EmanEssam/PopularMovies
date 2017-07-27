package com.appswarrior.www.popularmovies.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.support.design.widget.Snackbar;
import android.widget.TextView;
import android.widget.Toast;

import com.appswarrior.www.popularmovies.Network.ApiHelper;
import com.appswarrior.www.popularmovies.Network.Constants;
import com.appswarrior.www.popularmovies.R;
import com.appswarrior.www.popularmovies.adapters.MoviesAdapter;
import com.appswarrior.www.popularmovies.models.Movie;
import com.appswarrior.www.popularmovies.utils.ConnectivityReceiver;
import com.appswarrior.www.popularmovies.utils.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ApiHelper.GetMovies, MoviesAdapter.OnListClickListner, ConnectivityReceiver.ConnectivityReceiverListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private ProgressBar mPb;
    private List<Movie> mMoviesList = new ArrayList<>();
    private TextView mMoviesEmptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        checkConnection();
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

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
            showProgressbar();
            ApiHelper.getTopRatedMovies(this);
            ApiHelper.setGetMoviesInterface(this);
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            hideProgressbar();
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make(findViewById(R.id.relative_layout), message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mPb = (ProgressBar) findViewById(R.id.movies_pb);
        mMoviesEmptyView = (TextView) findViewById(R.id.movies_empty_view);
    }

    private void showProgressbar() {
        mPb.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void hideProgressbar() {
        mPb.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void moviesList(List<Movie> movies, Context context) {
        hideProgressbar();
        mMoviesList = movies;
        mAdapter = new MoviesAdapter(this, movies, this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mMoviesEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void onMovieRequestFailed(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        mPb.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mMoviesEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.most_popular) {
            showProgressbar();
            ApiHelper.getMostPopularMovies(this);
            mAdapter.notifyDataSetChanged();

        } else if (id == R.id.top_rated) {
            showProgressbar();
            ApiHelper.getTopRatedMovies(this);
            mAdapter.notifyDataSetChanged();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClicked(int ClickedItemPosition) {
        Intent intent = new Intent(MainActivity.this, MovieDetails.class);
        intent.putExtra(Constants.MOVIE, mMoviesList.get(ClickedItemPosition));
        startActivity(intent);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);

    }
}
