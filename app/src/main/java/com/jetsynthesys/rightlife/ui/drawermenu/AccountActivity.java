package com.jetsynthesys.rightlife.ui.drawermenu;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jetsynthesys.rightlife.R;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());

        findViewById(R.id.ll_delete_account).setOnClickListener(view -> {
            startActivity(new Intent(AccountActivity.this, DeleteAccountActivity.class));
        });
    }
}
