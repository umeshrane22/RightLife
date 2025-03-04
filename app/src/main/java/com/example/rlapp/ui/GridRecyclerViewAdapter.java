package com.example.rlapp.ui;

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
import com.example.rlapp.apimodel.modulecontentlist.Content;
import com.example.rlapp.ui.Wellness.MoreContentDetailViewActivity;
import com.example.rlapp.ui.moduledetail.ModuleContentDetailViewActivity;
import com.google.gson.Gson;

import java.util.List;

public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.ViewHolder> {

    private String[] itemNames;
    private int[] itemImages;
    private LayoutInflater inflater;
    private Context ctx;
    private List<Content> contentList;
    public GridRecyclerViewAdapter(Context context, String[] itemNames, int[] itemImages, List<Content> contentList) {
        this.ctx = context;
        this.itemNames = itemNames;
        this.itemImages = itemImages;
        this.contentList = contentList;
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.grid_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(contentList.get(position).getTitle());
        holder.item_text1.setText(contentList.get(position).getContentType());

        if (contentList.get(position).getThumbnail().getUrl() != null && !contentList.get(position).getThumbnail().getUrl().isEmpty()) {
            Glide.with(ctx).load(ApiClient.CDN_URL_QA+contentList.get(position).getThumbnail().getUrl()).into(holder.imageView);
            Log.d("Image URL List", "list Url: " + ApiClient.CDN_URL_QA+contentList.get(position).getThumbnail().getUrl());
            Log.d("Image URL List", "list title: " + contentList.get(position).getTitle());
        }
        //holder.imageView.setImageResource(itemImages[position]);
        if (contentList.get(position).getContentType().equalsIgnoreCase("SERIES")){
            holder.img_iconview.setImageResource(R.drawable.ic_read_category);
        }else if (contentList.get(position).getContentType().equalsIgnoreCase("AUDIO")){
            holder.img_iconview.setImageResource(R.drawable.ic_sound_category);
        } else if (contentList.get(position).getContentType().equalsIgnoreCase("VIDEO")){
            holder.img_iconview.setImageResource(R.drawable.ic_read_play);
        }else {
            holder.img_iconview.setImageResource(R.drawable.ic_read_category);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String json = gson.toJson(contentList);
                Intent intent = new Intent(holder.itemView.getContext(), ModuleContentDetailViewActivity.class);
                intent.putExtra("Categorytype", contentList.get(position).getId());
                intent.putExtra("position", position);
                intent.putExtra("contentId", contentList.get(position).getId());
                holder.itemView.getContext().startActivity(intent);
            }
        });
        if (contentList.get(position).getContentType().equalsIgnoreCase("TEXT")) {
            holder.img_iconview.setImageResource(R.drawable.ic_read_category);
            holder.item_text1.setVisibility(View.GONE);
        } else if (contentList.get(position).getContentType().equalsIgnoreCase("VIDEO")) {
            holder.img_iconview.setImageResource(R.drawable.ic_read_play);
            holder.item_text1.setVisibility(View.GONE);
        } else if (contentList.get(position).getContentType().equalsIgnoreCase("SERIES")) {
            holder.img_iconview.setImageResource(R.drawable.ic_read_play);
            holder.item_text1.setVisibility(View.VISIBLE);
            holder.item_text1.setText("."+ String.valueOf(contentList.get(position).getEpisodeCount())+"."+ "videos");
        } else {
            holder.img_iconview.setImageResource(R.drawable.ic_sound_category);
            holder.item_text1.setVisibility(View.GONE);
        }
        if (contentList.get(position).getIsFavourited()) {
            holder.favorite_image.setImageResource(R.drawable.favstarsolid);
        }else {
            holder.favorite_image.setImageResource(R.drawable.favstar);
        }
        holder.favorite_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contentList.get(position).getIsFavourited()) {
                    holder.favorite_image.setImageResource(R.drawable.favstarsolid);
                }else {
                    holder.favorite_image.setImageResource(R.drawable.favstar);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,favorite_image,img_iconview;
        TextView textView,item_text1;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            textView = itemView.findViewById(R.id.item_text);
            item_text1 = itemView.findViewById(R.id.item_text1);
            img_iconview = itemView.findViewById(R.id.img_iconview);
            favorite_image = itemView.findViewById(R.id.favorite_image);



            //img_iconview.setImageResource(R.drawable.ic_read_category);
        }
    }
}

