package com.example.rlapp.ui;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.apimodel.Affirmations;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

import java.util.AbstractList;
import java.util.List;

//public class TestAdapter extends BaseBannerAdapter {

public class TestAdapter extends BaseBannerAdapter<CardItem> {


    public TestAdapter(List<CardItem> cardItems) {
    }



    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param data     Current item data.
     * @param position Current item position.
     * @param pageSize Page size of BVP,equals {@link BaseBannerAdapter#getListSize()}.
     */
    @Override
    protected void bindData(BaseViewHolder<CardItem> holder, CardItem data, int position, int pageSize) {
        if (data != null) {
            holder.setImageResource(R.id.cardImage, data.getImageResId());
            Button button = holder.findViewById(R.id.btn_affirm);
            if (data.isAffirmation()) {
                button.setText("Affirmed");
                button.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_bg_button_affirm));
                button.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_bg_button_affirmed));

            }




        }
    }

    @Override
    public int getLayoutId(int viewType) {
        // Return the layout resource ID for this adapter
        return R.layout.test;
    }

}
