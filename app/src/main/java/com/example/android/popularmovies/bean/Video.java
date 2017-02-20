package com.example.android.popularmovies.bean;

/**
 * Created by Coco on 20/02/2017.
 */

public class Video {

    private String idVideo;
    private String key;
    private String name;

    public Video(String idVideo, String key, String name) {
        this.idVideo = idVideo;
        this.key = key;
        this.name = name;
    }


    public String getIdVideo() {
        return idVideo;
    }

    public void setIdVideo(String idVideo) {
        this.idVideo = idVideo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
