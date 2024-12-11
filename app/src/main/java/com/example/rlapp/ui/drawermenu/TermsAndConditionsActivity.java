package com.example.rlapp.ui.drawermenu;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rlapp.R;

public class TermsAndConditionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());
    }
}
