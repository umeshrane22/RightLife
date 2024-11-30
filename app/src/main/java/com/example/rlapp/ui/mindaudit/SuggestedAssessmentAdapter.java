package com.example.rlapp.ui.mindaudit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;

import java.util.ArrayList;

public class SuggestedAssessmentAdapter extends RecyclerView.Adapter<SuggestedAssessmentAdapter.SuggestedAssessmentViewHolder> {

    private Context context;
    private ArrayList<String> suggestedAssessment;
    private OnItemClickListener onItemClickListener;

    public SuggestedAssessmentAdapter(Context context, ArrayList<String> suggestedAssessment, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.suggestedAssessment = suggestedAssessment;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SuggestedAssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_suggested_assessment, parent, false);
        return new SuggestedAssessmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedAssessmentViewHolder holder, int position) {
        String sa = suggestedAssessment.get(position);
        holder.tvSuggestedAssessment.setText(sa);
        //holder.imageView.setImageResource();
        holder.itemView.setOnClickListener(view -> onItemClickListener.onItemClick(sa));
    }

    @Override
    public int getItemCount() {
        return suggestedAssessment.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String assessment);
    }

    static class SuggestedAssessmentViewHolder extends RecyclerView.ViewHolder {

        TextView tvSuggestedAssessment;
        ImageView imageView;

        public SuggestedAssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSuggestedAssessment = itemView.findViewById(R.id.tv_suggested_assessment);
            imageView = itemView.findViewById(R.id.img_suggested_assessment);
        }
    }
}
