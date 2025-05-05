package com.jetsynthesys.rightlife.ui.therledit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jetsynthesys.rightlife.BaseActivity;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.RetrofitData.ApiClient;
import com.jetsynthesys.rightlife.RetrofitData.ApiService;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewAllByArtistActivity extends BaseActivity {

    private MoreContentListAdapter adapter;
    private RecyclerView rvViewAll;
    private int mLimit = 10;
    private int mSkip = 0;
    private Button btnLoadMore;
    private List<MoreContentList> contentList = new ArrayList<>();
    private String artistId = "";
    private TextView tvHeader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_view_all);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());
        rvViewAll = findViewById(R.id.rv_view_all);
        btnLoadMore = findViewById(R.id.btn_load_more);
        tvHeader = findViewById(R.id.tv_header_htw);


        artistId = getIntent().getStringExtra("ArtistId");
        String artistName = getIntent().getStringExtra("ArtistName");
        tvHeader.setText(artistName + "'s Content");

        getMoreLikeContent(artistId, mSkip, mLimit);

        btnLoadMore.setOnClickListener(view -> getMoreLikeContent(artistId, mSkip, mLimit));
        rvViewAll.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new MoreContentListAdapter(this, contentList);
        rvViewAll.setAdapter(adapter);

    }

    private void getMoreLikeContent(String contentId, int skip, int limit) {

        Call<ResponseBody> call = apiService.getMoreLikeContentByArtistId(sharedPreferenceManager.getAccessToken(), contentId, skip, limit);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String jsonString = response.body().string();
                        Gson gson = new Gson();

                        MoreContentByArtistIdResponse ResponseObj = gson.fromJson(jsonString, MoreContentByArtistIdResponse.class);
                        Log.d("API Response", "User Details: " + ResponseObj.getData().getList().size()
                                + " " + ResponseObj.getData().getList().get(0).getTitle());
                        contentList.addAll(ResponseObj.getData().getList());
                        adapter.notifyDataSetChanged();
                        if (ResponseObj.getData().getCount() > adapter.getItemCount()) {
                            btnLoadMore.setVisibility(View.VISIBLE);
                            mSkip = mSkip + limit;
                        } else {
                            btnLoadMore.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        Log.e("JSON_PARSE_ERROR", "Error parsing response: " + e.getMessage());
                    }
                } else {
                    Log.e("API_ERROR", "Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                handleNoInternetView(t);
            }
        });

    }
}
