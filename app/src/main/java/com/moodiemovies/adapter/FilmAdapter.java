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
import com.moodiemovies.model.Film;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Film film);
    }

    private final List<Film> films;
    private final OnItemClickListener listener;

    public FilmAdapter(List<Film> films, OnItemClickListener listener) {
        this.films = films;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new FilmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film film = films.get(position);
        holder.title.setText(film.getTitle());
        Glide.with(holder.poster.getContext())
                .load(film.getPosterUrl())
                .placeholder(R.drawable.default_avatar) // ya da uygun bir placeholder
                .into(holder.poster);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(film));
    }

    @Override
    public int getItemCount() {
        return films.size();
    }

    static class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;

        FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.moviePoster);
            title  = itemView.findViewById(R.id.movieTitle);
        }
    }
}
