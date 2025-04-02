package com.jetsynthesys.rightlife.ui.mindaudit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.ui.healthaudit.Fruit;

import java.util.List;

public class MindAuditReasonslistAdapter extends RecyclerView.Adapter<MindAuditReasonslistAdapter.FruitViewHolder> {
    private List<Fruit> fruitList;
    private OnItemClickListener listener;

    public MindAuditReasonslistAdapter(List<Fruit> fruitList, OnItemClickListener listener) {
        this.fruitList = fruitList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mind_audit_item, parent, false);
        return new FruitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder holder, int position) {
        Fruit fruit = fruitList.get(position);
        holder.fruitName.setText(fruit.getName());
        holder.favoriteCheckbox.setChecked(fruit.isSelected());

        if (fruit.isSelected()) {
            holder.bgrelative.setBackgroundDrawable(holder.itemView.getContext().getDrawable(R.drawable.roundedcornerpinkborder_selected));
        } else {
            holder.bgrelative.setBackgroundDrawable(holder.itemView.getContext().getDrawable(R.drawable.roundedcornerpinkborder));
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
