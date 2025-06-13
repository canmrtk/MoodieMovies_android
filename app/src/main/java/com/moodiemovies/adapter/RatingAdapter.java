package com.moodiemovies.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moodiemovies.R;
import com.moodiemovies.model.RatingItem;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.RatingViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(RatingItem item);
    }

    private final List<RatingItem> items;
    private final OnItemClickListener listener;

    public RatingAdapter(List<RatingItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rating_card, parent, false);
        return new RatingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        RatingItem item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.ratingValue.setText(String.valueOf(item.getRating()));
        Glide.with(holder.poster.getContext())
                .load(item.getPosterUrl())
                .into(holder.poster);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class RatingViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title, ratingValue;
        RatingViewHolder(@NonNull View itemView) {
            super(itemView);
            poster      = itemView.findViewById(R.id.ratingPoster);
            title       = itemView.findViewById(R.id.ratingTitle);
            ratingValue = itemView.findViewById(R.id.ratingValue);
        }
    }
}
