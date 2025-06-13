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
import com.moodiemovies.model.FilmListSummary;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(FilmListSummary list);
    }

    private final List<FilmListSummary> lists;
    private final OnItemClickListener listener;

    public ListAdapter(List<FilmListSummary> lists, OnItemClickListener listener) {
        this.lists = lists;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_card, parent, false);
        return new ListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        FilmListSummary list = lists.get(position);
        holder.name.setText(list.getName());
        Glide.with(holder.cover.getContext())
                .load(list.getCoverImageUrl())
                .into(holder.cover);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(list));
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView name;
        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.listCover);
            name  = itemView.findViewById(R.id.listName);
        }
    }
}
