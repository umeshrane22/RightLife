package com.example.rlapp.ui;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.affirmations.AffirmationResponse;
import com.example.rlapp.apimodel.affirmations.SortedService;
import com.example.rlapp.apimodel.affirmations.updateAffirmation.AffirmationRequest;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
                    .placeholder(R.drawable.img_logintop1) // Replace with your placeholder image
                    .into(img_affirmation);

            if (!data.getConsumedCta().equalsIgnoreCase(""))
            {
                button.setText(data.getConsumedCta());
                button.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_bg_button_affirm));
                button.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_bg_button_affirmed));

            }else {
                button.setText(data.getCtaName());
                button.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_bg_button_affirmed));
                button.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.color_bg_button_affirm));
            }
            //button.setText(data.getCtaName());
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(holder.itemView.getContext(), "Affirmation Clicked", Toast.LENGTH_SHORT).show();
                    callAffirmationApi(holder.itemView.getContext(),data.getConsumedCta(), data.getId(), SharedPreferenceManager.getInstance(holder.itemView.getContext()).getUserId());

                }
            });
        }

    }



    private void callAffirmationApi(Context context, String consumedCta, String affirmationId, String userId) {
        //-----------
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        // SignupOtpRequest request = new SignupOtpRequest("+91"+mobileNumber);
        AffirmationRequest request = new AffirmationRequest(consumedCta, affirmationId, userId);

        // Make the API call
        Call<ResponseBody> call = apiService.postAffirmation(accessToken,request);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseBody affirmationsResponse = response.body();
                    Log.d("API Response", "Affirmation list: " + affirmationsResponse.toString());
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());

                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            Log.d("Response consumed", " " +
                                    responseBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    //setupAfirmationContent(ResponseObj);

                } else {
                    //Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details

            }
        });

    }

    @Override
    public int getLayoutId(int viewType) {
        // Return the layout resource ID for this adapter
        return R.layout.test;
    }

}
