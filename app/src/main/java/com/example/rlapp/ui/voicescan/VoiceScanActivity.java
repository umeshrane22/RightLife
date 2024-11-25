package com.example.rlapp.ui.voicescan;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.rlapp.R;
import com.zhpan.indicator.IndicatorView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import me.relex.circleindicator.CircleIndicator3;

public class VoiceScanActivity extends AppCompatActivity {

    ImageView ic_back_dialog, close_dialog;
    VoiceScanPagerAdapter adapter;
    Button btn_howitworks;

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
        setContentView(R.layout.activity_voicescan);

        ViewPager2 viewPager = findViewById(R.id.view_pager);
        ic_back_dialog = findViewById(R.id.ic_back_dialog);
        close_dialog = findViewById(R.id.ic_close_dialog);
        btn_howitworks = findViewById(R.id.btn_howitworks);

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


        close_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //finish();
                showExitDialog();
            }
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
                    // Toast.makeText(VoiceScanActivity.this, "Scan is Coming Soon", Toast.LENGTH_SHORT).show();
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
            btn_howitworks.setText("Next Page");
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
        dialogText.setText("Please find a quiet and comfortable place before starting");

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
        Button dialogButton = dialog.findViewById(R.id.dialog_button);

        EditText edtDD = dialog.findViewById(R.id.date_dd);
        EditText edtMM = dialog.findViewById(R.id.date_mm);
        EditText edtYYYY = dialog.findViewById(R.id.date_yy);

        // Optional: Set dynamic content
        dialogText.setText("Please find a quiet and comfortable place before starting");

        edtDD.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int count = edtDD.getText().length();
                if (count == 2){
                    edtMM.requestFocus();
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
                if (count == 2){
                    edtYYYY.requestFocus();
                }
                if (count == 0){
                    edtDD.requestFocus();
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
                if (count == 2){
                    dialogButton.requestFocus();
                }
                if (count == 0){
                    edtMM.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Set button click listener
        dialogButton.setOnClickListener(v -> {

            String date = edtDD.getText().toString() + "-" + edtMM.getText().toString() + "-" + edtYYYY.getText().toString();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!checkDateFormat(date)) {
                    Toast.makeText(VoiceScanActivity.this, "Please enter valid date", Toast.LENGTH_SHORT).show();
                } else {
                    // Perform your action
                    dialog.dismiss();
                    Toast.makeText(VoiceScanActivity.this, "Scan feature is Coming Soon", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(VoiceScanActivity.this, VoiceScanFromActivity.class);
                    // Optionally pass data
                    //intent.putExtra("key", "value");
                    startActivity(intent);
                }
            }
        });
        dialogIcon.setOnClickListener(v -> {
            dialog.dismiss();
        });

        // Show the dialog
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean checkDateFormat(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(date, formatter);
            if (!formatter.format(localDate).equals(date)) {
                return false;
            }
            return true;
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
}
