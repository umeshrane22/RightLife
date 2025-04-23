package com.jetsynthesys.rightlife.ui.new_design;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.jetsynthesys.rightlife.MainActivity;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.databinding.ActivityHappyHaveYouBinding;
import com.jetsynthesys.rightlife.ui.HomeActivity;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceConstants;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager;

@SuppressLint("CustomSplashScreen")
public class HappyToHaveYouActivity extends AppCompatActivity {
    SharedPreferenceManager sharedPreferenceManager;
    private ActivityHappyHaveYouBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy_have_you);
        binding = ActivityHappyHaveYouBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferenceManager = SharedPreferenceManager.getInstance(this);

        String username = sharedPreferenceManager.getUserName();
        binding.dialogWelcome.tvUsername.setText(username);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = getSharedPreferences(SharedPreferenceConstants.ACCESS_TOKEN, Context.MODE_PRIVATE);
                String accessToken = sharedPreferences.getString(SharedPreferenceConstants.ACCESS_TOKEN, null);
                boolean isLoggedIn = sharedPreferences.getBoolean(SharedPreferenceConstants.IS_LOGGED_IN, false);

                if (accessToken != null && isLoggedIn) {
                    // User is logged in, proceed to main activity
                    Intent intent = new Intent(HappyToHaveYouActivity.this, WellnessFocusActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 1500);
    }
}