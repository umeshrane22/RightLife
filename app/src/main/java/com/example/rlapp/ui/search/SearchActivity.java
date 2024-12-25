package com.example.rlapp.ui.search;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.ui.CategoryListActivity;
import com.example.rlapp.ui.HomeActivity;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    private SearchResponse searchResponse;
    private SearchCategoryAdapter searchCategoryAdapter;
    private RecyclerView recyclerView;
    private ImageView imageSearch;
    private EditText edtSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());

        recyclerView = findViewById(R.id.rv_popular_categories);
        imageSearch = findViewById(R.id.image_search);
        edtSearch = findViewById(R.id.edt_search);

        imageSearch.setOnClickListener(view -> Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        getSearchContent();
    }


    private void getSearchContent() {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Make the API call
        Call<ResponseBody> call = apiService.getSearchContent(accessToken);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    try {
                        searchResponse = gson.fromJson(response.body().string(), SearchResponse.class);
                        searchCategoryAdapter = new SearchCategoryAdapter(SearchActivity.this, searchResponse.getData(), new SearchCategoryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(SearchResult searchResult) {
                                Intent intent = new Intent(SearchActivity.this, CategoryListActivity.class);
                                intent.putExtra("Categorytype", searchResult.getCategoryId());
                                intent.putExtra("moduleId", searchResult.getModuleId());
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(searchCategoryAdapter);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details

            }
        });

    }
}
