package com.example.android.popularmovies.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.bean.Movie;
import com.example.android.popularmovies.listener.AsyncTaskCompleteListener;

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
 * Created by Coco on 16/02/2017.
 */

public class MoviesLoader extends AsyncTaskLoader<List<Movie>> {

    OkHttpClient client = new OkHttpClient();
    private Context context;
    private AsyncTaskCompleteListener<List<Movie>> listener;
    private String urlMovie;

    public MoviesLoader(Context ctx, AsyncTaskCompleteListener<List<Movie>> listener, Bundle args) {
        super(ctx);

        this.context = ctx;
        this.listener = listener;
        this.urlMovie = args.getString(ctx.getString(R.string.param_movie_url));
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    public List<Movie> loadInBackground() {

        List<Movie> movieList = new ArrayList<>();

        Request.Builder builder = new Request.Builder();
        builder.url(urlMovie);

        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();

            String resultJSON = response.body().string();

            if(null!= resultJSON) {

                JSONObject jsonObj = new JSONObject(resultJSON);

                JSONArray jsonArray = jsonObj.getJSONArray(context.getString(R.string.json_results));

                if(null!=jsonArray) {

                    // Loop over the array of movies and create the list of Movie beans
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonItem = jsonArray.getJSONObject(i);

                        int idMovie = jsonItem.getInt(context.getString(R.string.json_id_movie));
                        String posterPathMovie = jsonItem.getString(context.getString(R.string.json_poster_path)).substring(1);

                        Movie movieItem = new Movie(idMovie, posterPathMovie);
                        movieList.add(movieItem);

                    }

                }

            }

        } catch (IOException e) {

            Toast.makeText(context, R.string.no_internet_connection,Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {

            Log.e(context.getString(R.string.log_error_tag), context.getString(R.string.log_error_parsing_json) + e.getLocalizedMessage());
        }


        return movieList;
    }
}
