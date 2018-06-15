package com.example.sentimentalanalysis.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sentimentalanalysis.R;
import com.example.sentimentalanalysis.model.App;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {

    private final ArrayList<App> appArrayList;
    private ItemClickListener itemClickListener;
    private final Context context;

    public AppAdapter(ArrayList<App> appArrayList,
                      ItemClickListener itemClickListener,
                      Context context) {
        this.appArrayList = appArrayList;
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_app, parent, false);
        return new ViewHolder(root, itemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        App currentApp = appArrayList.get(position);
        holder.ivLogo.setImageDrawable(
                context.getResources().getDrawable(currentApp.getImageDrawable()));
        holder.tvName.setText(currentApp.getName());
        holder.tvCompany.setText(currentApp.getCompany());
        if (currentApp.getRating() != null) {
            holder.tvRating.setText(currentApp.getRating());
        } else {
            holder.tvRating.setText("0.0");
        }
        holder.tvSize.setText(currentApp.getSize());
        holder.tvSentiValue.setText((int) currentApp.getSentiValue() + "%");
        if (currentApp.isPositive()) {
            holder.tvSentiValue.setTextColor(
                    context.getResources().getColor(R.color.green));
            holder.ivThumbIc.setImageDrawable(
                    context.getResources().getDrawable(R.drawable.ic_thumbs_up));
        } else {
            holder.tvSentiValue.setTextColor(
                    context.getResources().getColor(R.color.red));
            holder.ivThumbIc.setImageDrawable(
                    context.getResources().getDrawable(R.drawable.ic_thumbs_down));
        }
    }

    @Override
    public int getItemCount() {
        return appArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_logo)
        ImageView ivLogo;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_company)
        TextView tvCompany;
        @BindView(R.id.tv_size)
        TextView tvSize;
        @BindView(R.id.tv_rating)
        TextView tvRating;
        @BindView(R.id.iv_star)
        ImageView ivStar;
        @BindView(R.id.tv_senti_value)
        TextView tvSentiValue;
        @BindView(R.id.iv_thumb_ic)
        ImageView ivThumbIc;

        public ViewHolder(View itemView, final ItemClickListener itemClickListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface ItemClickListener {
        public void onItemClick(int position);
    }
}
