package com.example.android.popularmovies.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.activity.DetailsMovieActivity;
import com.example.android.popularmovies.adapter.ReviewsAdapter;
import com.example.android.popularmovies.adapter.VideosAdapter;
import com.example.android.popularmovies.bean.Movie;
import com.example.android.popularmovies.bean.Video;
import com.example.android.popularmovies.listener.OnMovieClickListener;
import com.example.android.popularmovies.listener.OnVideoClickListener;
import com.example.android.popularmovies.loader.ReviewsLoader;
import com.example.android.popularmovies.loader.VideosLoader;

import java.util.List;


/**
 *
 */
public class VideosMovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Video>> {

    private static final int REVIEWS_LOADER = 120;

    private VideosAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getArguments()) {

            int idMovie = getArguments().getInt(getContext().getString(R.string.json_id_movie));

            Uri.Builder builder = new Uri.Builder();
            String tmdbUrl;

            builder.scheme(getString(R.string.tmdb_scheme))
                    .authority(getString(R.string.tmdb_authority))
                    .appendEncodedPath(getString(R.string.tmdb_path_movie))
                    .appendPath(String.valueOf(idMovie))
                    .appendPath(getString(R.string.tmdb_path_videos))
                    .appendQueryParameter(getString(R.string.tmdb_param_api_key), getString(R.string.tmdb_api_key));

            tmdbUrl = builder.build().toString();

            Bundle args = new Bundle();
            args.putString(getString(R.string.param_movie_url), tmdbUrl);

            getLoaderManager().initLoader(REVIEWS_LOADER, args, this).forceLoad();

        } else {

            Toast.makeText(getContext(), R.string.error_retrieving_movie_info, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_videos_movie, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.rv_list_videos);

        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    public Loader<List<Video>> onCreateLoader(int id, Bundle args) {
        return new VideosLoader(getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<Video>> loader, List<Video> result) {

        // Create the Adapter
        mAdapter = new VideosAdapter(getContext(), result);
        mRecyclerView.setAdapter(mAdapter);

        // Add click listener. When clicked, start movie details activity
        mAdapter.setOnVideoClickListener(new OnVideoClickListener() {
            @Override
            public void onVideoClick(Video item) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.youtube_prefix) + item.getKey()));

                startActivity(intent);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<List<Video>> loader) {

    }
}
