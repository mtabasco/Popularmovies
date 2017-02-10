/*
 * Copyright (C) 2016
 */

package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.bean.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*
 * Activity that shows detailed info about the movie
 * selected in MainActivity.
 */

public class DetailsMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details_movie);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Retrieve the ID of the movie we want to see details
        Intent intent = getIntent();

        String idMovie = getString(R.string.json_id_movie);

        if (null != intent && intent.hasExtra(idMovie)) {
            idMovie = intent.getStringExtra(idMovie);

            // Build query to retrieve the movie details
            Uri.Builder builder = new Uri.Builder().scheme(getString(R.string.tmdb_scheme))
                    .authority(getString(R.string.tmdb_authority))
                    .appendEncodedPath(getString(R.string.tmdb_path_movie))
                    .appendPath(idMovie)
                    .appendQueryParameter(getString(R.string.tmdb_param_api_key), getString(R.string.tmdb_api_key));

            String myUrl = builder.build().toString();

            // Call to AsyncTask to retrieve details
            new FetchMovieDetailsTask(this, new FetchMovieDetailsTaskCompleteListener()).execute(myUrl);
        } else {

            Toast.makeText(getApplicationContext(), R.string.error_retrieving_movie_info, Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                // overrides the behaviour of appbar's back button
                onBackPressed();
                return true;
        }
        return false;
    }

    public class FetchMovieDetailsTaskCompleteListener implements AsyncTaskCompleteListener<Movie> {

        @Override
        public void onTaskComplete(Movie movieDetail) {
            if (null == movieDetail) {

                Toast.makeText(getApplicationContext(), R.string.error_retrieving_movie_info, Toast.LENGTH_SHORT).show();
            } else {

                // Load views
                TextView titleView = (TextView) findViewById(R.id.tv_title);
                ImageView posterView = (ImageView) findViewById(R.id.iv_poster_details);
                TextView overviewView = (TextView) findViewById(R.id.tv_overview);
                TextView userRatingView = (TextView) findViewById(R.id.tv_user_rating);
                TextView releaseDateView = (TextView) findViewById(R.id.tv_release_date);

                // Compose Url for poster photo
                String urlImage = getString(R.string.tmdb_prefix_images) + movieDetail.getPosterPath();
                //Render image using Picasso library
                if (!TextUtils.isEmpty(movieDetail.getPosterPath())) {
                    Picasso.with(getApplicationContext()).load(urlImage)
                            .into(posterView);
                }

                // Set details info
                titleView.setText(movieDetail.getOriginalTitle());
                userRatingView.append(movieDetail.getVoteAverage());
                releaseDateView.append(movieDetail.getReleaseDate());
                overviewView.setText(movieDetail.getOverview());
            }
        }
    }
}
