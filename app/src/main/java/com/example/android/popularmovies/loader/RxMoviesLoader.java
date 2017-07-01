package com.example.android.popularmovies.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.bean.Movie;
import com.example.android.popularmovies.data.FavoritesContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Coco on 30/06/2017.
 */

public class RxMoviesLoader {

    OkHttpClient client = new OkHttpClient();
    private Context context;
    private String urlMovie;



    public RxMoviesLoader(Context ctx, Bundle args) {
        this.context = ctx;
        this.urlMovie = args.getString(ctx.getString(R.string.param_movie_url));
    }



    public Observable<List<Movie>> getMoviesObservable () {

        return Observable.fromCallable(new Callable<List<Movie>>() {
            @Override
            public List<Movie> call() throws Exception {

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
                                String originalTitle = jsonItem.getString(context.getString(R.string.json_original_title));
                                String overview = jsonItem.getString(context.getString(R.string.json_overview));
                                String releaseDate = jsonItem.getString(context.getString(R.string.json_release_date));
                                String voteAverage = jsonItem.getString(context.getString(R.string.json_vote_average));


                                Movie movieItem = new Movie(idMovie, posterPathMovie, originalTitle, overview, voteAverage, releaseDate);
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
        });
    }


}
