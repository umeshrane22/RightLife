package com.jetsynthesys.rightlife.ui.drawermenu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jetsynthesys.rightlife.R;

public class PurchaseHistoryTypesActivity extends AppCompatActivity {

    private RelativeLayout rlLiveClasses, rlWorkshops, rlSubscriptions, rlConsultations, rlECommerce, rlOthers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history_types);

        rlLiveClasses = findViewById(R.id.rl_live_classes);
        rlWorkshops = findViewById(R.id.rl_workshops);
        rlSubscriptions = findViewById(R.id.rl_subscriptions);
        rlConsultations = findViewById(R.id.rl_consultations);
        rlECommerce = findViewById(R.id.rl_e_commerce);
        rlOthers = findViewById(R.id.rl_others);

        rlLiveClasses.setOnClickListener(view -> startNextActivity("LIVE_CLASS"));
        rlWorkshops.setOnClickListener(view -> startNextActivity("WORKSHOP"));
        rlSubscriptions.setOnClickListener(view -> startNextActivity("LIVE_CLASS"));
        rlConsultations.setOnClickListener(view -> startNextActivity("CONSULTATION"));
        rlECommerce.setOnClickListener(view -> startNextActivity("PAID_CONTENT"));
        rlOthers.setOnClickListener(view -> startNextActivity("OTHER_PURCHASES"));

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());
    }

    private void startNextActivity(String type) {
        Intent intent = new Intent(this, PurchaseHistoryActivity.class);
        intent.putExtra("TYPE", type);
        startActivity(intent);
    }
}
