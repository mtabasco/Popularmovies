/*
 * Copyright (C) 2016
 */

package com.example.android.popularmovies.bean;

/**
 * Bean class for storing movie info
 */

public class Movie {

    private int idMovie;
    private String posterPath;
    private String originalTitle;

    public Movie(int idMovie, String posterPath, String originalTitle, String overview, String voteAverage, String releaseDate) {
        this.idMovie = idMovie;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    private String overview;
    private String voteAverage;

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    private String releaseDate;





    public Movie (int idMovie, String posterPath) {

        this.idMovie = idMovie;
        this.posterPath = posterPath;
    }


    public Movie() {
    }
}
