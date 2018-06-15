package com.example.sentimentalanalysis;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sentimentalanalysis.adapter.ReviewsAdapter;
import com.example.sentimentalanalysis.data.SentimentsContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.example.sentimentalanalysis.model.App;
import com.example.sentimentalanalysis.model.Review;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_downloads)
    TextView tvDownloads;
    @BindView(R.id.tv_downloads_label)
    TextView tvDownloadsLabel;
    @BindView(R.id.tv_rating)
    TextView tvRating;
    @BindView(R.id.rating_bar_indicator)
    RatingBar ratingBarIndicator;
    @BindView(R.id.tv_rate_this_app_label)
    TextView tvRateThisAppLabel;
    @BindView(R.id.rating_bar)
    RatingBar ratingBar;
    @BindView(R.id.rv_reviews)
    RecyclerView rv_reviews;
    @BindView(R.id.tv_write_review_label)
    TextView tvWriteReviewLabel;
    @BindView(R.id.et_review)
    EditText etReview;
    @BindView(R.id.btn_submit)
    Button btnSubmit;

    private static final String TAG = "DetailActivity";
    private ReviewsAdapter adapter;
    private ArrayList<Review> reviewsArrayList;
    private int appPos;
    private float avgRating;
    private float sumRating = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        rv_reviews.setNestedScrollingEnabled(false);

        if (getIntent().getIntExtra(MainActivity.APP_ID, -1) != -1) {
            App app = (App) getIntent().getSerializableExtra(MainActivity.APP_KEY);
            tvName.setText(app.getName());
            tvCompany.setText(app.getCompany());
            tvSize.setText(app.getSize());
            appPos = getIntent().getIntExtra(MainActivity.APP_ID, -1);
            setImageDrawable(tvName.getText().toString());
        }
        Cursor cursor = getContentResolver().query(
                SentimentsContract.SentimentsEntry.CONTENT_URI,
                null,
                SentimentsContract.SentimentsEntry.COLUMN_APP_ID + "=?",
                new String[]{String.valueOf(appPos)}, null);
        Log.d(TAG, "onCreate: cursor count " + cursor.getCount());

        reviewsArrayList = convertCursorsToReviewsList(cursor);

        setRating();

        adapter = new ReviewsAdapter(reviewsArrayList, this);
        rv_reviews.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false));
        rv_reviews.setAdapter(adapter);
    }

    private void setRating() {
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setMaximumFractionDigits(1);
        if (decimalFormat.format(avgRating).equalsIgnoreCase("NaN")) {
            tvRating.setText("0.0");
            ratingBarIndicator.setRating(0.0f);
        } else {
            tvRating.setText(decimalFormat.format(avgRating));
            ratingBarIndicator.setRating(Float.parseFloat(decimalFormat.format(avgRating)));
        }
    }

    public ArrayList<Review> convertCursorsToReviewsList(Cursor cursor) {
        ArrayList<Review> reviewArrayList = new ArrayList<>();
        Log.d(TAG, "convertCursorsToReviewsList: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            reviewArrayList.add(new Review(cursor.getString(2),
                    cursor.getFloat(3),
                    cursor.getLong(4)));
            sumRating = sumRating + cursor.getFloat(3);
            while (cursor.moveToNext()) {
                reviewArrayList.add(new Review(cursor.getString(2),
                        cursor.getFloat(3),
                        cursor.getLong(4)));
                sumRating = sumRating + cursor.getFloat(3);
            }
        }
        avgRating = sumRating / reviewArrayList.size();
        return reviewArrayList;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetailActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        String review = etReview.getText().toString();
        float rating = ratingBar.getRating();
        if (isValid(review, rating)) {
            saveReviewToDb(review, rating, System.currentTimeMillis());
        }
    }

    public boolean isValid(String review, float rating) {

        boolean isValid = true;
        if (TextUtils.isEmpty(review) && rating == 0.0) {
            Toast.makeText(this,
                    "Please select rating and enter review to proceed",
                    Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (TextUtils.isEmpty(review)) {
            Toast.makeText(this,
                    "Please enter review to proceed", Toast.LENGTH_SHORT).show();
            isValid = false;
        } else if (rating == 0.0) {
            Toast.makeText(this,
                    "Please select rating to proceed", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }

    public void saveReviewToDb(String review, float rating, long time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SentimentsContract.SentimentsEntry.COLUMN_APP_ID, appPos);
        contentValues.put(SentimentsContract.SentimentsEntry.COLUMN_REVIEW, review);
        contentValues.put(SentimentsContract.SentimentsEntry.COLUMN_RATING, rating);
        contentValues.put(SentimentsContract.SentimentsEntry.COLUMN_TIME,
                System.currentTimeMillis());

        Uri newUri = getContentResolver().insert(
                SentimentsContract.SentimentsEntry.CONTENT_URI, contentValues);
        if (newUri == null) {
            Log.d(TAG, "onViewClicked: review error");
            Toast.makeText(this,
                    "Error adding review.. Please try again", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "onViewClicked: review added");
            Toast.makeText(this,
                    "Review added", Toast.LENGTH_SHORT).show();

            //update recycler view with new cursor
            Cursor cursor = getContentResolver().query(
                    newUri,
                    null, null, null, null);

            if (cursor.moveToFirst()) {
                reviewsArrayList.add(new Review(cursor.getString(2),
                        cursor.getFloat(3),
                        cursor.getLong(4)));
                sumRating = sumRating + cursor.getFloat(3);
                avgRating = sumRating / reviewsArrayList.size();
                setRating();
                adapter.notifyDataSetChanged();
            }

            //clear rating bar and edittext
            etReview.setText("");
            etReview.clearFocus();
            ratingBar.setRating(0);
        }
    }

    public void setImageDrawable(String imageDrawable) {
        if (imageDrawable.equalsIgnoreCase("Netflix")) {
            ivBanner.setImageDrawable(getResources().getDrawable(R.drawable.ic_netflix_banner));
            ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_netflix));
        }
        if (imageDrawable.equalsIgnoreCase("Whatsapp")) {
            ivBanner.setImageDrawable(getResources().getDrawable(R.drawable.ic_whatsapp_banner));
            ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_whatsapp));
        }
        if (imageDrawable.equalsIgnoreCase("Instagram")) {
            ivBanner.setImageDrawable(getResources().getDrawable(R.drawable.ic_insta_banner));
            ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_insta));
        }
        if (imageDrawable.equalsIgnoreCase("Facebook")) {
            ivBanner.setImageDrawable(getResources().getDrawable(R.drawable.ic_fb_banner));
            ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_fb));
        }
        if (imageDrawable.equalsIgnoreCase("Chat")) {
            ivBanner.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_banner));
            ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat));
        }
        if (imageDrawable.equalsIgnoreCase("Amazon")) {
            ivBanner.setImageDrawable(getResources().getDrawable(R.drawable.ic_amazon_banner));
            ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_amazon));
        }
        if (imageDrawable.equalsIgnoreCase("Popers")) {
            ivBanner.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_banner));
            ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_chat_2));
        }
        if (imageDrawable.equalsIgnoreCase("Snapchat")) {
            ivBanner.setImageDrawable(getResources().getDrawable(R.drawable.ic_snapchat_banner));
            ivLogo.setImageDrawable(getResources().getDrawable(R.drawable.ic_snapchat));
        }
    }
}
