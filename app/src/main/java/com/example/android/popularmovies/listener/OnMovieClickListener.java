/*
 * Copyright (C) 2016
 */

package com.example.android.popularmovies.listener;

import com.example.android.popularmovies.bean.Movie;

/**
 * Interface for implementing click event listener
 * over recycler view
 */


public interface OnMovieClickListener {
    void onMovieClick(Movie movie);
}
