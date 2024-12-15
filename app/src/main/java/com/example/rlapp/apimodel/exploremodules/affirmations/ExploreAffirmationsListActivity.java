package com.example.rlapp.apimodel.exploremodules.affirmations;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.affirmations.AffirmationResponse;
import com.example.rlapp.ui.GridRecyclerViewAdapter;
import com.example.rlapp.ui.HomeActivity;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExploreAffirmationsListActivity extends AppCompatActivity {
    ImageView ic_back_dialog, close_dialog;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_affirmations_list);
           //find view
        recyclerView = findViewById(R.id.recycler_view);

        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        close_dialog = findViewById(R.id.ic_close_dialog);

        //Api Calls
        getAffirmationsList("");
        ic_back_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
               // showExitDialog();
            }
        });
    }


    private void getAffirmationsList(String s) {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Make the API call
        Call<ResponseBody> call = apiService.getAffrimationsListQuicklinks(accessToken, 20, 0, true);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String jsonResponse = null;
                    try {
                        jsonResponse = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.d("API_RESPONSE", jsonResponse);
                    Log.d("API Response", "Affirmation list: " + response.body());
                    Gson gson = new Gson();

                    Log.d("API Response", "Affirmation list: " + jsonResponse);
                    AffrimationListQuicklinks ResponseObj = gson.fromJson(jsonResponse, AffrimationListQuicklinks.class);
                    //Log.d("API Response Affrimation Quicklinks", "affirm Quicklinks Success: " + ResponseObj.getData().getAffirmationList().get(0).getTitle());
                    // setupAfirmationContent(ResponseObj);
                    setupListData(ResponseObj.getData().getAffirmationList());
                } else {
                    //Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ExploreAffirmationsListActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details

            }
        });

    }

    private void setupListData(List<Affirmation> affirmationList) {
        AffrimationRecyclerViewAdapter adapter = new AffrimationRecyclerViewAdapter(this, affirmationList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}