package com.example.rlapp.ui.drawermenu;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rlapp.R;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());

        WebView webView = findViewById(R.id.wv_privacy_policy);
        webView.loadUrl("file:///android_res/raw/privacy_policy.html");
    }
}
