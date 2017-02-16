/*
 * Copyright (C) 2016
 */

package com.example.android.popularmovies.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.listener.AsyncTaskCompleteListener;
import com.example.android.popularmovies.task.FetchMovieDetailsTask;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.bean.Movie;
import com.squareup.picasso.Picasso;

/*
 * Activity that shows detailed info about the movie
 * selected in MainActivity.
 */

public class DetailsMovieFragment extends Fragment {

    private Movie movie;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getArguments() && null != getArguments().getString(getContext().getString(R.string.json_id_movie))) {

            String idMovie = getArguments().getString(getContext().getString(R.string.json_id_movie));

            // Build query to retrieve the movie details
            Uri.Builder builder = new Uri.Builder().scheme(getString(R.string.tmdb_scheme))
                    .authority(getString(R.string.tmdb_authority))
                    .appendEncodedPath(getString(R.string.tmdb_path_movie))
                    .appendPath(idMovie)
                    .appendQueryParameter(getString(R.string.tmdb_param_api_key), getString(R.string.tmdb_api_key));

            String myUrl = builder.build().toString();

            // Call to AsyncTask to retrieve details
            new FetchMovieDetailsTask(getContext(), new DetailsMovieFragment.FetchMovieDetailsTaskCompleteListener()).execute(myUrl);
        } else {

            Toast.makeText(getContext(), R.string.error_retrieving_movie_info, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_movie, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(null != movie) {

            drawMovieDetails(movie);
        }
    }

    public class FetchMovieDetailsTaskCompleteListener implements AsyncTaskCompleteListener<Movie> {

        @Override
        public void onTaskComplete(Movie movieDetail) {

            drawMovieDetails(movieDetail);
        }
    }

    private void drawMovieDetails(Movie movieDetail) {

        if (null == movieDetail) {

            Toast.makeText(getContext(), R.string.error_retrieving_movie_info, Toast.LENGTH_SHORT).show();
        } else {

            // Load views
            TextView titleView = (TextView) getView().findViewById(R.id.tv_title);
            ImageView posterView = (ImageView) getView().findViewById(R.id.iv_poster_details);
            TextView overviewView = (TextView) getView().findViewById(R.id.tv_overview);
            TextView userRatingView = (TextView) getView().findViewById(R.id.tv_user_rating);
            TextView releaseDateView = (TextView) getView().findViewById(R.id.tv_release_date);

            // Compose Url for poster photo
            String urlImage = getString(R.string.tmdb_prefix_images) + movieDetail.getPosterPath();
            //Render image using Picasso library
            if (!TextUtils.isEmpty(movieDetail.getPosterPath())) {
                Picasso.with(getContext()).load(urlImage)
                        .into(posterView);
            }

            // Set details info
            titleView.setText(movieDetail.getOriginalTitle());
            userRatingView.append(movieDetail.getVoteAverage());
            releaseDateView.append(movieDetail.getReleaseDate());
            overviewView.setText(movieDetail.getOverview());

            movie = movieDetail;
        }
    }
}
