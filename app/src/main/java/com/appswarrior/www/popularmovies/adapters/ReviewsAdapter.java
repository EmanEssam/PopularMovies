package com.appswarrior.www.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appswarrior.www.popularmovies.R;
import com.appswarrior.www.popularmovies.models.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 26/07/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {
    private List<Review> mReviews = new ArrayList<>();
    private Context mContext;

    public ReviewsAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public ReviewsAdapter(Context mContext, List<Review> mReviews) {
        this.mContext = mContext;
        this.mReviews = mReviews;
    }

    @Override
    public ReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_row, parent, false);
        ReviewsViewHolder vh = new ReviewsViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ReviewsViewHolder holder, final int position) {

        holder.reviewTextView.setText(mReviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }


    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        public TextView reviewTextView;

        public ReviewsViewHolder(View itemView) {
            super(itemView);
            reviewTextView = (TextView) itemView.findViewById(R.id.review_tv);

        }
    }
}
