package com.jetsynthesys.rightlife.ui.therledit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;
import com.jetsynthesys.rightlife.ui.utility.Utils;

class ArtistContentListAdapter extends RecyclerView.Adapter<ArtistContentListAdapter.ArtistContentViewHolder> {

    private Context context;
    private Content content;
    private OnItemClickListener onItemClickListener;

    public ArtistContentListAdapter(Context context, Content content, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.content = content;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ArtistContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_artist_content, parent, false);
        return new ArtistContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistContentViewHolder holder, int position) {
        List list = content.getList().get(position);
        holder.tvContentText.setText(list.getTitle());
        if (list.getThumbnail().getUrl() != null && !list.getThumbnail().getUrl().isEmpty()) {
            Glide.with(context)
                    .load(ApiClient.CDN_URL_QA + list.getThumbnail().getUrl())
                    .into(holder.imageArtistContent);
        }

        if (list.getContentType().equalsIgnoreCase("TEXT")) {
            holder.imageContentIcon.setImageResource(R.drawable.ic_read_category);
            holder.tvContentType.setVisibility(View.GONE);
        } else if (list.getContentType().equalsIgnoreCase("VIDEO")) {
            holder.imageContentIcon.setImageResource(R.drawable.play);
            holder.tvContentType.setVisibility(View.GONE);
        } else if (list.getContentType().equalsIgnoreCase("SERIES")) {
            holder.imageContentIcon.setImageResource(R.drawable.play);
            holder.tvContentType.setVisibility(View.VISIBLE);
            holder.tvContentType.setText(". 0 . videos");
        } else {
            holder.imageContentIcon.setImageResource(R.drawable.ic_sound_category);
            holder.tvContentType.setVisibility(View.GONE);
        }

        if (list.getIsFavourited()) {
            holder.imageFavorite.setImageResource(R.drawable.favstarsolid);
        } else {
            holder.imageFavorite.setImageResource(R.drawable.unfavorite);
        }

        holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(list, position));

        holder.imageFavorite.setOnClickListener(view -> {
            FavouriteRequest favouriteRequest = new FavouriteRequest();
            favouriteRequest.setFavourite(!list.getIsFavourited());
            favouriteRequest.setEpisodeId("");
            Utils.addToFavourite(context, list.getId(), favouriteRequest, new OnFavouriteClickListener() {
                @Override
                public void onSuccess(boolean isSuccess, String message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    if (isSuccess) {
                        content.getList().get(position).setIsFavourited(!list.getIsFavourited());
                        notifyItemChanged(position);
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return content.getList().size();
    }

    public interface OnItemClickListener {
        void onItemClick(List list, int position);
    }

    static class ArtistContentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageArtistContent, imageContentIcon, imageFavorite;
        TextView tvContentType, tvContentText;

        public ArtistContentViewHolder(@NonNull View itemView) {
            super(itemView);
            imageArtistContent = itemView.findViewById(R.id.item_image);
            imageContentIcon = itemView.findViewById(R.id.img_iconview);
            tvContentType = itemView.findViewById(R.id.item_type_text);
            tvContentText = itemView.findViewById(R.id.item_text);
            imageFavorite = itemView.findViewById(R.id.favorite_image);
        }
    }
}
