package com.example.rlapp.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rlapp.R;
import com.example.rlapp.ui.HomeActivity;
import com.example.rlapp.ui.rlpagemain.RLPageActivity;

public class AccessPaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llHealthAudit, llHealthCam, llVoiceScan, llmindaudit;
    private ImageView rlmenu,img_homemenu,img_healthmenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_accesspayment);
       /* llHealthAudit = findViewById(R.id.ll_health_audit);
        llHealthCam = findViewById(R.id.ll_health_cam);
        llVoiceScan = findViewById(R.id.ll_voice_scan);
        llmindaudit = findViewById(R.id.ll_mind_audit);


        llHealthAudit.setOnClickListener(this);
        llHealthCam.setOnClickListener(this);
        llVoiceScan.setOnClickListener(this);
        llmindaudit.setOnClickListener(this);*/






        /*llHealthAudit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HealthPageMainActivity.this, "Health Audit card click", Toast.LENGTH_SHORT).show();
            }
        });*/

    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (view.getId() == R.id.ll_health_audit) {

            Toast.makeText(AccessPaymentActivity.this, "Health Audit card click", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.ll_health_cam) {
            Toast.makeText(AccessPaymentActivity.this, "Health Cam card click", Toast.LENGTH_SHORT).show();

        } else if (view.getId() == R.id.ll_voice_scan) {
            Toast.makeText(AccessPaymentActivity.this, "Voice Scan card click", Toast.LENGTH_SHORT).show();

        } else if (view.getId() == R.id.ll_mind_audit) {
            Toast.makeText(AccessPaymentActivity.this, "Mind Audit card click", Toast.LENGTH_SHORT).show();
        }

    }
}