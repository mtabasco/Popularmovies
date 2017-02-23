package com.example.android.popularmovies.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.bean.Movie;
import com.example.android.popularmovies.data.FavoritesContract;
import com.example.android.popularmovies.fragment.DetailsMovieFragment;
import com.example.android.popularmovies.fragment.ReviewsMovieFragment;
import com.example.android.popularmovies.fragment.VideosMovieFragment;

public class DetailsMovieActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private static final int MOVIES_LOADER = R.integer.movies_loader;

    private Movie movie;
    private boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_movie_tabs);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarDetails);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Retrieve the ID of the movie we want to see details
        Intent intent = getIntent();

        String movieParam = getString(R.string.json_movie);

        if (null != intent && intent.hasExtra(movieParam)) {
            movie = intent.getParcelableExtra(movieParam);

            getSupportActionBar().setTitle(movie.getOriginalTitle());
        }

        isFavorite = isFovoriteMovie(movie.getIdMovie());

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details_movie, menu);

        if(isFavorite) {

            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_favorite_checked));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                // overrides the behaviour of appbar's back button
                onBackPressed();
                return true;

            case R.id.action_favorite:
                if(isFavorite) {
                    removeFavorite();
                } else {
                    addFavorite();
                }
        }
        return false;
    }


    private boolean isFovoriteMovie(int idMovie) {

        boolean isFavorite;

        Cursor cursor = getContentResolver().query(FavoritesContract.MovieEntry.CONTENT_URI, null, "id_tmdb = ?", new String[]{String.valueOf(idMovie)}, null);

        if(null != cursor && cursor.getCount() > 0) {
            isFavorite = true;
        } else {
            isFavorite = false;
        }

        cursor.close();

        return isFavorite;
    }


    /**
     * onClickAddTask is called when the "ADD" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    private void addFavorite() {

        // Insert new task data via a ContentResolver
        // Create new empty ContentValues object
        ContentValues contentValues = new ContentValues();
        // Put the task description and selected mPriority into the ContentValues
        contentValues.put(FavoritesContract.MovieEntry.COLUMN_ID_TMDB, movie.getIdMovie());
        contentValues.put(FavoritesContract.MovieEntry.COLUMN_TITLE, movie.getOriginalTitle());
        contentValues.put(FavoritesContract.MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(FavoritesContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavoritesContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(FavoritesContract.MovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());

        // TODO add image poster (fetch in asyncTaskLoader and save it in a BLOB)

        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(FavoritesContract.MovieEntry.CONTENT_URI, contentValues);

        // Display the URI that's returned with a Toast
        // [Hint] Don't forget to call finish() to return to MainActivity after this insert is complete
        if(uri != null) {
            Toast.makeText(getBaseContext(), R.string.toast_movie_added, Toast.LENGTH_LONG).show();
            ((MenuView.ItemView) findViewById(R.id.action_favorite)).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_favorite_checked));
        }

    }

    private void removeFavorite() {

        Uri uri = FavoritesContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(movie.getIdMovie())).build();

        // COMPLETED (2) Delete a single row of data using a ContentResolver
        int moviesDeleted = getContentResolver().delete(uri, null, null);

        if(moviesDeleted > 0 ){
            Toast.makeText(getBaseContext(), R.string.toast_movie_removed, Toast.LENGTH_LONG).show();
            ((MenuView.ItemView) findViewById(R.id.action_favorite)).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_action_favorite));
            isFavorite = false;
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_details_movie, container, false);

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            Bundle args = new Bundle();
            args.putInt(getString(R.string.json_id_movie), movie.getIdMovie());

            switch(position) {

                case 0: // Details
                    Fragment details = new DetailsMovieFragment();
                    details.setArguments(args);
                    return details;

                case 1: // Trailers
                    VideosMovieFragment videos = new VideosMovieFragment();
                    videos.setArguments(args);
                    return videos;

                case 2: // Reviews
                    Fragment reviews = new ReviewsMovieFragment();
                    reviews.setArguments(args);
                    return reviews;
                }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Details";
                case 1:
                    return "Trailers";
                case 2:
                    return "Reviews";
            }
            return null;
        }
    }


}
