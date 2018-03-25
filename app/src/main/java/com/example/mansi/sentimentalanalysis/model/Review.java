package com.example.mansi.sentimentalanalysis.model;

import java.io.Serializable;

public class Review implements Serializable {

    private String review;
    private float rating;
    private long time;

    public Review(String review, float rating, long time) {
        this.review = review;
        this.rating = rating;
        this.time = time;
    }

    public String getReview() {
        return review;
    }

    public float getRating() {
        return rating;
    }

    public long getTime() {
        return time;
    }
}
