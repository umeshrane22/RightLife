package com.example.rlapp.ui.mindaudit;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.ui.mindaudit.questions.ScoringPattern;

import java.util.ArrayList;

public class MindAuditOptionsAdapter extends RecyclerView.Adapter<MindAuditOptionsAdapter.OptionsViewHolder> {
    private Context context;
    private ArrayList<ScoringPattern> scoringPatterns;
    private OnItemClickListener onItemClickListener;
    private int selectedItemPos = -1;
    private int lastItemSelectedPos = -1;

    public MindAuditOptionsAdapter(Context context, ArrayList<ScoringPattern> scoringPatterns, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.scoringPatterns = scoringPatterns;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public OptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        return new MindAuditOptionsAdapter.OptionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsViewHolder holder, int position) {

        ScoringPattern scoringPattern = scoringPatterns.get(position);
        holder.optionText.setText(scoringPattern.getOption());

        if (position == selectedItemPos)
            holder.bgrelative.setBackgroundDrawable(holder.itemView.getContext().getDrawable(R.drawable.roundedcornerpinkborder_selected));
        else
            holder.bgrelative.setBackgroundDrawable(holder.itemView.getContext().getDrawable(R.drawable.roundedcornerpinkborder));


        holder.itemView.setOnClickListener(view -> {
            selectedItemPos = holder.getAdapterPosition();
            if (lastItemSelectedPos == -1)
                lastItemSelectedPos = selectedItemPos;
            else {
                notifyItemChanged(lastItemSelectedPos);
                lastItemSelectedPos = selectedItemPos;
            }
            notifyItemChanged(selectedItemPos);
            onItemClickListener.onItemClick(scoringPattern);
        });
    }

    @Override
    public int getItemCount() {
        return scoringPatterns.size();
    }

    public interface OnItemClickListener {
        void onItemClick(ScoringPattern scoringPattern);
    }

    static class OptionsViewHolder extends RecyclerView.ViewHolder {
        TextView optionText;
        RelativeLayout bgrelative;

        public OptionsViewHolder(@NonNull View itemView) {
            super(itemView);
            optionText = itemView.findViewById(R.id.fruit_name);
            bgrelative = itemView.findViewById(R.id.bgrelative);
        }
    }
}
