package com.jetsynthesys.rightlife.ui.therledit;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;
import com.jetsynthesys.rightlife.apimodel.morelikecontent.Like;
import com.jetsynthesys.rightlife.ui.Wellness.MoreContentDetailViewActivity;
import com.jetsynthesys.rightlife.ui.utility.Utils;
import com.google.gson.Gson;

import java.util.List;

public class RLEditDetailMoreAdapter extends RecyclerView.Adapter<RLEditDetailMoreAdapter.ViewHolder> {

    List<Like> contentList;
    private LayoutInflater inflater;
    private Context ctx;

    public RLEditDetailMoreAdapter(Context context, List<Like> contentList) {
        this.ctx = context;
        this.contentList = contentList;
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_rledit_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(contentList.get(position).getTitle());

        if (contentList.get(position).getThumbnail().getUrl() != null && !contentList.get(position).getThumbnail().getUrl().isEmpty()) {
            Glide.with(ctx).load(ApiClient.CDN_URL_QA + contentList.get(position).getThumbnail().getUrl()).into(holder.imageView);
            Log.d("Image URL List", "list Url: " + ApiClient.CDN_URL_QA + contentList.get(position).getThumbnail().getUrl());
            Log.d("Image URL List", "list title: " + contentList.get(position).getTitle());
        }
        //holder.imageView.setImageResource(itemImages[position]);

        holder.itemView.setOnClickListener(view -> {
            //Toast.makeText(ctx, "image clicked - " + holder.getBindingAdapterPosition(), Toast.LENGTH_SHORT).show();
            Gson gson = new Gson();
            String json = gson.toJson(contentList);
            Intent intent = new Intent(holder.itemView.getContext(), MoreContentDetailViewActivity.class);
            intent.putExtra("Categorytype", json);
            intent.putExtra("position", position);

            holder.itemView.getContext().startActivity(intent);
        });
        holder.txt_modulename.setText(Utils.getModuleText(contentList.get(position).getModuleId()));
        int color = Utils.getModuleColor(ctx, contentList.get(position).getModuleId());
        holder.rl_modulename.setBackgroundTintList(ColorStateList.valueOf(color));
        if (contentList.get(position).getContentType().equalsIgnoreCase("TEXT")) {
            holder.img_iconview.setImageResource(R.drawable.ic_read_category);
            holder.item_type_text.setVisibility(View.GONE);
        } else if (contentList.get(position).getContentType().equalsIgnoreCase("VIDEO")) {
            holder.img_iconview.setImageResource(R.drawable.play);
            holder.item_type_text.setVisibility(View.GONE);
        } else if (contentList.get(position).getContentType().equalsIgnoreCase("SERIES")) {
            holder.img_iconview.setImageResource(R.drawable.play);
            holder.item_type_text.setVisibility(View.VISIBLE);
            holder.item_type_text.setText(". 0 . videos");
            holder.item_type_text.setText(contentList.get(position).getEpisodeCount()+" . "+contentList.get(position).getSeriesType());
        } else {
            holder.img_iconview.setImageResource(R.drawable.ic_sound_category);
            holder.item_type_text.setVisibility(View.GONE);
        }

        if (contentList.get(position).getIsFavourited()) {
            holder.favorite_image.setImageResource(R.drawable.favstarsolid);
        } else {
            holder.favorite_image.setImageResource(R.drawable.unfavorite);
        }

        holder.favorite_image.setOnClickListener(view -> {
            FavouriteRequest favouriteRequest = new FavouriteRequest();
            favouriteRequest.setFavourite(!contentList.get(position).getIsFavourited());
            favouriteRequest.setEpisodeId("");
            Utils.addToFavourite(ctx, contentList.get(position).getId(), favouriteRequest, new OnFavouriteClickListener() {
                @Override
                public void onSuccess(boolean isSuccess, String message) {
                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                    if (isSuccess) {
                        contentList.get(position).setIsFavourited(!contentList.get(position).getIsFavourited());
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
        ImageView imageView, favorite_image, img_iconview;
        TextView textView, item_type_text, txt_modulename;
        RelativeLayout rl_modulename;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            textView = itemView.findViewById(R.id.item_text);
            txt_modulename = itemView.findViewById(R.id.txt_modulename);
            item_type_text = itemView.findViewById(R.id.item_type_text);
            img_iconview = itemView.findViewById(R.id.img_iconview);
            favorite_image = itemView.findViewById(R.id.favorite_image);
            rl_modulename = itemView.findViewById(R.id.rl_modulename);

            //img_iconview.setImageResource(R.drawable.ic_read_category);
        }
    }
}

