package com.jetsynthesys.rightlife.ui.mindaudit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.R;

import java.util.ArrayList;

public class OtherAssessmentsAdapter extends RecyclerView.Adapter<OtherAssessmentsAdapter.SuggestedAssessmentViewHolder> {

    private Context context;
    private ArrayList<String> suggestedAssessment;
    private OnItemClickListener onItemClickListener;

    public OtherAssessmentsAdapter(Context context, ArrayList<String> suggestedAssessment, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.suggestedAssessment = suggestedAssessment;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SuggestedAssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_other_suggested_assessment, parent, false);
        return new SuggestedAssessmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedAssessmentViewHolder holder, int position) {
        String sa = suggestedAssessment.get(position);
        holder.tvSuggestedAssessment.setText(sa);
        switch (sa){
            case "DASS-21": {
                holder.imageView.setImageResource(R.drawable.dass_21);
                break;
            }
            case "Sleep Audit": {
                holder.imageView.setImageResource(R.drawable.sleep_audit);
                break;
            }
            case "GAD-7": {
                holder.imageView.setImageResource(R.drawable.gad_7);
                break;
            }
            case "OHQ": {
                holder.imageView.setImageResource(R.drawable.ohq);
                break;
            }
            case "CAS": {
                holder.imageView.setImageResource(R.drawable.cas);
                break;
            }
            case "PHQ-9": {
                holder.imageView.setImageResource(R.drawable.phq_9);
                break;
            }

        }
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

        TextView tvSuggestedAssessment,tvSuggestedAssessmentDesc;
        ImageView imageView;

        public SuggestedAssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSuggestedAssessment = itemView.findViewById(R.id.tv_suggested_assessment_title);
            tvSuggestedAssessmentDesc = itemView.findViewById(R.id.tv_suggested_assessment_desc);
            imageView = itemView.findViewById(R.id.img_suggested_assessment);
        }
    }
}
