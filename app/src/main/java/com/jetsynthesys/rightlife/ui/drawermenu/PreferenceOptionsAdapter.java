package com.jetsynthesys.rightlife.ui.drawermenu;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.R;

import java.util.List;

public class PreferenceOptionsAdapter extends RecyclerView.Adapter<PreferenceOptionsAdapter.OptionViewHolder> {

    private List<PreferenceOption> optionList;
    private boolean isMultipleSelection = false;
    private int selectedItemPos = -1;
    private int lastItemSelectedPos = -1;
    private OnItemClickListener onItemClickListener;
    private OnboardingPrompt onboardingPrompt;

    public PreferenceOptionsAdapter(OnboardingPrompt onboardingPrompt, boolean isMultipleSelection, OnItemClickListener onItemClickListener) {
        this.onboardingPrompt = onboardingPrompt;
        this.optionList = onboardingPrompt.getOptions();
        this.isMultipleSelection = isMultipleSelection;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public OptionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_preference_option, parent, false);
        return new OptionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionViewHolder holder, int position) {
        PreferenceOption option = optionList.get(position);
        holder.tvOptionName.setText(option.getDesc());

        holder.checkBox.setChecked(option.getIsAnswered());
        ColorStateList colorStateList;
        if (isMultipleSelection) {
            if (option.getIsAnswered()) {
                colorStateList = getColor(holder.itemView.getContext(), onboardingPrompt.getAppId());
                holder.tvOptionName.setTextColor(holder.itemView.getContext().getColor(R.color.white));
            } else {
                colorStateList = ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.white);
                holder.tvOptionName.setTextColor(holder.itemView.getContext().getColor(R.color.black));
            }
            holder.rlPreferenceOptions.setBackgroundTintList(colorStateList);
        } else {
            if (position == selectedItemPos) {
                holder.tvOptionName.setTextColor(holder.itemView.getContext().getColor(R.color.white));
                colorStateList = getColor(holder.itemView.getContext(), onboardingPrompt.getAppId());
            }
            else {
                holder.tvOptionName.setTextColor(holder.itemView.getContext().getColor(R.color.black));
                colorStateList = ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.white);
            }
            holder.rlPreferenceOptions.setBackgroundTintList(colorStateList);
        }

        holder.itemView.setOnClickListener(v -> {
            if (isMultipleSelection) {
                option.setIsAnswered(!option.getIsAnswered());
                notifyItemChanged(position);
                onItemClickListener.onItemClick(option);
            } else {
                selectedItemPos = holder.getAdapterPosition();
                if (lastItemSelectedPos == -1)
                    lastItemSelectedPos = selectedItemPos;
                else {
                    notifyItemChanged(lastItemSelectedPos);
                    lastItemSelectedPos = selectedItemPos;
                }
                notifyItemChanged(selectedItemPos);
                onItemClickListener.onItemClick(option);
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    private ColorStateList getColor(Context context, String appId) {
        switch (appId) {
            case "THINK_RIGHT":
                return ContextCompat.getColorStateList(context, R.color.medium_yellow);
            case "MOVE_RIGHT":
                return ContextCompat.getColorStateList(context, R.color.medium_red);
            case "EAT_RIGHT":
                return ContextCompat.getColorStateList(context, R.color.medium_green);
            case "SLEEP_RIGHT":
                ContextCompat.getColorStateList(context, R.color.medium_blue);

            default:
                return ContextCompat.getColorStateList(context, R.color.medium_pink);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(PreferenceOption option);
    }

    static class OptionViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlPreferenceOptions;
        TextView tvOptionName;
        CheckBox checkBox;

        public OptionViewHolder(@NonNull View itemView) {
            super(itemView);
            rlPreferenceOptions = itemView.findViewById(R.id.rl_preference_options);
            tvOptionName = itemView.findViewById(R.id.option_name);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
