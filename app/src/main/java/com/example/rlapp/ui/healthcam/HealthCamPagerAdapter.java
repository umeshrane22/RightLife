package com.example.rlapp.ui.healthcam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HealthCamPagerAdapter extends RecyclerView.Adapter<HealthCamPagerAdapter.ViewHolder> {

    private int[] layouts;

    // Constructor to accept an array of layouts (resources IDs)
    public HealthCamPagerAdapter(int[] layouts) {
        this.layouts = layouts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for the current page
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layouts[viewType], parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Nothing to bind since the layout is static, but you can configure your view here if needed
    }

    @Override
    public int getItemCount() {
        return layouts.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // ViewHolder class
    static class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
