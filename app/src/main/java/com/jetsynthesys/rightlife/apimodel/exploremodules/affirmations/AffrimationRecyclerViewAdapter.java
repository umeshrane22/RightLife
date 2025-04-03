package com.jetsynthesys.rightlife.apimodel.exploremodules.affirmations;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;
import com.jetsynthesys.rightlife.ui.Wellness.MoreContentDetailViewActivity;
import com.jetsynthesys.rightlife.ui.utility.svgloader.GlideApp;
import com.google.gson.Gson;

import java.util.List;

public class AffrimationRecyclerViewAdapter extends RecyclerView.Adapter<AffrimationRecyclerViewAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context ctx;
    List<Affirmation> affirmationList;

    public AffrimationRecyclerViewAdapter(ExploreAffirmationsListActivity exploreAffirmationsListActivity, List<Affirmation> affirmationList) {
        this.ctx = exploreAffirmationsListActivity;
        this.affirmationList = affirmationList;
        this.inflater = LayoutInflater.from(ctx);
    }


    @NonNull
    @Override
    public AffrimationRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_affirmationlist_quicklink, parent, false);
        return new AffrimationRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AffrimationRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.textView.setText(affirmationList.get(position).getTitle());
      //  holder.item_text1.setText(contentList.get(position).getContentType());

        if (affirmationList.get(position).getThumbnail().getUrl() != null && !affirmationList.get(position).getThumbnail().getUrl().isEmpty()) {
            GlideApp.with(ctx).load(ApiClient.CDN_URL_QA+affirmationList.get(position).getThumbnail().getUrl()).into(holder.imageView);
            Log.d("Image URL List", "list Url: " + ApiClient.CDN_URL_QA+affirmationList.get(position).getThumbnail().getUrl());
            Log.d("Image URL List", "list title: " + affirmationList.get(position).getTitle());
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ctx, "image clicked - "+holder.getBindingAdapterPosition(), Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                String json = gson.toJson(affirmationList);
                Intent intent = new Intent(holder.itemView.getContext(), MoreContentDetailViewActivity.class);
                intent.putExtra("Categorytype", json);
                intent.putExtra("position", position);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return affirmationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.banner_image_affirmation);
            textView = itemView.findViewById(R.id.txt_title_affirmation);
        }
    }
}
