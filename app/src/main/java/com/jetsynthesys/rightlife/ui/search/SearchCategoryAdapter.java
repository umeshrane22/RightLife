package com.jetsynthesys.rightlife.ui.search;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;

public class SearchCategoryAdapter extends RecyclerView.Adapter<SearchCategoryAdapter.CategoryViewHolder> {

    private Context context;
    private SearchData searchData;
    private OnItemClickListener onItemClickListener;

    public SearchCategoryAdapter(Context context, SearchData searchData, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.searchData = searchData;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        SearchResult searchResult = searchData.getResult().get(position);
        holder.tvCategory.setText(searchResult.getName());
        Glide.with(context)
                .load(ApiClient.CDN_URL_QA + searchResult.getImageUrl())
                .placeholder(R.drawable.rl_placeholder)
                .error(R.drawable.rl_placeholder)
                .into(holder.imgCategory);

        ColorStateList colorStateList;
        switch (searchResult.getModuleId()) {
            case "SLEEP_RIGHT":
                colorStateList = ContextCompat.getColorStateList(context, R.color.light_blue);
                break;

            case "MOVE_RIGHT":
                colorStateList = ContextCompat.getColorStateList(context, R.color.light_red);
                break;

            case "THINK_RIGHT":
                colorStateList = ContextCompat.getColorStateList(context, R.color.light_yellow);
                break;

            case "EAT_RIGHT":
                colorStateList = ContextCompat.getColorStateList(context, R.color.light_green);
                break;

            case "HOME_PAGE":
                colorStateList = ContextCompat.getColorStateList(context, R.color.black);
                break;

            case "HEALTH":
                colorStateList = ContextCompat.getColorStateList(context, R.color.black);
                break;

            default:
                colorStateList = ContextCompat.getColorStateList(context, R.color.dark_blue);

        }
        holder.llPopularCategory.setBackgroundTintList(colorStateList);

        holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(searchResult));
    }

    @Override
    public int getItemCount() {
        return searchData.getResult().size();
    }

    public interface OnItemClickListener {
        void onItemClick(SearchResult searchResult);
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView tvCategory;
        LinearLayout llPopularCategory;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.img_category);
            tvCategory = itemView.findViewById(R.id.tv_category);
            llPopularCategory = itemView.findViewById(R.id.ll_popular_category);
        }
    }
}
