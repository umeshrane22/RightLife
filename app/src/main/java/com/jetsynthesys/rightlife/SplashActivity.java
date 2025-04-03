package com.jetsynthesys.rightlife;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.jetsynthesys.rightlife.ui.HomeActivity;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceConstants;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        final ProgressBar loader = findViewById(R.id.loader);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.VISIBLE);
            }
        }, 1000);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loader.setVisibility(View.GONE);
                SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
                String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
                boolean isLoggedIn = sharedPreferences.getBoolean(SharedPreferenceConstants.IS_LOGGED_IN, false);

                if (accessToken != null && isLoggedIn) {
                    // User is logged in, proceed to main activity
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // User is not logged in, show login screen
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 1000);
    }
}