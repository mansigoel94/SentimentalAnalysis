package com.example.sentimentalanalysis.model;

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
    private boolean ispositive;
    private float sentiValue;

    public App(@DrawableRes int imageDrawable, String name, String company,
               String rating, String size) {
        this.imageDrawable = imageDrawable;
        this.name = name;
        this.company = company;
        this.rating = rating;
        this.size = size;
    }

    public App(@DrawableRes int imageDrawable, String name, String company,
               String rating, String size, ArrayList<Review> reviewArrayList) {
        this.imageDrawable = imageDrawable;
        this.name = name;
        this.company = company;
        this.rating = rating;
        this.size = size;
        this.reviewArrayList = reviewArrayList;
    }

    public App(@DrawableRes int imageDrawable, String name, String company,
               String rating, String size, ArrayList<Review> reviewArrayList,
               boolean positiveNegative, float sentiValue) {
        this.imageDrawable = imageDrawable;
        this.name = name;
        this.company = company;
        this.rating = rating;
        this.size = size;
        this.reviewArrayList = reviewArrayList;
        this.ispositive = positiveNegative;
        this.sentiValue = sentiValue;
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

    public boolean isPositive() {
        return ispositive;
    }

    public void setIspositive(boolean ispositive) {
        this.ispositive = ispositive;
    }

    public float getSentiValue() {
        return sentiValue;
    }

    public void setSentiValue(float sentiValue) {
        this.sentiValue = sentiValue;
    }

    public void setReviewArrayList(ArrayList<Review> reviewArrayList) {
        this.reviewArrayList = reviewArrayList;
    }

}
