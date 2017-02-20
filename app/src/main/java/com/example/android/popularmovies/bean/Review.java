/*
 * Copyright (C) 2016
 */

package com.example.android.popularmovies.bean;

/**
 * Bean class for storing movie info
 */

public class Review {

    private String idReview;
    private String author;
    private String content;

    public String getIdReview() {
        return idReview;
    }

    public void setIdReview(String idReview) {
        this.idReview = idReview;
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



    public Review(String idReview, String author, String content) {
        this.idReview = idReview;
        this.author = author;
        this.content = content;
    }



}