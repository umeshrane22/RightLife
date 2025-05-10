package com.jetsynthesys.rightlife.ui.Articles;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;
import com.jetsynthesys.rightlife.apimodel.morelikecontent.Like;
import com.jetsynthesys.rightlife.databinding.LayoutArticleMorelikeItemBinding;
import com.jetsynthesys.rightlife.ui.contentdetailvideo.ContentDetailsActivity;
import com.jetsynthesys.rightlife.ui.contentdetailvideo.SeriesListActivity;
import com.jetsynthesys.rightlife.ui.utility.DateTimeUtils;
import com.jetsynthesys.rightlife.ui.utility.Utils;

import java.util.List;

public class ArticleDetailMoreLikeAdapter extends RecyclerView.Adapter<ArticleDetailMoreLikeAdapter.ViewHolder> {

    private final List<Like> contentList;
    private final Context ctx;
    private final LayoutInflater inflater;

    public ArticleDetailMoreLikeAdapter(Context context, List<Like> contentList) {
        this.ctx = context;
        this.contentList = contentList;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutArticleMorelikeItemBinding binding = LayoutArticleMorelikeItemBinding.inflate(inflater, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Like item = contentList.get(position);

        holder.binding.tvItemTitle.setText(item.getTitle());

        if (item.getThumbnail() != null && item.getThumbnail().getUrl() != null && !item.getThumbnail().getUrl().isEmpty()) {
            Glide.with(ctx)
                    .load(ApiClient.CDN_URL_QA + item.getThumbnail().getUrl())
                    .into(holder.binding.imgContent);
        }

        //holder.binding.tvArtistName.setText(item.getArtist().get(0).getFirstName());
        setArtistname(item,holder);
        holder.binding.tvDate.setText(DateTimeUtils.convertAPIDateMonthFormat(item.getCreatedAt()));
        int color = Utils.getModuleColor(ctx, item.getModuleId());
        //holder.binding.rlModulename.setBackgroundTintList(ColorStateList.valueOf(color));

        String contentType = item.getContentType();
        if ("TEXT".equalsIgnoreCase(contentType)) {
            holder.binding.imgContentType.setImageResource(R.drawable.ic_text_content);
            //holder.binding.itemTypeText.setVisibility(View.GONE);
        } else if ("VIDEO".equalsIgnoreCase(contentType)) {
            holder.binding.imgContentType.setImageResource(R.drawable.ic_video_content);
            //holder.binding.itemTypeText.setVisibility(View.GONE);
        } else if ("SERIES".equalsIgnoreCase(contentType)) {
            holder.binding.imgContentType.setImageResource(R.drawable.ic_series_content);
            //holder.binding.itemTypeText.setVisibility(View.VISIBLE);
            //holder.binding.itemTypeText.setText(item.getEpisodeCount() + " . " + item.getSeriesType());
        } else {
            holder.binding.imgContentType.setImageResource(R.drawable.ic_series_content);
            //holder.binding.itemTypeText.setVisibility(View.GONE);
        }

        //holder.binding.favoriteImage.setImageResource(item.getIsFavourited() ? R.drawable.favstarsolid : R.drawable.unfavorite);

   /*     holder.binding.favoriteImage.setOnClickListener(v -> {
            FavouriteRequest favouriteRequest = new FavouriteRequest();
            favouriteRequest.setFavourite(!item.getIsFavourited());
            favouriteRequest.setEpisodeId("");
            Utils.addToFavourite(ctx, item.getId(), favouriteRequest, new OnFavouriteClickListener() {
                @Override
                public void onSuccess(boolean isSuccess, String message) {
                    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
                    if (isSuccess) {
                        item.setIsFavourited(!item.getIsFavourited());
                        notifyItemChanged(position);
                    }
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(ctx, error, Toast.LENGTH_SHORT).show();
                }
            });
        });*/

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            if ("TEXT".equalsIgnoreCase(contentType)) {
                intent = new Intent(ctx, ArticlesDetailActivity.class);
            } else if ("VIDEO".equalsIgnoreCase(contentType)) {
                intent = new Intent(ctx, ContentDetailsActivity.class);
            } else if ("SERIES".equalsIgnoreCase(contentType)) {
                intent = new Intent(ctx, SeriesListActivity.class);
            } else {
                return;
            }
            intent.putExtra("contentId", item.getId());
            ctx.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LayoutArticleMorelikeItemBinding binding;

        public ViewHolder(LayoutArticleMorelikeItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void setArtistname(Like contentResponseObj, ViewHolder holder) {
        if (holder.binding != null && holder.binding.tvArtistName != null && contentResponseObj != null
                && contentResponseObj != null && contentResponseObj.getArtist() != null
                && contentResponseObj.getArtist().size()>0) {

            String name = "";
            if (contentResponseObj.getArtist().get(0).getFirstName() != null) {
                name = contentResponseObj.getArtist().get(0).getFirstName();
            }
            if (contentResponseObj.getArtist().get(0).getLastName() != null) {
                name += (name.isEmpty() ? "" : " ") + contentResponseObj.getArtist().get(0).getLastName();
            }

            holder.binding.tvArtistName.setText(name);
        } else if (holder.binding != null && holder.binding.tvArtistName != null) {
            holder.binding.tvArtistName.setText(""); // or set some default value
        }
    }
}
