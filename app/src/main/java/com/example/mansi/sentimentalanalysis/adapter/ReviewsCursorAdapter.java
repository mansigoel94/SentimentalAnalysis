package com.example.mansi.sentimentalanalysis.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.AppCompatRatingBar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.mansi.sentimentalanalysis.R;
import com.example.mansi.sentimentalanalysis.data.SentimentsContract;

public class ReviewsCursorAdapter extends CursorAdapter {

    public ReviewsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_review,
                null, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_review = (TextView) view.findViewById(R.id.tv_review);
        TextView tv_date = (TextView) view.findViewById(R.id.tv_date);
        RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rating_bar_indicator);
        String review = cursor.getString(cursor.getColumnIndex(
                SentimentsContract.SentimentsEntry.COLUMN_REVIEW)).trim();
        String date = String.valueOf(cursor.getLong(cursor.getColumnIndex(
                SentimentsContract.SentimentsEntry.COLUMN_TIME)));
        float rating = cursor.getFloat(cursor.getColumnIndex(
                SentimentsContract.SentimentsEntry.COLUMN_RATING));
        tv_review.setText(review);
        tv_date.setText(date);
        ratingBar.setRating(rating);
    }
}