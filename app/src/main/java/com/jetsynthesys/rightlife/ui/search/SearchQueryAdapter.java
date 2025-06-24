package com.jetsynthesys.rightlife.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;

import java.util.List;

public class SearchQueryAdapter extends RecyclerView.Adapter<SearchQueryAdapter.SearchQueryViewHolder> {
    private Context context;
    private String adapterType;
    private SearchQueryResults searchQueryResults;
    private OnItemClickListener onItemClickListener;

    public SearchQueryAdapter(Context context, String adapterType, SearchQueryResults searchQueryResults, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.adapterType = adapterType;
        this.searchQueryResults = searchQueryResults;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SearchQueryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_search_query, parent, false);
        return new SearchQueryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchQueryViewHolder holder, int position) {

        switch (adapterType) {
            case "artists": {
                Artist artist = searchQueryResults.getArtists().get(position);
                holder.tvTitle.setText(artist.getFirstName() + " " + artist.getLastName());
                holder.tvContentType.setText("Artist");
                holder.standingLine.setVisibility(View.GONE);
                Glide.with(context)
                        .load(ApiClient.CDN_URL_QA + artist.getProfilePicture())
                        .placeholder(R.drawable.rl_placeholder)
                        .error(R.drawable.rl_placeholder)
                        .into(holder.imageView);
                holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(artist.getId(), artist.getId()));
                break;
            }
            case "contents": {
                Content content = searchQueryResults.getContents().get(position);
                holder.tvTitle.setText(content.getTitle());
                holder.tvContentType.setText(content.getContentType());
                holder.tvModuleName.setText(content.getModule());
                Glide.with(context)
                        .load(ApiClient.CDN_URL_QA + content.getUrl())
                        .placeholder(R.drawable.rl_placeholder)
                        .error(R.drawable.rl_placeholder)
                        .into(holder.imageView);
                holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(content.getId(), content.getId()));
                break;
            }
            case "instructorProfiles": {
                InstructorProfile instructorProfile = searchQueryResults.getInstructorProfiles().get(position);
                holder.tvTitle.setText(instructorProfile.getFirstName() + " "+instructorProfile.getLastName());
                holder.tvModuleName.setText(instructorProfile.getModule());
                holder.standingLine.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(instructorProfile.getId(), instructorProfile.getId()));
                break;
            }
            case "events": {
                List<Object> events = searchQueryResults.getEvents();
                break;
            }
            case "services": {
                Service service = searchQueryResults.getServices().get(position);
                holder.tvTitle.setText(service.getTitle());
                holder.tvModuleName.setText(service.getModule());
                holder.standingLine.setVisibility(View.GONE);
                Glide.with(context)
                        .load(ApiClient.CDN_URL_QA + service.getImageUrl())
                        .placeholder(R.drawable.rl_placeholder)
                        .error(R.drawable.rl_placeholder)
                        .into(holder.imageView);
                holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(service.getId(), service.getId()));
                break;
            }

        }
    }

    @Override
    public int getItemCount() {
        switch (adapterType) {
            case "artists":
                return searchQueryResults.getArtists().size();
            case "contents":
                return searchQueryResults.getContents().size();
            case "instructorProfiles":
                return searchQueryResults.getInstructorProfiles().size();
            case "events":
                return searchQueryResults.getEvents().size();
            case "services":
                return searchQueryResults.getServices().size();
            default:
                return 0;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(String categoryId, String contentId);
    }

    static class SearchQueryViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvContentType, tvModuleName, tvTitle;
        View standingLine;

        public SearchQueryViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_row_search_query);
            tvContentType = itemView.findViewById(R.id.tv_content_type);
            tvModuleName = itemView.findViewById(R.id.tv_module_name);
            tvTitle = itemView.findViewById(R.id.title);
            standingLine = itemView.findViewById(R.id.standing_line);
        }
    }
}
