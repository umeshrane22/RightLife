package com.jetsynthesys.rightlife.ui;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;
import com.jetsynthesys.rightlife.RetrofitData.ApiService;
import com.jetsynthesys.rightlife.apimodel.affirmations.SortedService;
import com.jetsynthesys.rightlife.apimodel.affirmations.updateAffirmation.AffirmationRequest;
import com.jetsynthesys.rightlife.ui.thoughtoftheday.ThoughtOfTheDayDetailActivity;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceConstants;
import com.google.gson.Gson;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//public class TestAdapter extends BaseBannerAdapter {

public class TestAdapter extends BaseBannerAdapter<SortedService> {


    public TestAdapter(List<SortedService> cardItems) {
    }



    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param data     Current item data.
     * @param position Current item position.
     * @param pageSize Page size of BVP,equals {@link BaseBannerAdapter#getListSize()}.
     */


    /**
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param data     Current item data.
     * @param position Current item position.
     * @param pageSize Page size of BVP,equals {@link BaseBannerAdapter#getListSize()}.
     */
    @Override
    protected void bindData(BaseViewHolder<SortedService> holder, SortedService data, int position, int pageSize) {
        if (data != null) {
            //holder.setImageResource(R.id.cardImage, data.getImageResId());

            ImageView img_affirmation = holder.findViewById(R.id.img_affirmation);
            TextView txt_title_affirm = holder.findViewById(R.id.txt_title_affirm);
            TextView txt_desc_affirm = holder.findViewById(R.id.txt_desc_affirm);
            Button button = holder.findViewById(R.id.btn_affirm);
            txt_title_affirm.setText(data.getTitle());
            txt_desc_affirm.setText(data.getSubtitle());

            Glide.with(holder.itemView.getContext())
                    .load(ApiClient.CDN_URL_QA + data.getThumbnail().getUrl())
                    .placeholder(R.drawable.rl_placeholder)
                    .error(R.drawable.rl_placeholder)
                    .transform(new RoundedCorners(25))
                    .into(img_affirmation);

            if (!data.getConsumedCta().equalsIgnoreCase(""))
            {
                button.setText(data.getConsumedCta());
                button.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_bg_button_affirm));
                button.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_bg_button_affirmed));
                button.setEnabled(false);
            }else {
                button.setText(data.getCtaName());
                button.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_bg_button_affirmed));
                button.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_bg_button_affirm));
                button.setEnabled(true);
            }
            //button.setText(data.getCtaName());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   // callAffirmationApi(holder.itemView.getContext(),data.getConsumedCta(), data.getId(), SharedPreferenceManager.getInstance(holder.itemView.getContext()).getUserId());
                    Intent intent = new Intent(holder.itemView.getContext(), ThoughtOfTheDayDetailActivity.class);
                    intent.putExtra("affirmationId", data.getId());
                    intent.putExtra("contentId", data.getContentId());
                    holder.itemView.getContext().startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getLayoutId(int viewType) {
        // Return the layout resource ID for this adapter
        return R.layout.test;
    }

}
