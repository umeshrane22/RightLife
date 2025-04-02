package com.jetsynthesys.rightlife.ui.voicescan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.ui.healthaudit.questionlist.Option;

import java.util.List;

class FeelingListAdapter extends RecyclerView.Adapter<FeelingListAdapter.FeelingViewHolder> {
    private List<Option> feelingList;
    private OnItemClickListener onItemClickListener;
    private Context context;

    public FeelingListAdapter(Context context, List<Option> feelingList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.feelingList = feelingList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override

    public FeelingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feeling_list, parent, false);
        return new FeelingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeelingViewHolder holder, int position) {
        Option option = feelingList.get(position);
        String feeling = option.getOptionText();
        holder.tvFeelingTitle.setText(feeling);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, -25, 0, 0);

        switch (feeling) {
            case "Sad":
                holder.rlFeeling.setBackground(context.getDrawable(R.drawable.toproundedvoicescan_sad));
                holder.imgFeeling.setImageResource(R.drawable.vociesscan_sad);
                break;
            case "Stressed":
                holder.rlFeeling.setBackground(context.getDrawable(R.drawable.toproundedvoicescan_stressed));
                holder.imgFeeling.setImageResource(R.drawable.voicescan_stressed);
                holder.linearLayout.setLayoutParams(params);
                break;
            case "Unsure":
                holder.rlFeeling.setBackground(context.getDrawable(R.drawable.toproundedvoicescan_unsure));
                holder.imgFeeling.setImageResource(R.drawable.voicescan_unsure);
                holder.linearLayout.setLayoutParams(params);
                break;
            case "Relaxed":
                holder.rlFeeling.setBackground(context.getDrawable(R.drawable.toproundedvoicescan_relaxed));
                holder.imgFeeling.setImageResource(R.drawable.voicescan_relaxed);
                holder.linearLayout.setLayoutParams(params);
                break;
            case "Happy":
                holder.rlFeeling.setBackground(context.getDrawable(R.drawable.toproundedvoicescan_happy));
                holder.imgFeeling.setImageResource(R.drawable.voicescan_happy);
                holder.linearLayout.setLayoutParams(params);
                break;
            default:
                holder.rlFeeling.setBackground(context.getDrawable(R.drawable.toproundedvoicescan_sad));
        }

        holder.itemView.setOnClickListener(view -> {
            onItemClickListener.onItemClick(option);
        });
    }

    @Override
    public int getItemCount() {
        return feelingList.size();
    }

    static class FeelingViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlFeeling;
        TextView tvFeelingTitle;
        ImageView imgFeeling;
        LinearLayout linearLayout;

        public FeelingViewHolder(@NonNull View itemView) {
            super(itemView);
            rlFeeling = itemView.findViewById(R.id.rl_feeling);
            tvFeelingTitle = itemView.findViewById(R.id.txt_title_feeling);
            imgFeeling = itemView.findViewById(R.id.img_feeling);
            linearLayout = itemView.findViewById(R.id.ll_feeling_list);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Option option);
    }
}
