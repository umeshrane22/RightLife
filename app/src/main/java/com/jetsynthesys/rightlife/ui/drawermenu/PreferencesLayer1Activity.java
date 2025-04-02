package com.jetsynthesys.rightlife.ui.drawermenu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.ui.utility.SharedPreferenceManager;

public class PreferencesLayer1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_layer1);

        String name = SharedPreferenceManager.getInstance(this).getUserProfile().getUserdata().getFirstName();

        TextView tvUserName = findViewById(R.id.tv_username);
        tvUserName.setText("Hi " + name);
        findViewById(R.id.btn_continue).setOnClickListener(view -> {
            Intent intent = new Intent(this, PreferencesLayer2Activity.class);
            startActivity(intent);
            finish();
        });
    }
}
