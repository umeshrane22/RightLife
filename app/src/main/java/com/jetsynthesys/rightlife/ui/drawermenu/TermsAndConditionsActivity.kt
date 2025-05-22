package com.jetsynthesys.rightlife.ui.drawermenu;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jetsynthesys.rightlife.R;

public class TermsAndConditionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());

        WebView webView = findViewById(R.id.wv_terms_condition);
        webView.loadUrl("file:///android_res/raw/terms_condition.html");
    }
}
