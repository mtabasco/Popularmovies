/*
 * Copyright (C) 2016
 */

package com.example.android.popularmovies.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bean class for storing movie info
 */

public class Review {

    private String idMovie;
    private String author;
    private String content;

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



    public Review(String idMovie, String author, String content) {
        this.idMovie = idMovie;
        this.author = author;
        this.content = content;
    }



}