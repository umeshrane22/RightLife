package com.jetsynthesys.rightlife.ui.voicescan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.ui.healthaudit.OptionsAdapter;
import com.jetsynthesys.rightlife.ui.healthaudit.questionlist.Option;

import java.util.List;

class FeelingReasonsListAdapter extends RecyclerView.Adapter<FeelingReasonsListAdapter.FeelingReasonOptionsViewHolder> {

    private List<Option> optionList;
    private OptionsAdapter.OnItemClickListener onItemClickListener;
    private Context context;

    public FeelingReasonsListAdapter(Context context, List<Option> options, OptionsAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.optionList = options;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FeelingReasonOptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feeling_reason_list, parent, false);
        return new FeelingReasonOptionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeelingReasonOptionsViewHolder holder, int position) {

        Option option = optionList.get(position);

        holder.tvFeelingReason.setText(option.getOptionText());

        if (option.isSelected()) {
            holder.tvFeelingReason.setBackground(context.getDrawable(R.drawable.roundedcornershape_selected));
            holder.tvFeelingReason.setTextColor(context.getColor(R.color.white));
        } else {
            holder.tvFeelingReason.setBackground(context.getDrawable(R.drawable.roundedcorneryellowbordervoicescan));
            holder.tvFeelingReason.setTextColor(context.getColor(R.color.black));
        }

        holder.itemView.setOnClickListener(view -> {
            option.setSelected(!option.isSelected());
            notifyItemChanged(position);
            onItemClickListener.onItemClick(option);
        });

    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Option option);
    }

    static class FeelingReasonOptionsViewHolder extends RecyclerView.ViewHolder {
        TextView tvFeelingReason;

        public FeelingReasonOptionsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFeelingReason = itemView.findViewById(R.id.tv_reason_option);
        }
    }
}
