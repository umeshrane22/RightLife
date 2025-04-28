package com.jetsynthesys.rightlife.ui.voicescan;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.jetsynthesys.rightlife.BaseActivity;
import com.jetsynthesys.rightlife.R;
import com.zhpan.indicator.IndicatorView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import me.relex.circleindicator.CircleIndicator3;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VoiceScanActivity extends BaseActivity {

    private ImageView ic_back_dialog, close_dialog;
    private VoiceScanPagerAdapter adapter;
    private Button btn_howitworks;

    /**
     * {@inheritDoc}
     * <p>
     * Perform initialization of all fragments.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_voicescan);

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        close_dialog = findViewById(R.id.ic_close_dialog);
        btn_howitworks = findViewById(R.id.btn_howitworks);


        getVoiceScanResults();

        CircleIndicator3 indicator = findViewById(R.id.indicator);
        IndicatorView indicator_view = findViewById(R.id.indicator_view);
        indicator_view.setSliderHeight(21);
        indicator_view.setSliderWidth(80);
        // Array of layout resources to use in the ViewPager
        int[] layouts = {
                R.layout.page_one, // Define these layout files in your res/layout directory
                R.layout.page_two,
                R.layout.page_three
        };

        // Set up the adapter
        adapter = new VoiceScanPagerAdapter(layouts);

        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        indicator_view.setupWithViewPager(viewPager);

        showCustomDialog();


        ic_back_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = viewPager.getCurrentItem();
                int totalItems = adapter.getItemCount();

                // Go to the next page if it's not the last one
               /* if (currentItem < totalItems - 1) {
                    viewPager.setCurrentItem(currentItem + 1);
                } else {
                    // If it's the last page, go back to the first page
                    viewPager.setCurrentItem(0);
                }*/

                if (currentItem == 0) {
                    finish();
                }
                // If on any other page, move to the previous page
                else {
                    viewPager.setCurrentItem(currentItem - 1);
                }
            }
        });


        close_dialog.setOnClickListener(view -> {
            showExitDialog();
        });

        btn_howitworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentItem = viewPager.getCurrentItem();
                int totalItems = adapter.getItemCount();
                // Go to the next page if it's not the last one
                if (currentItem < totalItems - 1) {
                    viewPager.setCurrentItem(currentItem + 1);
                } else {
                    // If it's the last page, got to scan
                    showBirthDayDialog();
                }
            }
        });

        // Add page change callback to update button text
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonText(position);
            }
        });
    }

    // Method to update button text based on the current page
    private void updateButtonText(int position) {
        int totalItems = adapter.getItemCount();

        if (position == totalItems - 1) {
            btn_howitworks.setText("Proceed to Scan");
        } else {
            btn_howitworks.setText("Next");
        }
    }

    private void showCustomDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);

        // Find views from the dialog layout
        ImageView dialogIcon = dialog.findViewById(R.id.dialog_icon);
        ImageView dialogImage = dialog.findViewById(R.id.dialog_image);
        TextView dialogText = dialog.findViewById(R.id.dialog_text);
        Button dialogButton = dialog.findViewById(R.id.dialog_button);

        // Optional: Set dynamic content
        dialogText.setText("Please find a quiet and\n comfortable place before starting");

        // Set button click listener
        dialogButton.setOnClickListener(v -> {
            // Perform your action
            dialog.dismiss();
        });

        // Show the dialog
        dialog.show();
    }


    private void showBirthDayDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_bday_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);

        // Find views from the dialog layout
        ImageView dialogIcon = dialog.findViewById(R.id.img_close_dialog);
        ImageView dialogImage = dialog.findViewById(R.id.dialog_image);
        TextView dialogText = dialog.findViewById(R.id.dialog_text);
        MaterialButton dialogButton = dialog.findViewById(R.id.dialog_button);

        EditText edtDD = dialog.findViewById(R.id.date_dd);
        EditText edtMM = dialog.findViewById(R.id.date_mm);
        EditText edtYYYY = dialog.findViewById(R.id.date_yy);

        // Optional: Set dynamic content
        dialogText.setText("Tell us when you were born");

        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.dark_yellow);
        ColorStateList colorStateListWhite = ContextCompat.getColorStateList(this, R.color.white);

        if (edtDD.getText().length() == 2 && edtMM.getText().length() == 2 && edtYYYY.getText().length() == 4) {
            dialogButton.setBackgroundTintList(colorStateList);
            dialogButton.setTextColor(getColor(R.color.white));
            dialogButton.setStrokeWidth(0);
        }

        edtDD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int count = edtDD.getText().length();
                if (count == 2) {
                    edtMM.requestFocus();
                }

                if (count == 2 && edtMM.getText().length() == 2 && edtYYYY.getText().length() == 4) {
                    dialogButton.setBackgroundTintList(colorStateList);
                    dialogButton.setTextColor(getColor(R.color.white));
                    dialogButton.setStrokeWidth(0);
                } else {
                    dialogButton.setBackgroundTintList(colorStateListWhite);
                    dialogButton.setTextColor(getColor(R.color.txt_color_vs));
                    dialogButton.setStrokeWidth(1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtMM.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int count = edtMM.getText().length();
                if (count == 2) {
                    edtYYYY.requestFocus();
                }
                if (count == 0) {
                    edtDD.requestFocus();
                }

                if (count == 2 && edtDD.getText().length() == 2 && edtYYYY.getText().length() == 4) {
                    dialogButton.setBackgroundTintList(colorStateList);
                    dialogButton.setTextColor(getColor(R.color.white));
                    dialogButton.setStrokeWidth(0);
                } else {
                    dialogButton.setBackgroundTintList(colorStateListWhite);
                    dialogButton.setTextColor(getColor(R.color.txt_color_vs));
                    dialogButton.setStrokeWidth(1);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtYYYY.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int count = edtYYYY.getText().length();
                if (count == 2) {
                    dialogButton.requestFocus();
                }
                if (count == 0) {
                    edtMM.requestFocus();
                }
                if (count == 4 && edtDD.getText().length() == 2 && edtMM.getText().length() == 2) {
                    dialogButton.setBackgroundTintList(colorStateList);
                    dialogButton.setTextColor(getColor(R.color.white));
                    dialogButton.setStrokeWidth(0);
                } else {
                    dialogButton.setBackgroundTintList(colorStateListWhite);
                    dialogButton.setTextColor(getColor(R.color.txt_color_vs));
                    dialogButton.setStrokeWidth(1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        dialogButton.setOnClickListener(v -> {
            String date = edtDD.getText().toString() + "/" + edtMM.getText().toString() + "/" + edtYYYY.getText().toString();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!checkDateFormat(date)) {
                    Toast.makeText(VoiceScanActivity.this, "Please enter valid date", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.dismiss();
                    Intent intent = new Intent(VoiceScanActivity.this, VoiceScanFromActivity.class);
                    startActivity(intent);
                }
            }
        });
        dialogIcon.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }


    private void showErrorDialog1() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.vc_error_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)

        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkDateFormat(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate localDate = LocalDate.parse(date, formatter);
            return formatter.format(localDate).equals(date);
        } catch (Exception e) {
            return false;
        }
    }

    private void showExitDialog() {
        // Create the dialog
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.layout_exit_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);

        Button dialogButtonStay = dialog.findViewById(R.id.dialog_button_stay);
        Button dialogButtonExit = dialog.findViewById(R.id.dialog_button_exit);

        dialogButtonStay.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialogButtonExit.setOnClickListener(v -> {
            dialog.dismiss();
            this.finish();
        });

        dialog.show();
    }

    private void getVoiceScanResults() {
        Call<ResponseBody> call = apiService.getVoiceScanResults(sharedPreferenceManager.getAccessToken(), sharedPreferenceManager.getUserId(), true, 0, 0);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("AAAA", "Get Voice Scan results response = " + jsonResponse);
                } else {
                    Toast.makeText(VoiceScanActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(VoiceScanActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
