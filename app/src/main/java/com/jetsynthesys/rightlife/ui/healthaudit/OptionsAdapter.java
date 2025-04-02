package com.jetsynthesys.rightlife.ui.healthaudit;

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
import com.jetsynthesys.rightlife.ui.healthaudit.questionlist.Option;

import java.util.List;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder> {

    private List<Option> optionList;
    private OnItemClickListener onItemClickListener;
    private boolean isMultipleSelection = false;
    private int selectedItemPos = -1;
    private int lastItemSelectedPos = -1;

    public OptionsAdapter(List<Option> options, OnItemClickListener onItemClickListener, boolean isMultipleSelection) {
        this.optionList = options;
        this.onItemClickListener = onItemClickListener;
        this.isMultipleSelection = isMultipleSelection;
    }

    @NonNull
    @Override
    public OptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        return new OptionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsViewHolder holder, int position) {
        Option option = optionList.get(position);
        holder.optionText.setText(option.getOptionText());
        holder.favoriteCheckbox.setChecked(option.isSelected());
        ColorStateList colorStateList;
        if (isMultipleSelection) {
            if (option.isSelected()) {
                colorStateList = ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.healthauditgreen);
                //   holder.bgrelative.setBackgroundTintList(ContextCompat.getColor(holder.itemView.getContext(), R.color.healthauditgreen));
            } else {
                colorStateList = ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.white);
                // holder.bgrelative.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            }
            holder.bgrelative.setBackgroundTintList(colorStateList);
        }else {
            if(position == selectedItemPos)
                colorStateList = ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.healthauditgreen);
            else
                colorStateList = ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.white);
            holder.bgrelative.setBackgroundTintList(colorStateList);
        }


        holder.itemView.setOnClickListener(v -> {
            if (isMultipleSelection) {
                if (option.getOptionText().equalsIgnoreCase("None")){
                    for(int i = 0; i<optionList.size(); i++){
                        optionList.get(i).setSelected(false);
                        notifyDataSetChanged();
                    }
                } else {
                    for(int i = 0; i<optionList.size(); i++){
                        if (optionList.get(i).getOptionText().equalsIgnoreCase("None")) {
                            optionList.get(i).setSelected(false);
                            notifyItemChanged(i);
                        }
                    }
                }
                option.setSelected(!option.isSelected());
                notifyItemChanged(position);
                onItemClickListener.onItemClick(option);
            }else {
                selectedItemPos = holder.getAdapterPosition();
                if(lastItemSelectedPos == -1)
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

    public interface OnItemClickListener {
        void onItemClick(Option option);
    }

    static class OptionsViewHolder extends RecyclerView.ViewHolder {
        TextView optionText;
        CheckBox favoriteCheckbox;
        RelativeLayout bgrelative;

        public OptionsViewHolder(@NonNull View itemView) {
            super(itemView);
            optionText = itemView.findViewById(R.id.fruit_name);
            favoriteCheckbox = itemView.findViewById(R.id.favorite_checkbox);
            bgrelative = itemView.findViewById(R.id.bgrelative);
        }
    }


}
