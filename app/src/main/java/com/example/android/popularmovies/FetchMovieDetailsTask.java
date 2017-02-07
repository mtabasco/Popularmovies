package com.example.android.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.bean.Movie;
import com.squareup.picasso.Picasso;

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
 * AsyncQueryMovie do the http request to TMDB,
 * converts response in JSONObject and create a Movie object.
 * Then, fill the views in layout with this info.
 */
public class FetchMovieDetailsTask extends AsyncTask<String, Void, Movie> {

    OkHttpClient client = new OkHttpClient();
    private Context context;
    private AsyncTaskCompleteListener<Movie> listener;


    public FetchMovieDetailsTask(Context ctx, AsyncTaskCompleteListener<Movie> listener)
    {
        this.context = ctx;
        this.listener = listener;
    }

    protected void onPreExecute()
    {
        super.onPreExecute();
    }

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

                Movie movie = new Movie(jsonObj.getInt(context.getString(R.string.json_id_movie)),
                        jsonObj.getString(context.getString(R.string.json_poster_path)).substring(1),
                        jsonObj.getString(context.getString(R.string.json_original_title)),
                        jsonObj.getString(context.getString(R.string.json_overview)),
                        jsonObj.getString(context.getString(R.string.json_vote_average)),
                        jsonObj.getString(context.getString(R.string.json_release_date)));

                return movie;
            }


        } catch (IOException e) {

            Toast.makeText(context, R.string.no_internet_connection,Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {

            Log.e(context.getString(R.string.log_error_tag), context.getString(R.string.log_error_parsing_json) + e.getLocalizedMessage());
        }


        return null;
    }

    protected void onPostExecute(Movie movieDetail) {

        super.onPostExecute(movieDetail);
        listener.onTaskComplete(movieDetail);
    }
}
