package com.example.rlapp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.chipsmodulefilter.ModuleChipCategory;
import com.example.rlapp.apimodel.modulecontentlist.Content;
import com.example.rlapp.apimodel.modulecontentlist.ModuleContentDetailsList;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListActivity extends AppCompatActivity {

    ImageView ic_back_dialog, close_dialog;
    private RecyclerView recyclerView;
    private ChipGroup chipGroup;
    String[] itemNames;
    int[] itemImages;
    ModuleChipCategory ResponseObj;
    String moduleId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);

        Intent intent = getIntent();
        String categoryType = intent.getStringExtra("Categorytype");
         moduleId = intent.getStringExtra("moduleId");

// Now you can use the categoryType variable to perform actions or set up the UI
        if (categoryType != null) {
            // Do something with the category type
            Log.d("CategoryListActivity", "Received category type: " + categoryType);
            Log.d("CategoryListActivity", "Received Module type: " + moduleId);
            // For example, set a TextView's text or load data based on the category type
        } else {
            // Handle the case where the extra is not present
            Log.d("CategoryListActivity", "Category type not found in intent");
        }

        recyclerView = findViewById(R.id.recycler_view);
        chipGroup = findViewById(R.id.filter_chip_group);



        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        close_dialog = findViewById(R.id.ic_close_dialog);

        itemNames = new String[]{"Sleep Right with sounds", "Move right", "Sleep music", "Video category", "Audio 1", "Audio 2", "Audio 2", "Audio 2", "Audio 2", "Audio 2"};
        itemImages = new int[]{R.drawable.contents, R.drawable.eat_home, R.drawable.facial_scan,
                R.drawable.first_look, R.drawable.generic_02, R.drawable.meal_plan, R.drawable.generic_02,
                R.drawable.meal_plan, R.drawable.generic_02, R.drawable.meal_plan};


        //API Call
        getContentlistdetails(categoryType,moduleId);

        getContentlist(moduleId);   // api to get module




        List<Content> contentList = Collections.emptyList();
        GridRecyclerViewAdapter adapter = new GridRecyclerViewAdapter(this, itemNames, itemImages, contentList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        recyclerView.setAdapter(adapter);

       // showCustomDialog();




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
                showExitDialog();
            }
        });
        ArrayList<String> tags = new ArrayList<>();
        tags.add("technology");
        tags.add("programming");
        tags.add("Java");
        tags.add("web development");
        tags.add("data science");
        tags.add("machine learning");
        tags.add("artificial intelligence");
        for (String tag : tags) {
            //addChip(tag); // Add each tag as a chip
        }
        setupChipListeners();
    }

    // Method to dynamically add chips
    private void addChip(String category) {
        Chip chip = new Chip(this);
        chip.setId(View.generateViewId()); // Generate unique ID
        chip.setText(category);
        chip.setCheckable(true);
        chip.setChecked(false);
        chip.setChipCornerRadius(50);
        chip.setChipStrokeColor(ContextCompat.getColorStateList(this,R.color.chip_selected_background_color));
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setTextColor(ContextCompat.getColorStateList(this, R.color.black));


        // Set up the chip's appearance
        chip.setChipBackgroundColorResource(R.color.white);
        chip.setTextColor(ContextCompat.getColorStateList(this, R.color.chip_text_color));

        // Set different colors for selected state
        ColorStateList colorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[]{
                        ContextCompat.getColor(this, R.color.chip_selected_background_color),
                        ContextCompat.getColor(this, R.color.white)
                }
        );
        chip.setChipBackgroundColor(colorStateList);

        // Text color for selected state
        ColorStateList textColorStateList = new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{-android.R.attr.state_checked}
                },
                new int[]{
                        ContextCompat.getColor(this, R.color.chip_selected_text_color),
                        ContextCompat.getColor(this, R.color.chip_text_color)
                }
        );
        chip.setTextColor(textColorStateList);
        chipGroup.addView(chip);
    }
    private void setupChipListeners() {
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip != null) {
                String category = chip.getText().toString();
                Toast.makeText(this, "this is "+category, Toast.LENGTH_SHORT).show();
                //filterList(category.equals("All") ? "all" : category.toLowerCase());


                int position = -1;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = group.getChildAt(i);
                    if (child.getId() == checkedId) {
                        position = i;
                        break;
                    }
                }

                // Use the position as needed
                if (position != -1) {
                    // Do something with the position
                    Log.d("ChipGroup", "Checked chip position: " + position);
                    Toast.makeText(this, "this is "+
                            ResponseObj.getData().getResult().get(position).getCategoryId(), Toast.LENGTH_SHORT).show();
                    //API Call
                    getContentlistdetails(ResponseObj.getData().getResult().get(position).getCategoryId(),moduleId);
                }

            }
        });
    }





    private void showExitDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_exit_dialog_mind);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);

        // Find views from the dialog layout
        //ImageView dialogIcon = dialog.findViewById(R.id.img_close_dialog);
        ImageView dialogImage = dialog.findViewById(R.id.dialog_image);
        TextView dialogText = dialog.findViewById(R.id.dialog_text);
        Button dialogButtonStay = dialog.findViewById(R.id.dialog_button_stay);
        Button dialogButtonExit = dialog.findViewById(R.id.dialog_button_exit);

        // Optional: Set dynamic content
       // dialogText.setText("Please find a quiet and comfortable place before starting");

        // Set button click listener
        dialogButtonStay.setOnClickListener(v -> {
            // Perform your action
            dialog.dismiss();
            //Toast.makeText(VoiceScanActivity.this, "Scan feature is Coming Soon", Toast.LENGTH_SHORT).show();


        });
        dialogButtonExit.setOnClickListener(v -> {
            dialog.dismiss();
            this.finish();
        });

        // Show the dialog
        dialog.show();
    }


    private void getContentlistdetails(String categoryId, String moduleId) {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

// Create an instance of the ApiService


        // Make the GET request
        Call<ResponseBody> call = apiService.getContentdetailslist(
                accessToken,
                categoryId,
                10,
                0,
                moduleId
        );

        // Handle the response
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        if (response.body() != null) {
                            String successMessage = response.body().string();
                            System.out.println("Request successful: " + successMessage);
                            //Log.d("API Response", "User Details: " + response.body().toString());
                            Gson gson = new Gson();
                            String jsonResponse = gson.toJson(response.body().toString());
                            Log.d("API Response", "User Details: " + successMessage);
                            ModuleContentDetailsList ResponseObj = gson.fromJson(successMessage, ModuleContentDetailsList.class);
                            Log.d("API Response", "User Details: " + ResponseObj.getData().getContentList().size()
                                    +" "+ResponseObj.getData().getContentList().get(0).getTitle());
                            setupListData(ResponseObj.getData().getContentList());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorMessage = response.errorBody().string();
                            System.out.println("Request failed with error: " + errorMessage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("Request failed: " + t.getMessage());
            }
        });

    }





    private void setupListData(List<Content> contentList) {
        GridRecyclerViewAdapter adapter = new GridRecyclerViewAdapter(this, itemNames, itemImages,contentList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // 2 columns
        recyclerView.setAdapter(adapter);
    }





    // get module content chip list
    private void getContentlist(String moduleId) {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        // SignupOtpRequest request = new SignupOtpRequest("+91"+mobileNumber);

        // Make the API call
        //Call<JsonElement> call = apiService.getContent(accessToken,"HEALTH_REPORT");
        // Make the GET request
        Call<JsonElement> call = apiService.getContent(
                accessToken,
                "CATEGORY",
                moduleId,
                false,
                true
        );
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonElement promotionResponse5 = response.body();
                    Log.d("API Response", "Content  list: " + promotionResponse5.toString());
                    //Log.d("API Response", "Content  list - : " + affirmationsResponse.toString());
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());

                     ResponseObj = gson.fromJson(jsonResponse,ModuleChipCategory.class);
                    Log.d("API Response body", "Success:chip name " + ResponseObj.getData().getResult().get(0).getName());
                    for (int i = 0; i < ResponseObj.getData().getResult().size(); i++) {
                        addChip(ResponseObj.getData().getResult().get(i).getName());
                    }

                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorMessage = response.errorBody().string();
                            System.out.println("Request failed with error: " + errorMessage);
                            Log.d("API Response 2", "Success: " + errorMessage);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(CategoryListActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(CategoryListActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details

            }
        });

    }

}
