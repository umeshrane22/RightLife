package com.jetsynthesys.rightlife.ui.healthcam;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import com.google.gson.Gson;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.jetsynthesys.rightlife.apimodel.newreportfacescan.Recommendation;
import com.jetsynthesys.rightlife.ui.Wellness.MoreContentDetailViewActivity;
import com.jetsynthesys.rightlife.ui.contentdetailvideo.ContentDetailsActivity;
import com.jetsynthesys.rightlife.ui.contentdetailvideo.SeriesListActivity;
import com.jetsynthesys.rightlife.ui.utility.Utils; // Import your Utils class

import java.util.List;

public class HealthCamRecommendationAdapter extends RecyclerView.Adapter<HealthCamRecommendationAdapter.RecommendationViewHolder> {

    private Context context;
    private List<Recommendation> recommendations;

    public HealthCamRecommendationAdapter(Context context, List<Recommendation> recommendations) {
        this.context = context;
        this.recommendations = recommendations;
    }

    @NonNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.health_cam_recommendation_item, parent, false);
        return new RecommendationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
        Recommendation recommendation = recommendations.get(position);

        holder.titleTextView.setText(recommendation.title);
        holder.categoryTextView.setText(recommendation.categoryName);

        if (recommendation.artist != null && !recommendation.artist.isEmpty()) {
            String artistName = recommendation.artist.get(0).firstName + " " + recommendation.artist.get(0).lastName;
            holder.artistTextView.setText(artistName);
        } else {
            holder.artistTextView.setText("");
        }

        if (recommendation.thumbnail != null && recommendation.thumbnail.url != null && !recommendation.thumbnail.url.isEmpty()) {
            Glide.with(context)
                    .load(ApiClient.CDN_URL_QA + recommendation.thumbnail.url)
                    .transform(new RoundedCorners(25))
                    .into(holder.thumbnailImageView);
            Log.d("RecommendationAdapter", "Thumbnail URL: " + ApiClient.CDN_URL_QA + recommendation.thumbnail.url);
        } else {
            holder.thumbnailImageView.setImageResource(R.drawable.image1_rlpage_edit); // Default image
        }

        holder.moduleNameTextView.setText(Utils.getModuleText(recommendation.moduleId));
        Drawable startDrawable = getModuleTypeDrawable(context, recommendation.moduleId);
        startDrawable.setBounds(0, 0, startDrawable.getIntrinsicWidth(), startDrawable.getIntrinsicHeight());
        holder.moduleNameTextView.setCompoundDrawables(startDrawable, null, null, null);

        String text = "";

        if ("SERIES".equals(recommendation.contentType)) {
            text = (recommendation.episodeCount) + "  videos";// " videos/audios";
        } else {
            text = "";//contentList.get(position).getLeftDuration() + " left";
        }

       /* if ("SERIES".equals(recommendation.seriesType)) {
            text = recommendation.episodeCount + " episodes";
        } else {
            // You might need a way to determine "left duration" for other content types
            text = "";
        }*/
        holder.timeLeftTextView.setText(text);

        holder.itemView.setOnClickListener(view -> {
            //Toast.makeText(ctx, "image clicked - " + holder.getBindingAdapterPosition(), Toast.LENGTH_SHORT).show();
            Gson gson = new Gson();
            String json = gson.toJson(recommendations);
            if ("SERIES".equals(recommendation.contentType)) {
                Intent intent = new Intent(holder.itemView.getContext(), SeriesListActivity.class);
                intent.putExtra("contentId", recommendation.id);
                holder.itemView.getContext().startActivity(intent);
            }else {
                Intent intent = new Intent(holder.itemView.getContext(), ContentDetailsActivity.class);
                intent.putExtra("contentId", recommendation.id);
                holder.itemView.getContext().startActivity(intent);
            }
            /*Intent intent = new Intent(holder.itemView.getContext(), MoreContentDetailViewActivity.class);
            intent.putExtra("Categorytype", json);
            intent.putExtra("position", position);
            intent.putExtra("contentId", recommendations.get(position).id);
            holder.itemView.getContext().startActivity(intent);*/
        });
    }

    private Drawable getModuleTypeDrawable(Context ctx, String moduleName) {
        Drawable startDrawable = ContextCompat.getDrawable(ctx, R.drawable.rlpage_thinkright_svg);
        if ("THINK_RIGHT".equalsIgnoreCase(moduleName)) {
            return ContextCompat.getDrawable(ctx, R.drawable.rlpage_thinkright_svg);
        } else if ("SLEEP_RIGHT".equalsIgnoreCase(moduleName)) {
            return ContextCompat.getDrawable(ctx, R.drawable.rlpage_sleepright_svg);
        } else if ("MOVE_RIGHT".equalsIgnoreCase(moduleName)) {
            return ContextCompat.getDrawable(ctx, R.drawable.rlpage_moveright_svg);
        } else if ("EAT_RIGHT".equalsIgnoreCase(moduleName)) {
            return ContextCompat.getDrawable(ctx, R.drawable.rlpage_eatright_svg);
        }
        return startDrawable;
    }

    @Override
    public int getItemCount() {
        return recommendations.size();
    }

    public static class RecommendationViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView titleTextView, categoryTextView, artistTextView, timeLeftTextView, moduleNameTextView;

        public RecommendationViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.rlimg1);
            titleTextView = itemView.findViewById(R.id.item_text);
            categoryTextView = itemView.findViewById(R.id.tv_category);
            artistTextView = itemView.findViewById(R.id.tv_artistname);
            timeLeftTextView = itemView.findViewById(R.id.tvTimeLeft);
            moduleNameTextView = itemView.findViewById(R.id.tv_modulename);
        }
    }
}
