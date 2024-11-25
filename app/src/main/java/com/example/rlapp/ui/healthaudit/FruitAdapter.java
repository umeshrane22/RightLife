package com.example.rlapp.ui.healthaudit;

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

import com.example.rlapp.R;

import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.FruitViewHolder> {
    private List<Fruit> fruitList;
    private OnItemClickListener listener;

    public FruitAdapter(List<Fruit> fruitList, OnItemClickListener listener) {
        this.fruitList = fruitList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        return new FruitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder holder, int position) {
        Fruit fruit = fruitList.get(position);
        holder.fruitName.setText(fruit.getName());
        holder.favoriteCheckbox.setChecked(fruit.isSelected());

        if (fruit.isSelected()) {

            ColorStateList colorStateList = ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.healthauditgreen);
            holder.bgrelative.setBackgroundTintList(colorStateList);
         //   holder.bgrelative.setBackgroundTintList(ContextCompat.getColor(holder.itemView.getContext(), R.color.healthauditgreen));
        } else {
            ColorStateList colorStateList = ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.white);
            holder.bgrelative.setBackgroundTintList(colorStateList);
           // holder.bgrelative.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
        }


        holder.itemView.setOnClickListener(v -> {
            fruit.setSelected(!fruit.isSelected());
            notifyItemChanged(position);
            listener.onItemClick(fruit);
        });
    }

    @Override
    public int getItemCount() {
        return fruitList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Fruit fruit);
    }

    static class FruitViewHolder extends RecyclerView.ViewHolder {
        TextView fruitName;
        CheckBox favoriteCheckbox;
        RelativeLayout bgrelative;

        public FruitViewHolder(@NonNull View itemView) {
            super(itemView);
            fruitName = itemView.findViewById(R.id.fruit_name);
            favoriteCheckbox = itemView.findViewById(R.id.favorite_checkbox);
            bgrelative = itemView.findViewById(R.id.bgrelative);

        }
    }
}
