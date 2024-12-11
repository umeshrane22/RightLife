package com.example.rlapp.ui.drawermenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;

import java.util.ArrayList;

public class FavouritesButtonAdapter extends RecyclerView.Adapter<FavouritesButtonAdapter.ButtonViewHolder> {

    private Context context;
    private ArrayList<String> favTypes;
    private OnItemClickListener onItemClickListener;
    private int selectedItemPos = 0;
    private int lastItemSelectedPos = 0;

    public FavouritesButtonAdapter(Context context, ArrayList<String> favTypes, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.favTypes = favTypes;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ButtonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_favourites_button, parent, false);
        return new ButtonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ButtonViewHolder holder, int position) {
        holder.tvFav.setText(favTypes.get(position));
        if (position == selectedItemPos) {
            holder.llFav.setBackground(context.getDrawable(R.drawable.roundedcornerpinkborder_selected));
            holder.ivArrowUp.setVisibility(View.VISIBLE);
        } else {
            holder.llFav.setBackground(context.getDrawable(R.drawable.roundedcornerpinkborder));
            holder.ivArrowUp.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(view -> {
            selectedItemPos = holder.getAdapterPosition();
            if (lastItemSelectedPos == -1)
                lastItemSelectedPos = selectedItemPos;
            else {
                notifyItemChanged(lastItemSelectedPos);
                lastItemSelectedPos = selectedItemPos;
            }
            notifyItemChanged(selectedItemPos);
            onItemClickListener.onItemClick(favTypes.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return favTypes.size();
    }

    public interface OnItemClickListener {
        void onItemClick(String selectedValue);
    }

    static class ButtonViewHolder extends RecyclerView.ViewHolder {
        TextView tvFav;
        LinearLayout llFav;
        ImageView ivArrowUp;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFav = itemView.findViewById(R.id.tv_fav);
            llFav = itemView.findViewById(R.id.ll_fav_row);
            ivArrowUp = itemView.findViewById(R.id.arrow_up);
        }
    }
}
