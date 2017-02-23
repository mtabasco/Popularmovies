package com.example.android.popularmovies.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.bean.Video;

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

public class VideosLoader extends AsyncTaskLoader<List<Video>> {

    OkHttpClient client = new OkHttpClient();
    private Context context;
    private String urlMovie;

    public VideosLoader(Context ctx, Bundle args) {
        super(ctx);

        this.context = ctx;
        this.urlMovie = args.getString(ctx.getString(R.string.param_movie_url));
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    public List<Video> loadInBackground() {

        List<Video> videoList = new ArrayList<>();

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

                        String idVideo = jsonItem.getString(context.getString(R.string.json_video_id));
                        String key = jsonItem.getString(context.getString(R.string.json_video_key));
                        String name = jsonItem.getString(context.getString(R.string.json_video_name));

                        Video video = new Video(idVideo, key, name);
                        videoList.add(video);

                    }

                }

            }

        } catch (IOException e) {

            Toast.makeText(context, R.string.no_internet_connection,Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {

            Log.e(context.getString(R.string.log_error_tag), context.getString(R.string.log_error_parsing_json) + e.getLocalizedMessage());
        }


        return videoList;
    }
}
