/*
 * Copyright (C) 2016
 */

package com.example.android.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.bean.Movie;
import com.example.android.popularmovies.listener.OnMovieClickListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for RecyclerView
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.PosterViewHolder> {

    private List<Movie> mMoviesList;
    private Context mContext;

    private OnMovieClickListener onMovieClickListener;

    public MoviesAdapter(Context context, List<Movie> movies) {

        mContext =  context;
        mMoviesList = movies;
    }




    public void setOnMovieClickListener(OnMovieClickListener onMovieClickListener) {
        this.onMovieClickListener = onMovieClickListener;
    }

    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_item, null);

        return new PosterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder holder, int position) {

        final Movie movie = mMoviesList.get(position);

        String urlImage = mContext.getString(R.string.tmdb_prefix_images) + movie.getPosterPath();

        //Render image using Picasso library
        if (!TextUtils.isEmpty(movie.getPosterPath())) {
            Picasso.with(mContext)
                    .load(urlImage)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_no_image)
                    .into(holder.imageView);
        }


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMovieClickListener.onMovieClick(movie);
            }
        };
        holder.imageView.setOnClickListener(listener);


    }




    @Override
    public int getItemCount() {
        return (null != mMoviesList ? mMoviesList.size():0);
    }

    class PosterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;


        public PosterViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.iv_poster);

        }
    }
}
