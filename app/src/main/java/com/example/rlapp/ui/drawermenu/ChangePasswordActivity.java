package com.example.rlapp.ui.drawermenu;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rlapp.R;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextView tvError;
    private EditText edtCurrentPassword, edtNewPassword, edtConfirmPassword;
    private Button btnRestPassword;
    private ImageButton ibNewPass,ibCurrentPass, ibConfirmPass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getViews();
    }

    private void getViews() {
        tvError = findViewById(R.id.tv_error);
        edtCurrentPassword = findViewById(R.id.edt_current_password);
        edtNewPassword = findViewById(R.id.edt_new_password);
        edtConfirmPassword = findViewById(R.id.edt_confirm_new_password);


        btnRestPassword = findViewById(R.id.btn_reset_password);

        ibConfirmPass = findViewById(R.id.ib_confirm_pass);
        ibCurrentPass = findViewById(R.id.ib_current_pass);
        ibNewPass = findViewById(R.id.ib_new_pass);

        ibCurrentPass.setOnClickListener(view -> showPasswordClick(edtCurrentPassword));

        ibConfirmPass.setOnClickListener(view -> showPasswordClick(edtConfirmPassword));

        ibNewPass.setOnClickListener(view -> showPasswordClick(edtNewPassword));

        edtNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String confirmPass = edtConfirmPassword.getText().toString();
                if (confirmPass.equals(charSequence.toString())) {
                    tvError.setVisibility(View.GONE);
                } else {
                    tvError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newPass = edtNewPassword.getText().toString();
                if (newPass.equals(charSequence.toString())) {
                    tvError.setVisibility(View.GONE);
                } else {
                    tvError.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnRestPassword.setOnClickListener(view -> {
            String currentPassword = edtCurrentPassword.getText().toString();
            String newPassword = edtNewPassword.getText().toString();
            String confirmPassword = edtConfirmPassword.getText().toString();
            if (currentPassword.isEmpty()) {
                Toast.makeText(this, "Current Password is required", Toast.LENGTH_SHORT).show();
            } else if (newPassword.isEmpty()) {
                Toast.makeText(this, "New Password is required", Toast.LENGTH_SHORT).show();
            } else {
                ChangePassword changePassword = new ChangePassword();
                changePassword.setOldpassword(currentPassword);
                changePassword.setNewpassword(newPassword);
                updatePasswordAPI(changePassword);
            }
        });
    }

    private void showPasswordClick(EditText passwordEditText){
        if (passwordEditText.getInputType() == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }

        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    private void updatePasswordAPI(ChangePassword changePassword) {

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<ResponseBody> call = apiService.changePassword(accessToken, changePassword);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());


                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
