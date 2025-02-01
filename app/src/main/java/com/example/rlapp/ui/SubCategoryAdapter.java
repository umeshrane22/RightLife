package com.example.rlapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;

import java.util.List;

class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {

    private Context context;
    private List<SubCategoryResult> subCategoryResultList;
    private OnItemClickListener onItemClickListener;

    public SubCategoryAdapter(Context context, List<SubCategoryResult> subCategoryResultList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.subCategoryResultList = subCategoryResultList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subcategory, parent, false);
        return new SubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        SubCategoryResult subCategoryResult = subCategoryResultList.get(position);
        holder.tvSubCategory.setText(subCategoryResult.getName());
        holder.rdSubCategory.setChecked(subCategoryResult.getSelected());

        holder.itemView.setOnClickListener(view -> {
            subCategoryResult.setSelected(!subCategoryResult.getSelected());
            onItemClickListener.onItemClick(subCategoryResult);
            notifyItemChanged(position);
        });

        /*holder.rdSubCategory.setOnCheckedChangeListener((compoundButton, b) -> {
            onItemClickListener.onItemClick(subCategoryResult);
            holder.rdSubCategory.setSelected(!subCategoryResult.getSelected());
            notifyItemChanged(position);
        });*/
    }

    @Override
    public int getItemCount() {
        return subCategoryResultList.size();
    }

    public void clearAll() {
        for (int i = 0; i < subCategoryResultList.size(); i++) {
            subCategoryResultList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(SubCategoryResult subCategoryResult);
    }

    static class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubCategory;
        RadioButton rdSubCategory;

        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubCategory = itemView.findViewById(R.id.tv_subcategory);
            rdSubCategory = itemView.findViewById(R.id.rd_subcategory);
        }
    }
}
