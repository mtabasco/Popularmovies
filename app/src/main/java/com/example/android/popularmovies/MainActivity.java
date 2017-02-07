/*
 * Copyright (C) 2016
 */


package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.popularmovies.bean.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Main activity for popular movies. Shows a grid of poster movies,
 * providing filter: popular and top rated.
 */

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private RecyclerView mRecyclerView;
    private MoviesAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list_movies);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);



        // Spinner
        Spinner spinner = (Spinner) findViewById(R.id.sp_filter);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_filter_movies, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }


    /**
     * Starts the async task to obtain the grid of movies, based on
     * the user's selection (popular or topo rated)
     */
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        // Check if internet connection is present
        if (!isOnline()) {

            Toast.makeText(getApplicationContext(), R.string.no_internet_connection,Toast.LENGTH_LONG).show();
        } else {

            Uri.Builder builder = new Uri.Builder();
            String tmdbUrl;

            switch (pos) {
                case 0: // Popular movies filter

                     builder.scheme(getString(R.string.tmdb_scheme))
                    .authority(getString(R.string.tmdb_authority))
                    .appendEncodedPath(getString(R.string.tmdb_path_movie))
                    .appendPath(getString(R.string.tmdb_path_popular))
                    .appendQueryParameter(getString(R.string.tmdb_param_api_key), getString(R.string.tmdb_api_key));

                    tmdbUrl = builder.build().toString();

                    new FetchMoviesTask(this, new FetchMoviesTaskCompleteListener()).execute(tmdbUrl);

                    return;

                case 1: // Top rated movies filter

                    builder.scheme(getString(R.string.tmdb_scheme))
                            .authority(getString(R.string.tmdb_authority))
                            .appendEncodedPath(getString(R.string.tmdb_path_movie))
                            .appendPath(getString(R.string.tmdb_path_top_rated))
                            .appendQueryParameter(getString(R.string.tmdb_param_api_key), getString(R.string.tmdb_api_key));

                    tmdbUrl = builder.build().toString();

                    new FetchMoviesTask(this, new FetchMoviesTaskCompleteListener()).execute(tmdbUrl);

            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        int orientation = getResources().getConfiguration().orientation;

        // Portrait
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            // GridLayoutManager
            mLayoutManager = new GridLayoutManager(getApplicationContext(), calculateNoOfColumns(getBaseContext()));
            mRecyclerView.setLayoutManager(mLayoutManager);
        } else { // Landscape
            mLayoutManager = new GridLayoutManager(getApplicationContext(), calculateNoOfColumns(getBaseContext()));
            mRecyclerView.setLayoutManager(mLayoutManager);
        }
    }

    private static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class FetchMoviesTaskCompleteListener implements AsyncTaskCompleteListener<List<Movie>>
    {

        @Override
        public void onTaskComplete(List<Movie> result)
        {
            // Create the Adapter
            mAdapter = new MoviesAdapter(getApplicationContext(), result);
            mRecyclerView.setAdapter(mAdapter);

            // Add click listener. When clicked, start movie details activity
            mAdapter.setOnMovieClickListener(new OnMovieClickListener() {
                @Override
                public void onMovieClick(Movie item) {

                    Intent intent = new Intent(getApplicationContext(), DetailsMovieActivity.class);
                    intent.putExtra(getString(R.string.json_id_movie), String.valueOf(item.getIdMovie()));

                    startActivity(intent);
                }
            });
        }
    }





    /**
     * Checks if there is connectivity
     */
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}