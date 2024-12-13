package com.example.rlapp.ui.drawermenu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.UploadImage;
import com.example.rlapp.apimodel.userdata.UserProfileResponse;
import com.example.rlapp.apimodel.userdata.Userdata;
import com.example.rlapp.ui.utility.DateTimeUtils;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.example.rlapp.ui.utility.Utils;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.rlapp.ui.utility.ConversionUtils.convertCentimeterToFtInch;
import static com.example.rlapp.ui.utility.ConversionUtils.convertFeetToCentimeter;
import static com.example.rlapp.ui.utility.ConversionUtils.convertKgToLbs;
import static com.example.rlapp.ui.utility.ConversionUtils.convertLbsToKgs;

public class ProfileActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 100;
    private static final int PICK_IMAGE_REQUEST = 101;
    UserProfileResponse data;
    Userdata userdata;
    String token;
    ApiService apiService;
    private int requestCode = 100;
    private ImageView ivProfileImage, ivSave, calendarButton, nextArrow, backArrow;
    ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
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
    private EditText edtFirstName, edtLastName, edtFt, edtInch, edtHeightCms, edtWeight, edtPhoneNumber, edtEmail;
    private TextView tvDate, tvGenderSpinner, tvHeightSpinner, tvWeightSpinner, tvUploadPhoto, tvTakePhoto;
    private LinearLayout llHeightFtInch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getViews();

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
        apiService = ApiClient.getClient().create(ApiService.class);

        setData();

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

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());
    }

    private void setData() {
        data = SharedPreferenceManager.getInstance(this).getUserProfile();
        userdata = data.getUserdata();

        edtFirstName.setText(userdata.getFirstName());
        edtLastName.setText(userdata.getLastName());
        edtEmail.setText(userdata.getEmail());
        edtPhoneNumber.setText(userdata.getPhoneNumber());
        tvDate.setText(DateTimeUtils.convertAPIDate(userdata.getDateofbirth()));
        Glide.with(this).load(ApiClient.CDN_URL_QA + userdata.getProfilePicture()).into(ivProfileImage);

        if (userdata.getGender().equals("M") || userdata.getGender().equalsIgnoreCase("Male"))
            tvGenderSpinner.setText("Male");
        else tvGenderSpinner.setText("Female");

        if (userdata.getWeightUnit().equals("KG")) {
            tvWeightSpinner.setText("Kgs");
        }
        edtWeight.setText(userdata.getWeight().toString());
        if (userdata.getHeightUnit().equals("CM")) {
            tvHeightSpinner.setText("cms");
            edtHeightCms.setVisibility(View.VISIBLE);
            llHeightFtInch.setVisibility(View.GONE);

            edtHeightCms.setText(userdata.getHeight().toString());
        } else {
            String cms = userdata.getHeight().toString();
            String[] strings = cms.split("\\.");
            edtFt.setText(strings[0]);
            if (strings.length > 1) {
                edtInch.setText(strings[1]);
            }
        }

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

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
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

    private void getPreSignedUrl(UploadImage uploadImage) {
        Call<ResponseBody> call = apiService.getPreSignedUrl(token, uploadImage);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                } else {
                    Toast.makeText(ProfileActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
        int age = getAge(dob);

        if (firstName.isEmpty()) {
            Toast.makeText(this, "First Name is required", Toast.LENGTH_SHORT).show();
        } else if (lastName.isEmpty()) {
            Toast.makeText(this, "Last Name is required", Toast.LENGTH_SHORT).show();
        } else if (height.isEmpty()) {
            Toast.makeText(this, "Height is required", Toast.LENGTH_SHORT).show();
        } else if (weight.isEmpty()) {
            Toast.makeText(this, "Weight is required", Toast.LENGTH_SHORT).show();
        } else if (gender.isEmpty()) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
        } else if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Phone Number is required", Toast.LENGTH_SHORT).show();
        } else if (phoneNumber.length() != 10) {
            Toast.makeText(this, "Phone Number contains 10 digits", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
        } else if (!email.matches(Utils.emailPattern)) {
            Toast.makeText(this, "Invalid Email format", Toast.LENGTH_SHORT).show();
        } else if (age < 13) {
            Toast.makeText(this, "User must be at least 13 years old", Toast.LENGTH_SHORT).show();
        } else {

            userdata.setFirstName(firstName);
            userdata.setLastName(lastName);
            userdata.setEmail(email);
            userdata.setNewEmail(email);
            userdata.setPhoneNumber(phoneNumber);
            userdata.setDateofbirth(DateTimeUtils.convert_ddMMyyyy_toAPIDate(dob));
            if (gender.equalsIgnoreCase("Male")) {
                userdata.setGender("M");
            } else {
                userdata.setGender("F");
            }
            userdata.setWeight(Integer.parseInt(edtWeight.getText().toString()));
            if ("Kgs".equalsIgnoreCase(tvWeightSpinner.getText().toString())){
                userdata.setWeightUnit("KG");
            }else {
                userdata.setWeightUnit("LBS");
            }

            userdata.setHeight(Integer.parseInt(edtHeightCms.getText().toString()));
            userdata.setHeightUnit("CM");
            userdata.setPhoneNumber(phoneNumber);


            updateUserData(userdata);
        }
    }

    private void updateUserData(Userdata userdata) {
        Call<ResponseBody> call = apiService.updateUser(token, userdata);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(ProfileActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
