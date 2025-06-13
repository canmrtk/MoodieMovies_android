// FilmAdapter.java
package com.moodiemovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moodiemovies.model.Film;

import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    public interface OnFilmClickListener {
        void onFilmClick(Film film);
    }

    private final List<Film> filmList;
    private final OnFilmClickListener listener;

    public FilmAdapter(List<Film> filmList, OnFilmClickListener listener) {
        this.filmList = filmList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_film_card, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film film = filmList.get(position);
        holder.bind(film);
    }

    @Override
    public int getItemCount() {
        return filmList.size();
    }

    class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImage;
        TextView titleText;

        FilmViewHolder(View itemView) {
            super(itemView);
            posterImage = itemView.findViewById(R.id.posterImage);
            titleText = itemView.findViewById(R.id.titleText);
        }

        void bind(Film film) {
            titleText.setText(film.getTitle());
            Glide.with(itemView.getContext())
                    .load(film.getPosterUrl())
                    .into(posterImage);

            itemView.setOnClickListener(v -> listener.onFilmClick(film));
        }
    }
}
