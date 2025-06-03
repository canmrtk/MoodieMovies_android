// com.moodiemovies.ui.FilmAdapter.java
package com.moodiemovies.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.moodiemovies.R;
import com.moodiemovies.model.Film; // MODEL PAKETİNDEN IMPORT
import com.squareup.picasso.Picasso;
import java.util.List;

public class FilmAdapter extends RecyclerView.Adapter<FilmAdapter.FilmViewHolder> {

    public interface OnFilmClickListener {
        void onFilmClick(Film film); // MODEL PAKETİNDEN Film
    }

    private List<Film> filmList;
    private OnFilmClickListener listener;

    // Context parametresini kaldırabiliriz, genellikle holder.itemView.getContext() yeterli olur.
    public FilmAdapter(List<Film> filmList, OnFilmClickListener listener) {
        this.filmList = filmList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FilmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_film, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmViewHolder holder, int position) {
        Film film = filmList.get(position);
        holder.filmTitleTextView.setText(film.getTitle());

        if (film.getPosterUrl() != null && !film.getPosterUrl().isEmpty()) {
            Picasso.get()
                    .load(film.getPosterUrl())
                    .placeholder(R.drawable.placeholder) // Placeholder ekle
                    .error(R.drawable.placeholder_error) // Hata durumunda gösterilecek resim (bu da eksik)
                    .into(holder.filmPosterImageView);
        } else {
            holder.filmPosterImageView.setImageResource(R.drawable.placeholder);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFilmClick(film);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filmList == null ? 0 : filmList.size();
    }

    static class FilmViewHolder extends RecyclerView.ViewHolder {
        ImageView filmPosterImageView;
        TextView filmTitleTextView;

        public FilmViewHolder(@NonNull View itemView) {
            super(itemView);
            filmPosterImageView = itemView.findViewById(R.id.filmPosterImageView); // item_film.xml'deki ID'ler
            filmTitleTextView = itemView.findViewById(R.id.filmTitleTextView);   // item_film.xml'deki ID'ler
        }
    }
}