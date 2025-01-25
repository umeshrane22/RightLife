package com.example.rlapp.ui.exploremodule;

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
import com.example.rlapp.apimodel.exploremodules.Suggested;
import com.example.rlapp.apimodel.morelikecontent.Like;
import com.example.rlapp.ui.Wellness.MoreContentDetailViewActivity;
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

        if (contentList.get(position).getThumbnail().getUrl() != null && !contentList.get(position).getThumbnail().getUrl().isEmpty()) {
            Glide.with(ctx).load(ApiClient.CDN_URL_QA+contentList.get(position).getThumbnail().getUrl()).into(holder.imageView);
            Log.d("Image URL List", "list Url: " + ApiClient.CDN_URL_QA+contentList.get(position).getThumbnail().getUrl());
            Log.d("Image URL List", "list title: " + contentList.get(position).getTitle());
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
                holder.itemView.getContext().startActivity(intent);
            }
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

