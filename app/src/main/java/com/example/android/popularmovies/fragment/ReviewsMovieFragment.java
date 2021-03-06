package com.example.android.popularmovies.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.adapter.ReviewsAdapter;
import com.example.android.popularmovies.bean.Review;
import com.example.android.popularmovies.loader.ReviewsLoader;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class ReviewsMovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Review>>{


    private static final int REVIEWS_LOADER = 110;

    private ReviewsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    public ReviewsMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null != getArguments()) {

            int idMovie = getArguments().getInt(getContext().getString(R.string.json_id_movie));

            Uri.Builder builder = new Uri.Builder();
            String tmdbUrl;



            builder.scheme(getString(R.string.tmdb_scheme))
                    .authority(getString(R.string.tmdb_authority))
                    .appendEncodedPath(getString(R.string.tmdb_path_movie))
                    .appendPath(String.valueOf(idMovie))
                    .appendPath(getString(R.string.tmdb_path_reviews))
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
        return inflater.inflate(R.layout.fragment_reviews_movie, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.rv_list_reviews);


    }

    @Override
    public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
        return new ReviewsLoader(getContext(), args);
    }

    @Override
    public void onLoadFinished(Loader<List<Review>> loader, List<Review> result) {

        // Create the Adapter
        mAdapter = new ReviewsAdapter(getContext(), result);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    public void onLoaderReset(Loader<List<Review>> loader) {

    }
}
