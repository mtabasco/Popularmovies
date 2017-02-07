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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.bean.Movie;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
        String idMovie = intent.getStringExtra(getString(R.string.json_id_movie));

        // Build query to retrieve the movie details
        Uri.Builder builder = new Uri.Builder().scheme(getString(R.string.tmdb_scheme))
                .authority(getString(R.string.tmdb_authority))
                .appendEncodedPath(getString(R.string.tmdb_path_movie))
                .appendPath(idMovie)
                .appendQueryParameter(getString(R.string.tmdb_param_api_key), getString(R.string.tmdb_api_key));

        String myUrl = builder.build().toString();

        // Call to AsyncTask to retrieve details
        new AsyncQueryMovie().execute(myUrl);

    }

    /**
     * AsyncQueryMovie do the http request to TMDB,
     * converts response in JSONObject and create a Movie object.
     * Then, fill the views in layout with this info.
     */
    public class AsyncQueryMovie extends AsyncTask<String, Void, Movie> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected Movie doInBackground(String... params) {



            // Retrieve the id of the movie
            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);

            Request request = builder.build();

            try {
                Response response = client.newCall(request).execute();

                if(null!=response) {

                    String resultJSON = response.body().string();

                    JSONObject jsonObj = new JSONObject(resultJSON);

                    Movie movie = new Movie(jsonObj.getInt(getString(R.string.json_id_movie)),
                            jsonObj.getString(getString(R.string.json_poster_path)).substring(1),
                            jsonObj.getString(getString(R.string.json_original_title)),
                            jsonObj.getString(getString(R.string.json_overview)),
                            jsonObj.getString(getString(R.string.json_vote_average)),
                            jsonObj.getString(getString(R.string.json_release_date)));

                    return movie;
                }


            } catch (IOException e) {

                Toast.makeText(getApplicationContext(), R.string.no_internet_connection,Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {

                Log.e(getString(R.string.log_error_tag), getString(R.string.log_error_parsing_json) + e.getLocalizedMessage());
            }


            return null;
        }

        protected void onPostExecute(Movie movieDetail) {

            if(null == movieDetail) {

                Toast.makeText(getApplicationContext(), R.string.error_retrieving_movie_info,Toast.LENGTH_SHORT).show();
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
