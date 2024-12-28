package com.example.rlapp.ui.rlpagemain;

import android.content.Context;
import android.content.res.ColorStateList;
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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.apimodel.rlpagemodels.continuemodela.ContentDetail;
import com.example.rlapp.ui.utility.AppConstants;
import com.example.rlapp.ui.utility.Utils;


import java.util.List;

public class RLContinueListAdapter extends RecyclerView.Adapter<RLContinueListAdapter.ViewHolder> {

    private String[] itemNames;
    private int[] itemImages;
    private LayoutInflater inflater;
    private Context ctx;
    List<ContentDetail> contentList;
    public RLContinueListAdapter(Context context, List<ContentDetail> contentList) {
        this.ctx = context;

        this.contentList = contentList;
        this.inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public RLContinueListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_episodelist_item, parent, false);
        return new RLContinueListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RLContinueListAdapter.ViewHolder holder, int position) {
        holder.textView.setText(contentList.get(position).getTitle());

        holder.tv_modulename.setText(Utils.getModuleText(contentList.get(position).getModuleId()));
        Drawable startDrawable =getModuleTypeDrawabale(ctx,contentList.get(position).getModuleId());
                //Drawable startDrawable = ContextCompat.getDrawable(ctx, R.drawable.rlpage_thinkright_svg);
        startDrawable.setBounds(0, 0, startDrawable.getIntrinsicWidth(), startDrawable.getIntrinsicHeight());
        holder.tv_modulename.setCompoundDrawables(startDrawable, null, null, null);

        int color = Utils.getModuleColor(ctx, contentList.get(position).getModuleId());
        //holder.imgtag.setBackgroundColor(color);
        holder.imgtag.setImageTintList(ColorStateList.valueOf(color));
        holder.tv_category.setText(contentList.get(position).getCategoryName());
        holder.tv_artistname.setText(String.format("%s %s",
                contentList.get(position).getArtist().get(0).getFirstName(),
                contentList.get(position).getArtist().get(0).getLastName()));

        if (contentList.get(position).getThumbnail().getUrl() != null && !contentList.get(position).getThumbnail().getUrl().isEmpty()) {
            Glide.with(ctx).load(ApiClient.CDN_URL_QA+contentList.get(position).getThumbnail().getUrl())
                    .transform(new RoundedCorners(25))
                    .into(holder.imageView);
            Log.d("Image URL List", "list Url: " + ApiClient.CDN_URL_QA+contentList.get(position).getThumbnail().getUrl());
            Log.d("Image URL List", "list title: " + contentList.get(position).getTitle());
        }
        //holder.imageView.setImageResource(itemImages[position]);
       /* if (contentList.get(position).getContentType().equalsIgnoreCase("TEXT")){
            holder.img_iconview.setImageResource(R.drawable.ic_read_category);
        }else {
            holder.img_iconview.setImageResource(R.drawable.ic_sound_category);
        }*/
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ctx, "image clicked - "+holder.getBindingAdapterPosition(), Toast.LENGTH_SHORT).show();
            }
        });
        String text = "";
        if ("SERIES".equals(contentList.get(position).getContentType())) {
            text = (contentList.get(position).getLeftDuration()) + " videos/audios";
        } else {
            text = contentList.get(position).getLeftDuration() + " left";
        }
        holder.tvTimeLeft.setText(text);
    }

    private Drawable getModuleTypeDrawabale(Context ctx, String moduleName) {
        Drawable startDrawable = ContextCompat.getDrawable(ctx, R.drawable.rlpage_thinkright_svg);
        if ("THINK_RIGHT".equalsIgnoreCase(moduleName)) {
            return ContextCompat.getDrawable(ctx, R.drawable.rlpage_thinkright_svg);
        } else if ("SLEEP_RIGHT".equalsIgnoreCase(moduleName)) {
            return ContextCompat.getDrawable(ctx, R.drawable.rlpage_sleepright_svg);
        } else if ("MOVE_RIGHT".equalsIgnoreCase(moduleName)) {
            return ContextCompat.getDrawable(ctx, R.drawable.rlpage_moveright_svg);
        } else if ("EAT_RIGHT".equalsIgnoreCase(moduleName)) {
            return ContextCompat.getDrawable(ctx, R.drawable.rlpage_eatright_svg);
        }
        return startDrawable;
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,imgtag,favorite_image,img_iconview;
        TextView textView,tv_modulename,tv_category,tv_artistname,tvTimeLeft;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rlimg1);
            textView = itemView.findViewById(R.id.item_text);
            tv_modulename = itemView.findViewById(R.id.tv_modulename);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_artistname = itemView.findViewById(R.id.tv_artistname);
            tvTimeLeft = itemView.findViewById(R.id.tvTimeLeft);
            imgtag = itemView.findViewById(R.id.imgtag);

        /*    img_iconview = itemView.findViewById(R.id.img_iconview);
            favorite_image = itemView.findViewById(R.id.favorite_image);
            favorite_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "position - "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });*/

            //img_iconview.setImageResource(R.drawable.ic_read_category);
        }
    }
}

