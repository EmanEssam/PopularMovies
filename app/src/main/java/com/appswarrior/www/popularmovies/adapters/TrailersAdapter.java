package com.appswarrior.www.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.appswarrior.www.popularmovies.R;
import com.appswarrior.www.popularmovies.models.Trailer;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hp on 19/07/2017.
 */

public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.TrailerViewHolder> {
    private List<Trailer> mTrailers = new ArrayList<>();
    private Context mContext;

    public TrailersAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public TrailersAdapter(Context mContext, List<Trailer> mTrailers) {
        this.mContext = mContext;
        this.mTrailers = mTrailers;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_item_row, parent, false);
        TrailerViewHolder vh = new TrailerViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, final int position) {
        Glide.with(mContext).load("https://img.youtube.com/vi/" + mTrailers.get(position).getKey() + "/0.jpg").placeholder(R.drawable.placeholder).into(holder.trailerVideo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://www.youtube.com/watch?v=" + mTrailers.get(position).getKey());
                Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                mContext.startActivity(launchBrowser);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }


    public class TrailerViewHolder extends RecyclerView.ViewHolder {
        public ImageView trailerVideo;

        public TrailerViewHolder(View itemView) {
            super(itemView);
            trailerVideo = (ImageView) itemView.findViewById(R.id.video_view_trailer);

        }
    }
}
