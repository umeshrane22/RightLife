package com.example.rlapp.ui.drawermenu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rlapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.example.rlapp.ui.utility.ConversionUtils.convertCentimeterToFtInch;
import static com.example.rlapp.ui.utility.ConversionUtils.convertFeetToCentimeter;
import static com.example.rlapp.ui.utility.ConversionUtils.convertKgToLbs;
import static com.example.rlapp.ui.utility.ConversionUtils.convertLbsToKgs;

public class ProfileActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 100;
    private static final int PICK_IMAGE_REQUEST = 101;
    private int requestCode = 100;
    private ImageView ivProfileImage, ivSave, calendarButton, nextArrow, backArrow;
    private EditText edtFirstName, edtLastName, edtFt, edtInch, edtHeightCms, edtWeight, edtPhoneNumber, edtEmail;
    private TextView tvDate, tvGenderSpinner, tvHeightSpinner, tvWeightSpinner, tvUploadPhoto, tvTakePhoto;
    private LinearLayout llHeightFtInch;

    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (requestCode == CAMERA_REQUEST) {
                        Intent data = result.getData();
                        assert data != null;
                        Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                        ivProfileImage.setImageBitmap(photo);
                    } else {
                        assert result.getData() != null;
                        Uri selectedImage = result.getData().getData();
                        ivProfileImage.setImageURI(selectedImage);
                    }
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getViews();

        ivSave.setOnClickListener(view -> saveData());

        tvHeightSpinner.setOnClickListener(view -> openHeightPopUp());
        tvWeightSpinner.setOnClickListener(view -> openWeightPopup());
        tvGenderSpinner.setOnClickListener(view -> openGenderPopup());
        calendarButton.setOnClickListener(view -> showDatePicker());

        ivProfileImage.setOnClickListener(view -> {
            nextArrow.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.VISIBLE);
            tvTakePhoto.setVisibility(View.VISIBLE);
            tvUploadPhoto.setVisibility(View.VISIBLE);
        });

        tvTakePhoto.setOnClickListener(view -> {
            requestCode = CAMERA_REQUEST;
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraActivityResultLauncher.launch(cameraIntent);
        });

        tvUploadPhoto.setOnClickListener(view -> {
            requestCode = PICK_IMAGE_REQUEST;
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            cameraActivityResultLauncher.launch(intent);
        });

    }

    private void getViews() {
        ivProfileImage = findViewById(R.id.iv_profile_image);
        ivSave = findViewById(R.id.ic_save);
        tvDate = findViewById(R.id.dateText);
        calendarButton = findViewById(R.id.calendarButton);
        tvGenderSpinner = findViewById(R.id.edt_spinner_gender);
        llHeightFtInch = findViewById(R.id.ll_height_ft_inch);
        edtFt = findViewById(R.id.edt_ft);
        edtInch = findViewById(R.id.edt_inch);
        edtHeightCms = findViewById(R.id.edt_height);
        edtWeight = findViewById(R.id.edt_weight);
        tvHeightSpinner = findViewById(R.id.edt_spinnerheight);
        tvWeightSpinner = findViewById(R.id.edt_spinner_weight);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        edtEmail = findViewById(R.id.edt_email);
        nextArrow = findViewById(R.id.iv_next_arrow);
        backArrow = findViewById(R.id.iv_back_arrow);
        tvUploadPhoto = findViewById(R.id.tv_upload_photo);
        tvTakePhoto = findViewById(R.id.tv_take_photo);
        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
    }

    private void openHeightPopUp() {
        PopupMenu popupMenu = new PopupMenu(this, tvHeightSpinner);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_ft_inch_cms, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            String previousSelection = tvHeightSpinner.getText().toString();
            if (previousSelection.equals(menuItem.toString())) {
                return true;
            }
            if (!tvHeightSpinner.getText().toString().equals(menuItem.toString())) {
                if (getString(R.string.str_cms).equals(menuItem.toString())) {
                    edtHeightCms.setVisibility(View.VISIBLE);
                    llHeightFtInch.setVisibility(View.GONE);

                    String ftInch = edtFt.getText().toString() + "." + edtInch.getText().toString();
                    edtHeightCms.setText(convertFeetToCentimeter(ftInch));

                } else {
                    edtHeightCms.setVisibility(View.GONE);
                    llHeightFtInch.setVisibility(View.VISIBLE);

                    String cms = convertCentimeterToFtInch(edtHeightCms.getText().toString());
                    String[] strings = cms.split("\\.");
                    edtFt.setText(strings[0]);
                    if (strings.length > 1) {
                        edtInch.setText(strings[1]);
                    }

                }
            }
            tvHeightSpinner.setText(menuItem.toString());
            return true;
        });

        popupMenu.show();
    }

    private void openGenderPopup() {
        PopupMenu popupMenu = new PopupMenu(this, tvGenderSpinner);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_gender, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            tvGenderSpinner.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }

    private void openWeightPopup() {
        PopupMenu popupMenu = new PopupMenu(this, tvWeightSpinner);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_weight, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (!tvWeightSpinner.getText().toString().equals(menuItem.toString())) {
                String weight;
                if (getString(R.string.str_kgs).equals(menuItem.toString())) {
                    weight = convertKgToLbs(edtWeight.getText().toString());
                } else {
                    weight = convertLbsToKgs(edtWeight.getText().toString());
                }
                edtWeight.setText(weight);
            }
            tvWeightSpinner.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    tvDate.setText(selectedDate);
                    ArrayList<String> data = new ArrayList<>();
                    data.add(selectedDate);
                }, year - 15, month, day);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(year - 15, month, day);
        datePickerDialog.getDatePicker().setMaxDate(calendar1.getTimeInMillis());
        datePickerDialog.show();
    }

    private int getAge(String dobString) {

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);


        return today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
    }

    private void saveData() {
        String firstName = edtFirstName.getText().toString();
        String lastName = edtLastName.getText().toString();
        String height = edtHeightCms.getText().toString();
        String weight = edtWeight.getText().toString();
        String gender = tvGenderSpinner.getText().toString();
        String dob = tvDate.getText().toString();
        String phoneNumber = edtPhoneNumber.getText().toString();
        String email = edtEmail.getText().toString();


    }
}
