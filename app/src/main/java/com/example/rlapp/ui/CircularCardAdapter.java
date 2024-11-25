package com.example.rlapp.ui;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.apimodel.submodule.SubModuleResponse;
import com.example.rlapp.ui.healthaudit.HealthAuditActivity;
import com.example.rlapp.ui.mindaudit.MindAuditActivity;
import com.example.rlapp.ui.voicescan.VoiceScanActivity;

import java.util.List;

public class CircularCardAdapter extends RecyclerView.Adapter<CircularCardAdapter.CardViewHolder> {

    private Context mContext;
    private List<CardItem> items; // Replace CardItem with your model class

    public CircularCardAdapter( Context context, List<CardItem> items) {
        this.items = items;
        this.mContext = context;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        // Circular scrolling logic
        CardItem item = items.get(position % items.size());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(view.getContext(), "Clicked on: " + item.getTitle()+ holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                // Start new activity here


                if (item.getCategory().equalsIgnoreCase("MIND_AUDIT")) {
                    Intent intent = new Intent(mContext, MindAuditActivity.class);
                    // Optionally pass data
                    //intent.putExtra("key", "value");
                    mContext.startActivity(intent);

                } else if (item.getCategory().equalsIgnoreCase("VOICE_SCAN")) {
                    Intent intent = new Intent(mContext, VoiceScanActivity.class);
                    // Optionally pass data
                    //intent.putExtra("key", "value");
                    mContext.startActivity(intent);

                } else
                {
                    Intent intent = new Intent(mContext, HealthAuditActivity.class);
                    // Optionally pass data
                    //intent.putExtra("key", "value");
                    mContext.startActivity(intent);
                }

            }
        });
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        // To enable infinite scrolling
        return Integer.MAX_VALUE;
    }



    static class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView cardTitle,cardbtntext,cardbtntextDesc,workshop_tag1,workshop_tag3;
        private ImageView cardImage;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardTitle  = itemView.findViewById(R.id.promotitle);
            cardbtntextDesc= itemView.findViewById(R.id.promodescription);
            cardbtntext = itemView.findViewById(R.id.promobtntxt);
            cardImage = itemView.findViewById(R.id.cardImage);
            workshop_tag1 = itemView.findViewById(R.id.workshop_tag1);
        }

        public void bind(CardItem item) {
           // cardTitle.setText(item.getTitle());
            cardImage.setImageResource(item.getImageResId());
            if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext()).load(ApiClient.CDN_URL_QA+item.getImageUrl()).into(cardImage);
                Log.e("Image URL List", "list : " + ApiClient.CDN_URL_QA+item.getImageUrl());
            }
            cardbtntext.setText(item.getButtonText());
            cardTitle.setText(item.getTitle());
            cardbtntextDesc.setText(item.getContent());
            workshop_tag1.setText(item.getViewCount());

            //Drawable drawable = itemView.getContext().getResources().getDrawable(R.drawable.ic_home_black_24dp);

            Log.d("banner", "cardcategory " + item.getCategory());
            if (item.getCategory().equalsIgnoreCase("MIND_AUDIT")) {
                Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_banner_t_mindaudit);
                cardTitle.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                Drawable drawable1 = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_banner_chekinnow);
                cardbtntext.setCompoundDrawablesWithIntrinsicBounds(drawable1, null, null, null);

            } else if (item.getCategory().equalsIgnoreCase("VOICE_SCAN")) {

                Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_banner_t_voicescan);
                cardTitle.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                Drawable drawable1 = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_banner_recordnow);
                cardbtntext.setCompoundDrawablesWithIntrinsicBounds(drawable1, null, null, null);

            } else
            {
                //else if (item.getCategory().equalsIgnoreCase("FACIAL_SCAN"))
                Drawable drawable = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_banner_t_healthcam);
                cardTitle.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                Drawable drawable1 = ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_banner_scannow);
                cardbtntext.setCompoundDrawablesWithIntrinsicBounds(drawable1, null, null, null);
            }

        }
    }

        public void updateData(List<CardItem> newData) {
        // 1. Clear the existing data
        items.clear();

        // 2. Add the new data
        items.addAll(newData);

        // 3. Notify the adapter
        notifyDataSetChanged();
    }

}
