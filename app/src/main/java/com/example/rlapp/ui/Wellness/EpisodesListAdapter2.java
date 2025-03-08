package com.example.rlapp.ui.Wellness;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.apimodel.Episodes.EpisodeModel;
import com.example.rlapp.apimodel.Episodes.EpisodeResponseModel;
import com.example.rlapp.apimodel.morelikecontent.Like;
import com.example.rlapp.ui.therledit.FavouriteRequest;
import com.example.rlapp.ui.therledit.OnFavouriteClickListener;
import com.example.rlapp.ui.thoughtoftheday.ThoughtOfTheDayDetailActivity;
import com.example.rlapp.ui.utility.Utils;

import java.util.List;

public class EpisodesListAdapter2 extends RecyclerView.Adapter<EpisodesListAdapter.ViewHolder> {

    private String[] itemNames;
    private int[] itemImages;
    private LayoutInflater inflater;
    private Context ctx;
    List<EpisodeModel> contentList;
    public EpisodesListAdapter2(Context context, String[] itemNames, int[] itemImages, List<EpisodeModel> contentList) {
        this.ctx = context;
        this.itemNames = itemNames;
        this.itemImages = itemImages;
        this.contentList = contentList;
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public EpisodesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_episodelist_item, parent, false);
        return new EpisodesListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesListAdapter.ViewHolder holder, int position) {
        holder.textView.setText(String.valueOf(position+1) +". "+contentList.get(position).getTitle());

        if (contentList.get(position).getThumbnail().getUrl() != null && !contentList.get(position).getThumbnail().getUrl().isEmpty()) {
            Glide.with(ctx).load(ApiClient.CDN_URL_QA+contentList.get(position).getThumbnail().getUrl()).into(holder.imageView);
            Log.d("Image URL List", "list Url: " + ApiClient.CDN_URL_QA+contentList.get(position).getThumbnail().getUrl());
            Log.d("Image URL List", "list title: " + contentList.get(position).getTitle());
        }
        //holder.imageView.setImageResource(itemImages[position]);
        if (contentList.get(position).getType().equalsIgnoreCase("TEXT")){
            holder.img_iconview.setImageResource(R.drawable.ic_read_category);
        }else {
            holder.img_iconview.setImageResource(R.drawable.ic_sound_category);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, "Episode clicked  "+holder.getBindingAdapterPosition(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(holder.itemView.getContext(), SeriesEpisodeDetailActivity.class);
                intent.putExtra("seriesId", contentList.get(position).getContentId());
                intent.putExtra("episodeId", contentList.get(position).get_id());
                holder.itemView.getContext().startActivity(intent);
            }
        });

        if (contentList.get(position).isFavourited()) {
            holder.favorite_image.setImageResource(R.drawable.favstarsolid);
        } else {
            holder.favorite_image.setImageResource(R.drawable.unfavorite);
        }

        holder.favorite_image.setOnClickListener(view -> {
            FavouriteRequest favouriteRequest = new FavouriteRequest();
            favouriteRequest.setFavourite(!contentList.get(position).isFavourited());
            favouriteRequest.setEpisodeId(contentList.get(position).get_id());
            Utils.addToFavourite(ctx, contentList.get(position).getContentId(), favouriteRequest, new OnFavouriteClickListener() {
                @Override
                public void onSuccess(boolean isSuccess, String message) {
                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                    if (isSuccess) {
                        contentList.get(position).setFavourited(!contentList.get(position).isFavourited());
                        notifyItemChanged(position);
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(ctx, error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,favorite_image,img_iconview;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            textView = itemView.findViewById(R.id.item_text);
            img_iconview = itemView.findViewById(R.id.img_iconview);
            favorite_image = itemView.findViewById(R.id.favorite_image);
            favorite_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "position - "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });

            //img_iconview.setImageResource(R.drawable.ic_read_category);
        }
    }
}

