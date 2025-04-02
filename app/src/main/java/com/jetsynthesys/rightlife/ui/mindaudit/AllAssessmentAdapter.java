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

public class AllAssessmentAdapter extends RecyclerView.Adapter<AllAssessmentAdapter.AllAssessmentViewHolder> {

    private Context context;
    private ArrayList<String> allAssessment;
    private OnItemClickListener onItemClickListener;

    public AllAssessmentAdapter(Context context, ArrayList<String> suggestedAssessment, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.allAssessment = suggestedAssessment;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public AllAssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_all_assessment, parent, false);
        return new AllAssessmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllAssessmentViewHolder holder, int position) {
        String all = allAssessment.get(position);
        holder.tvAllAssessment.setText(all);
        switch (all){
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
        holder.itemView.setOnClickListener(view -> {
            onItemClickListener.onItemClick(all);
        });
    }

    public interface OnItemClickListener {
        void onItemClick(String assessment);
    }

    @Override
    public int getItemCount() {
        return allAssessment.size();
    }

    static class AllAssessmentViewHolder extends RecyclerView.ViewHolder {

        TextView tvAllAssessment;
        ImageView imageView;

        public AllAssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAllAssessment = itemView.findViewById(R.id.tv_all_assessment_row);
            imageView = itemView.findViewById(R.id.img_all_assessment);
        }
    }
}
