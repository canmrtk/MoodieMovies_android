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
        // item_film_card layout'unu kullandığımızdan emin olalım
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_film_card, parent, false);
        return new FilmViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film film = films.get(position);
        holder.bind(film, listener);
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
            poster = itemView.findViewById(R.id.posterImage);
            title  = itemView.findViewById(R.id.titleText);
        }

        void bind(final Film film, final OnItemClickListener listener) {
            title.setText(film.getTitle());

            // posterUrl yerine getImageUrl() kullanılıyor.
            Glide.with(itemView.getContext())
                    .load(film.getImageUrl())
                    .placeholder(R.drawable.placeholder) // Genel bir placeholder
                    .error(R.drawable.ic_films_placeholder) // Hata durumunda gösterilecek ikon
                    .into(poster);

            itemView.setOnClickListener(v -> listener.onItemClick(film));
        }
    }
}