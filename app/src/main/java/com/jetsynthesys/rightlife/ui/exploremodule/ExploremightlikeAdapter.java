package com.jetsynthesys.rightlife.ui.exploremodule;

import android.content.Context;
import android.content.Intent;
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
import com.jetsynthesys.rightlife.apimodel.exploremodules.Suggested;
import com.jetsynthesys.rightlife.ui.Wellness.MoreContentDetailViewActivity;
import com.jetsynthesys.rightlife.ui.utility.Utils;
import com.jetsynthesys.rightlife.ui.therledit.FavouriteRequest;
import com.jetsynthesys.rightlife.ui.therledit.OnFavouriteClickListener;
import com.google.gson.Gson;

import java.util.List;

public class ExploremightlikeAdapter extends RecyclerView.Adapter<ExploremightlikeAdapter.ViewHolder> {

    private String[] itemNames;
    private int[] itemImages;
    private LayoutInflater inflater;
    private Context ctx;
    List<Suggested> contentList;
    public ExploremightlikeAdapter(Context context, String[] itemNames, int[] itemImages, List<Suggested> contentList) {
        this.ctx = context;
        this.itemNames = itemNames;
        this.itemImages = itemImages;
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
        holder.txt_modulename.setText(Utils.getModuleText(contentList.get(position).getModuleId()));
        if (contentList.get(position).getSeriesType()!=null){
            if (contentList.get(position).getEpisodeCount()!=null) {
                holder.item_type_text.setText(contentList.get(position).getEpisodeCount()+" . "+contentList.get(position).getSeriesType());
            }else {
                holder.item_type_text.setText(contentList.get(position).getSeriesType());
            }
        }


        holder.rl_modulename.setBackgroundTintList(Utils.getModuleColorStateList(ctx,contentList.get(position).getModuleId()));
        if (contentList.get(position).getThumbnail().getUrl() != null && !contentList.get(position).getThumbnail().getUrl().isEmpty()) {
            Glide.with(ctx).load(ApiClient.CDN_URL_QA+contentList.get(position).getThumbnail().getUrl())
                    .placeholder(R.drawable.rl_placeholder)
                    .error(R.drawable.rl_placeholder)
                    .into(holder.imageView);
        }
        //holder.imageView.setImageResource(itemImages[position]);
        if (contentList.get(position).getContentType().equalsIgnoreCase("TEXT")){
            holder.img_iconview.setImageResource(R.drawable.ic_read_category);
        }else {
            holder.img_iconview.setImageResource(R.drawable.ic_sound_category);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ctx, "image clicked - "+holder.getBindingAdapterPosition(), Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                String json = gson.toJson(contentList);
                Intent intent = new Intent(holder.itemView.getContext(), MoreContentDetailViewActivity.class);
                intent.putExtra("Categorytype", json);
                intent.putExtra("position", position);
                intent.putExtra("contentId", contentList.get(position).getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });

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
        ImageView imageView,favorite_image,img_iconview;
        TextView textView,txt_modulename,item_type_text;
        RelativeLayout rl_modulename;

        public ViewHolder(View itemView) {
            super(itemView);
            rl_modulename = itemView.findViewById(R.id.rl_modulename);
            imageView = itemView.findViewById(R.id.item_image);
            textView = itemView.findViewById(R.id.item_text);
            item_type_text = itemView.findViewById(R.id.item_type_text);
            txt_modulename = itemView.findViewById(R.id.txt_modulename);
            img_iconview = itemView.findViewById(R.id.img_iconview);
            favorite_image = itemView.findViewById(R.id.favorite_image);
        }
    }
}

