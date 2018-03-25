package com.example.mansi.sentimentalanalysis.model;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

import java.io.Serializable;
import java.util.ArrayList;

public class App implements Serializable {
    private int imageDrawable;
    private String name;
    private String company;
    private String rating;
    private String size;
    private ArrayList<Review> reviewArrayList;

    public App(@DrawableRes int imageDrawable, String name, String company,
               String rating, String size, ArrayList<Review> reviewArrayList) {
        this.imageDrawable = imageDrawable;
        this.name = name;
        this.company = company;
        this.rating = rating;
        this.size = size;
        this.reviewArrayList = reviewArrayList;
    }

    public int getImageDrawable() {
        return imageDrawable;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public String getRating() {
        return rating;
    }

    public String getSize() {
        return size;
    }

    public ArrayList<Review> getReviewArrayList() {
        return reviewArrayList;
    }

}
