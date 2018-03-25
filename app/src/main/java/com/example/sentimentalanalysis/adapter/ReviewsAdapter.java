package com.example.sentimentalanalysis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.sentimentalanalysis.R;
import com.example.sentimentalanalysis.Utils;
import com.example.sentimentalanalysis.model.Review;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private ArrayList<Review> reviewArrayList;
    private Context context;

    public ReviewsAdapter(ArrayList<Review> reviewArrayList, Context context) {
        this.reviewArrayList = reviewArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review currentReview = reviewArrayList.get(position);
        holder.tvReview.setText(currentReview.getReview());
        holder.tvDate.setText(Utils.convertTimeStampToFormattedDate(
                currentReview.getTime(), "dd MMM"));
        holder.ratingBarIndicator.setRating(currentReview.getRating());
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rating_bar_indicator)
        RatingBar ratingBarIndicator;
        @BindView(R.id.tv_review)
        TextView tvReview;
        @BindView(R.id.tv_date)
        TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
