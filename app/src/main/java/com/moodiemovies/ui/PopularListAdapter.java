package com.moodiemovies.ui; // veya com.moodiemovies.adapter

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.moodiemovies.R;
import com.moodiemovies.model.FilmListSummary;
import com.squareup.picasso.Picasso; // Picasso veya Glide
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView; // Eğer kullanıyorsanız

public class PopularListAdapter extends RecyclerView.Adapter<PopularListAdapter.ListViewHolder> {

    public interface OnListClickListener {
        void onListClick(FilmListSummary filmList);
    }

    private List<FilmListSummary> filmLists;
    // Context'e genellikle adapter içinde ihtiyaç olmaz, holder.itemView.getContext() kullanılabilir.
    // Ama Picasso gibi bazı kütüphaneler için bazen direkt geçmek gerekebilir.
    private Context context;
    private OnListClickListener listener;

    public PopularListAdapter(Context context, List<FilmListSummary> filmLists, OnListClickListener listener) {
        this.context = context; // Eğer Picasso/Glide için vb. gerekiyorsa
        this.filmLists = filmLists;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_popular_list_card, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        FilmListSummary currentList = filmLists.get(position);

        holder.tvListName.setText(currentList.getName());

        if (currentList.getOwner() != null && currentList.getOwner().getName() != null) {
            holder.tvListUserName.setText(currentList.getOwner().getName());
            if (currentList.getOwner().getAvatarUrl() != null && !currentList.getOwner().getAvatarUrl().isEmpty()) {
                Picasso.get()
                        .load(currentList.getOwner().getAvatarUrl())
                        .placeholder(R.drawable.placeholder_avatar)
                        .error(R.drawable.placeholder_avatar) // Hata durumunda da placeholder
                        .into(holder.ivListUserAvatar);
            } else {
                holder.ivListUserAvatar.setImageResource(R.drawable.placeholder_avatar);
            }
        } else {
            holder.tvListUserName.setText("Bilinmiyor");
            holder.ivListUserAvatar.setImageResource(R.drawable.placeholder_avatar);
        }

        // Liste kapak resmi
        // API'den `coverImageUrl` geliyorsa onu kullan, yoksa placeholder.
        // Figma'daki kolaj görünümü için özel bir resim işleme veya backend'den hazır URL gerekir.
        if (currentList.getCoverImageUrl() != null && !currentList.getCoverImageUrl().isEmpty()) {
            Picasso.get()
                    .load(currentList.getCoverImageUrl())
                    .placeholder(R.drawable.placeholder_list_cover)
                    .error(R.drawable.placeholder_list_cover)
                    .into(holder.ivListCover);
        } else {
            holder.ivListCover.setImageResource(R.drawable.placeholder_list_cover);
        }


        holder.tvListFilmCount.setText(String.valueOf(currentList.getFilmCount())); // Sadece sayı
        // Figma'daki gibi "3.1K Film" formatı için:
        // holder.tvListFilmCount.setText(formatCount(currentList.getFilmCount()));

        // Yorum ve beğeni sayıları API'den gelmiyorsa şimdilik placeholder
        // API'den bu veriler FilmListSummary içine eklenirse burada gösterilir.
        holder.tvListCommentCount.setText("-"); // Veya API'den geliyorsa: String.valueOf(currentList.getCommentCount())
        holder.tvListLikeCount.setText("-");   // Veya API'den geliyorsa: String.valueOf(currentList.getLikeCount())

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onListClick(currentList);
            }
        });
    }

    // Film sayısı gibi sayıları formatlamak için (örn: 3100 -> 3.1K)
    // private String formatCount(int count) {
    //     if (count < 1000) return String.valueOf(count);
    //     int exp = (int) (Math.log(count) / Math.log(1000));
    //     return String.format("%.1f%c", count / Math.pow(1000, exp), "KMBTPE".charAt(exp-1));
    // }


    @Override
    public int getItemCount() {
        return filmLists == null ? 0 : filmLists.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView ivListCover;
        CircleImageView ivListUserAvatar; // CircleImageView olarak güncellendi
        TextView tvListName, tvListUserName, tvListFilmCount, tvListCommentCount, tvListLikeCount;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            ivListCover = itemView.findViewById(R.id.ivListCover);
            ivListUserAvatar = itemView.findViewById(R.id.ivListUserAvatar);
            tvListName = itemView.findViewById(R.id.tvListName);
            tvListUserName = itemView.findViewById(R.id.tvListUserName);
            tvListFilmCount = itemView.findViewById(R.id.tvListFilmCount);
            tvListCommentCount = itemView.findViewById(R.id.tvListCommentCount);
            tvListLikeCount = itemView.findViewById(R.id.tvListLikeCount);
        }
    }

    public void updateData(List<FilmListSummary> newLists) {
        filmLists.clear();
        if (newLists != null) {
            filmLists.addAll(newLists);
        }
        notifyDataSetChanged();
    }
}