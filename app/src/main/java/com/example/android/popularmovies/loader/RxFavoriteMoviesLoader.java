package com.example.android.popularmovies.loader;

import android.content.ContentResolver;
import android.database.Cursor;

import com.example.android.popularmovies.bean.Movie;
import com.example.android.popularmovies.data.FavoritesContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * Created by Coco on 30/06/2017.
 */

public class RxFavoriteMoviesLoader {

    ContentResolver contentResolver;

    public RxFavoriteMoviesLoader(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public Observable<List<Movie>> getFavoriteMoviesObservable () {

        return Observable.fromCallable(new Callable<List<Movie>>() {
            @Override
            public List<Movie> call() throws Exception {

                Cursor cursor = contentResolver.query(FavoritesContract.MovieEntry.CONTENT_URI, null, null, null, null);
                ArrayList<Movie> movieList = new ArrayList<>();

                while (cursor.moveToNext()) {

                    // Indices for columns
                    int idIndex = cursor.getColumnIndex(FavoritesContract.MovieEntry.COLUMN_ID_TMDB);
                    int titleIndex = cursor.getColumnIndex(FavoritesContract.MovieEntry.COLUMN_TITLE);
                    int overviewIndex = cursor.getColumnIndex(FavoritesContract.MovieEntry.COLUMN_OVERVIEW);
                    int releaseDateIndex = cursor.getColumnIndex(FavoritesContract.MovieEntry.COLUMN_RELEASE_DATE);
                    int voteAverageIndex = cursor.getColumnIndex(FavoritesContract.MovieEntry.COLUMN_VOTE_AVERAGE);
                    int posterPathIndex = cursor.getColumnIndex(FavoritesContract.MovieEntry.COLUMN_POSTER_PATH);

                    // Determine the values of the wanted data
                    int idMovie = cursor.getInt(idIndex);
                    String title = cursor.getString(titleIndex);
                    String overview = cursor.getString(overviewIndex);
                    String releaseDate = cursor.getString(releaseDateIndex);
                    String voteAverage = cursor.getString(voteAverageIndex);
                    String posterPath = cursor.getString(posterPathIndex);

                    Movie movie = new Movie(idMovie, posterPath, title, overview, voteAverage, releaseDate);

                    movieList.add(movie);
                }

                return movieList;
            }
        });

    }
}
