package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
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
 * FetchMoviesTask do the http request to TMDB,
 * converts response in JSONObject and build a List of Movie beans.
 * This list is passed to create the adapter attached to the RecyclerView.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

    OkHttpClient client = new OkHttpClient();
    private Context context;
    private AsyncTaskCompleteListener<List<Movie>> listener;


    public FetchMoviesTask(Context ctx, AsyncTaskCompleteListener<List<Movie>> listener)
    {
        this.context = ctx;
        this.listener = listener;
    }

    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected List<Movie> doInBackground(String... params) {

        List<Movie> movieList = new ArrayList<>();

        Request.Builder builder = new Request.Builder();
        builder.url(params[0]);

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

    protected void onPostExecute(List<Movie> result) {

        super.onPostExecute(result);
        listener.onTaskComplete(result);
    }
}
