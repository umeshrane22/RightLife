package com.example.rlapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.rlapp.RetrofitData.ApiClient;
import com.example.rlapp.RetrofitData.ApiService;
import com.example.rlapp.apimodel.LoginResponse;
import com.example.rlapp.apimodel.LoginResponseMobile;
import com.example.rlapp.apimodel.SignupOtpRequest;
import com.example.rlapp.apimodel.SubmitLoginOtpRequest;
import com.example.rlapp.apimodel.SubmitOtpRequest;
import com.example.rlapp.apimodel.emaillogin.EmailLoginRequest;
import com.example.rlapp.apimodel.emaillogin.EmailOtpRequest;
import com.example.rlapp.apimodel.emaillogin.SubmitEmailOtpRequest;
import com.example.rlapp.apimodel.userdata.UserProfileResponse;
import com.example.rlapp.ui.HomeActivity;
import com.example.rlapp.ui.utility.EncryptionUtil;
import com.example.rlapp.ui.utility.SharedPreferenceConstants;
import com.example.rlapp.ui.utility.SharedPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.JsonElement;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Login is in this
    // test dev branch 2


    ImageButton googleButton;
    EditText phoneInput,phoneInputOption,emailInput,emailInputOption;
    ImageView img_getotp,imgGetOtpBtn,imgSignupOtpBtn,imgGetEmailOtpBtn;
    LinearLayout ll_loginoption_mobile,ll_loginoption_email ,ll_loginoption,ll_loginoption_otp;
    PinView pinView;
    public static String mobileNumber,emailId;
    public static String loginType = "mobile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        googleButton  = findViewById(R.id.googleButton);
        phoneInput = findViewById(R.id.phoneInput);
        phoneInputOption = findViewById(R.id.phoneInputOption);
        emailInput  = findViewById(R.id.emailInput);
        emailInputOption = findViewById(R.id.emailInputOption);
        imgGetOtpBtn = findViewById(R.id.imgGetOtpBtn);
        imgGetEmailOtpBtn = findViewById(R.id.imgGetEmailOtpBtn);
        imgSignupOtpBtn = findViewById(R.id.imgSignupOtpBtn);
        pinView = findViewById(R.id.pinview);

        ll_loginoption_mobile = findViewById(R.id.ll_loginoption_mobile);
        ll_loginoption_email = findViewById(R.id.ll_loginoption_email);
        ll_loginoption = findViewById(R.id.ll_loginoption);
        ll_loginoption_otp = findViewById(R.id.ll_loginoption_otp);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(MainActivity.this, "Google Button Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();*/
                //String email = "nakoda0701@gmail.com";
                String email = "hemant19@yopmail.com";
                String password = "";


                try {
                     password = EncryptionUtil.getEncryptedPassword("123456");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                submitEmailPasswordLogin(email,password);

            }
        });
        imgGetOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_loginoption.setVisibility(View.GONE);
                ll_loginoption_mobile.setVisibility(View.GONE);
                ll_loginoption_otp.setVisibility(View.VISIBLE);
                 mobileNumber = phoneInputOption.getText().toString();
                Log.d("API OTP ", "Success: --" + mobileNumber);
                Toast.makeText(MainActivity.this, mobileNumber, Toast.LENGTH_SHORT).show();
                //callForOtpSignup(mobileNumber);
                loginType = "mobile";
                callForOtpLogin(mobileNumber);
            }
        });
        imgGetEmailOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                emailId = emailInputOption.getText().toString();
                Log.d("API OTP ", "Success: --" + emailId);
              //  Toast.makeText(MainActivity.this, emailId, Toast.LENGTH_SHORT).show();
                if (emailId.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter Email Id", Toast.LENGTH_SHORT).show();
                }else {
                    loginType = "email";
                    callForOtpEmail(emailId);
                    ll_loginoption.setVisibility(View.GONE);
                    ll_loginoption_mobile.setVisibility(View.GONE);
                    ll_loginoption_email.setVisibility(View.GONE);

                    ll_loginoption_otp.setVisibility(View.VISIBLE);
                }
            }
        });

        phoneInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_loginoption.setVisibility(View.GONE);
                ll_loginoption_mobile.setVisibility(View.VISIBLE);
                ll_loginoption_email.setVisibility(View.GONE);
            }
        });

        emailInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_loginoption_email.setVisibility(View.VISIBLE);
                ll_loginoption.setVisibility(View.GONE);
                ll_loginoption_mobile.setVisibility(View.GONE);
            }
        });

        imgSignupOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=pinView.getText().toString();
                Log.d("API OTP ", "Success: ---" + otp);
                Toast.makeText(MainActivity.this, otp, Toast.LENGTH_SHORT).show();

               // CallSubmitOtpSignup(otp);
                if (loginType.equalsIgnoreCase("mobile")) {
                    submitOtpLogin(otp);
                } else if (loginType.equalsIgnoreCase("email")) {
                    submitOtpEmailLogin(otp);
                }

            }
        });
       /* imgGetOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp=pinView.getText().toString();
                Log.d("API OTP ", "Success: --" + otp);
                Toast.makeText(MainActivity.this, otp, Toast.LENGTH_SHORT).show();

                //callForOtpSignup();
            }
        });*/
/*
        //callForOtpSignup();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        LoginRequest request = new LoginRequest("", "phoneNumber", "9021335118");

        // Make the API call
        Call<LoginResponse> call = apiService.loginUser(request);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Log.d("API Response", "Success: " + loginResponse.getStatusCode());
                    Log.d("API Response 2", "Success: " + loginResponse.getMessage());
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("API Response body", "Success: " + jsonResponse);
                    if (loginResponse.isSuccess()) {
                        Toast.makeText(MainActivity.this, "Success: " + loginResponse.getStatusCode(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed: " + loginResponse.getStatusCode(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/

      //  getPromotionList("");


    }
    //get otp for signup
 void callForOtpSignup(String mobileNumber){

     ApiService apiService = ApiClient.getClient().create(ApiService.class);

     // Create a request body (replace with actual email and phone number)
     SignupOtpRequest request = new SignupOtpRequest("+91"+mobileNumber);

     // Make the API call
     Call<LoginResponse> call = apiService.generateOtpSignup(request);
     call.enqueue(new Callback<LoginResponse>() {
         @Override
         public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
             if (response.isSuccessful() && response.body() != null) {
                 LoginResponse loginResponse = response.body();
                 Log.d("API Response", "Success: " + loginResponse.getMessage());
                 Gson gson = new Gson();
                 String jsonResponse = gson.toJson(response.body());
                 Log.d("API Response body", "Success: " + jsonResponse);
                 if (loginResponse.isSuccess()) {
           //          Toast.makeText(MainActivity.this, "Success: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                 } else {
                     Toast.makeText(MainActivity.this, "Failed: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                 }

             } else {
                 Toast.makeText(MainActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
             }
         }

         @Override
         public void onFailure(Call<LoginResponse> call, Throwable t) {
             Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
         }
     });

 }

 //get otp for login
 void callForOtpLogin(String mobileNumber){

     ApiService apiService = ApiClient.getClient().create(ApiService.class);

     // Create a request body (replace with actual email and phone number)
     SignupOtpRequest request = new SignupOtpRequest("+91"+mobileNumber);

     // Make the API call
     Call<LoginResponse> call = apiService.generateOtpLogin(request);
     call.enqueue(new Callback<LoginResponse>() {
         @Override
         public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
             if (response.isSuccessful() && response.body() != null) {
                 LoginResponse loginResponse = response.body();
                 Log.d("API Response", "Success: " + loginResponse.getMessage());
                 Gson gson = new Gson();
                 String jsonResponse = gson.toJson(response.body());
                 Log.d("API Response body", "Success: " + jsonResponse);
                 if (loginResponse.isSuccess()) {
                     Toast.makeText(MainActivity.this, "Success: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                 } else {
                     Toast.makeText(MainActivity.this, "Failed: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                 }

             } else {
                 Toast.makeText(MainActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
             }
         }

         @Override
         public void onFailure(Call<LoginResponse> call, Throwable t) {
             Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
         }
     });
 }


    //call submit otp for signup
    void CallSubmitOtpSignup(String OTP){

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        SubmitOtpRequest request = new SubmitOtpRequest("9226164804",OTP,"ajdsawejkd","sjdhawesjd","android","cazgv4h_TEeHDiFdjKtwVU:APA91bHppHumnxKnekhG7RerIZDDZa3dEogX80UHcv0hVRZlj2IvvATvWcRlnbxo_yqnqgI2CxQrW-4sYAS9wD6xaR5ukd8mj8NQzovPKqxfqNX8pzRlGLnG0jkV5xz6i4Rkw-z3mCxq");

        // Make the API call
        Call<LoginResponse> call = apiService.submitOtpSignup(request);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Log.d("API Response", "Success: " + loginResponse.getMessage());
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("API Response body", "Success: " + jsonResponse);
                    if (loginResponse.isSuccess()) {
                        Toast.makeText(MainActivity.this, "Success: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Call Submit Otp for Email
    void submitOtpEmailLogin(String OTP){

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        SubmitEmailOtpRequest request = new SubmitEmailOtpRequest(emailId,OTP,"ABC123","Asus ROG 6","hp","ABC123","");

        // Make the API call
        Call<LoginResponseMobile> call = apiService.submitOtpEmail(request);
        call.enqueue(new Callback<LoginResponseMobile>() {
            @Override
            public void onResponse(Call<LoginResponseMobile> call, Response<LoginResponseMobile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponseMobile loginResponse = response.body();
                    Log.d("API Response", "Success: " + loginResponse.getStatusCode());
                    Log.d("API Response 2", "Success: " + loginResponse.getAccessToken());

                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("API Response body", "Success: " + jsonResponse);
                    if (loginResponse.isSuccess()) {
                        Toast.makeText(MainActivity.this, "Success: " + loginResponse.getStatusCode(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                      //  saveAccessToken(loginResponse.getAccessToken());
                       // getUserDetails("");

                    } else {
                        Toast.makeText(MainActivity.this, "Failed: " + loginResponse.getStatusCode(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseMobile> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    //call submit otp for login
    void submitOtpLogin(String OTP){

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        SubmitLoginOtpRequest request = new SubmitLoginOtpRequest("+91"+mobileNumber,OTP,"ABC123","Asus ROG 6","hp","ABC123");

        // Make the API call
        Call<LoginResponseMobile> call = apiService.submitOtpLogin(request);
        call.enqueue(new Callback<LoginResponseMobile>() {
            @Override
            public void onResponse(Call<LoginResponseMobile> call, Response<LoginResponseMobile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponseMobile loginResponse = response.body();
                    Log.d("API Response", "Success: " + loginResponse.getStatusCode());
                    Log.d("API Response 2", "Success: " + loginResponse.getAccessToken());

                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("API Response body", "Success: " + jsonResponse);
                    if (loginResponse.isSuccess()) {
                        Toast.makeText(MainActivity.this, "Success: " + loginResponse.getStatusCode(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        saveAccessToken(loginResponse.getAccessToken());
                        getUserDetails("");

                    } else {
                        Toast.makeText(MainActivity.this, "Failed: " + loginResponse.getStatusCode(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseMobile> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveAccessToken(String accessToken) {
        SharedPreferenceManager.getInstance(this).saveAccessToken(accessToken);
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferenceConstants.ACCESS_TOKEN, accessToken);
        editor.putBoolean(SharedPreferenceConstants.IS_LOGGED_IN, true);
        editor.apply();
    }

    //-----------
// get user details
    private void getUserDetails(String s) {
        //-----------
        SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        // SignupOtpRequest request = new SignupOtpRequest("+91"+mobileNumber);

        // Make the API call
        Call<JsonElement> call = apiService.getUserDetais(accessToken);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonElement promotionResponse2 = response.body();
                    Log.d("API Response", "User Details: " + promotionResponse2.toString());
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());

                    UserProfileResponse ResponseObj = gson.fromJson(jsonResponse, UserProfileResponse.class);
                    Log.d("API Response body", "Success: User Details" + ResponseObj.getUserdata().getId());
                    //handleServicePaneResponse(ResponseObj);
                    //saveUserId(ResponseObj.getUserdata().getId());
                    SharedPreferenceManager.getInstance(getApplicationContext()).saveUserId(ResponseObj.getUserdata().getId());

                    Log.d("UserID", "USerID: User Details" + SharedPreferenceManager.getInstance(getApplicationContext()).getUserId());

                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();
                } else {
                    //  Toast.makeText(HomeActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API ERROR", "onFailure: " + t.getMessage());
                t.printStackTrace();  // Print the full stack trace for more details

            }
        });

    }


    //call submit otp for login
    void submitEmailPasswordLogin(String email, String password){

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)

        EmailLoginRequest emailLoginRequest = new EmailLoginRequest( email,
                password,
                "TP1A.220905.001",
                "IV2201",
                "cA5akcnzTfGpbQr7wEVuf3:APA91bEJYJNwJ-PJcEJ8Cpe95PWCJxMVGV7oT_CNtuo7xOjVecfTMjflleBu-m38h46gOcoegID68FQVTuMVLQLF4xA7o_ehNu49fNi2eNGg4Co9jzXimdo",
                "android"
        );


        // Make the API call
        Call<LoginResponseMobile> call = apiService.EmailPasswordLogin(emailLoginRequest);
        call.enqueue(new Callback<LoginResponseMobile>() {
            @Override
            public void onResponse(Call<LoginResponseMobile> call, Response<LoginResponseMobile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponseMobile loginResponse = response.body();
                    Log.d("API Response", "Success: " + loginResponse.getStatusCode());
                    Log.d("API Response 2", "Success: " + loginResponse.getAccessToken());

                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("API Response body", "Success: " + jsonResponse);
                    if (loginResponse.isSuccess()) {
                        Toast.makeText(MainActivity.this, "Success: " + loginResponse.getStatusCode(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        saveAccessToken(loginResponse.getAccessToken());
                        getUserDetails("");

                    } else {
                        Toast.makeText(MainActivity.this, "Failed: " + loginResponse.getStatusCode(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponseMobile> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }



    // get otp for email -

    void callForOtpEmail(String email){

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Create a request body (replace with actual email and phone number)
        EmailOtpRequest request = new EmailOtpRequest(email);

        // Make the API call
        Call<LoginResponse> call = apiService.generateOtpEmail(request);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    Log.d("API Response", "Success:Email Otp " + loginResponse.getMessage());
                    Gson gson = new Gson();
                    String jsonResponse = gson.toJson(response.body());
                    Log.d("API Response body", "Success: " + jsonResponse);
                    if (loginResponse.isSuccess()) {
                        Toast.makeText(MainActivity.this, "Success: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed: " + loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Server Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
