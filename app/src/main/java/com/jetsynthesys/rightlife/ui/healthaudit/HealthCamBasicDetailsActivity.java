package com.jetsynthesys.rightlife.ui.healthaudit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.jetsynthesys.rightlife.BaseActivity;
import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.ui.payment.AccessPaymentActivity;

public class HealthCamBasicDetailsActivity extends BaseActivity {

    private EditText edtFirstName, edtLastName, edtHeight, edtSpinnerHeight, edtWeight, edtSpinnerWeight,
            edtAge, edtGender, edtSmoke, edtBPMedication, edtDiabetic;
    private Button btnStartScan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setChildContentView(R.layout.activity_health_cam_basic_details);
        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtHeight = findViewById(R.id.edt_height);
        edtSpinnerHeight = findViewById(R.id.edt_spinnerheight);
        edtWeight = findViewById(R.id.edt_weight);
        edtSpinnerWeight = findViewById(R.id.edt_spinner_weight);
        edtAge = findViewById(R.id.edt_age);
        edtGender = findViewById(R.id.edt_spinner_gender);
        edtSmoke = findViewById(R.id.edt_spinner_smoke);
        edtBPMedication = findViewById(R.id.edt_spinner_bp_medication);
        edtDiabetic = findViewById(R.id.edt_spinner_diabetic);
        btnStartScan = findViewById(R.id.btn_start_scan);

        edtSpinnerHeight.setOnClickListener(view -> openHeightPopUp());
        edtSpinnerWeight.setOnClickListener(view -> openWeightPopup());
        edtGender.setOnClickListener(view -> openGenderPopup());
        edtSmoke.setOnClickListener(view -> openSmokePopup());
        edtBPMedication.setOnClickListener(view -> openBPMedicationPopUp());
        edtDiabetic.setOnClickListener(view -> openDiabeticPopup());

        btnStartScan.setOnClickListener(view -> {
            String firstName = edtFirstName.getText().toString();
            String lastName = edtLastName.getText().toString();
            String height = edtHeight.getText().toString();
            String weight = edtWeight.getText().toString();
            String age = edtAge.getText().toString();
            String gender = edtGender.getText().toString();
            String smoke = edtSmoke.getText().toString();
            String bpMedication = edtBPMedication.getText().toString();
            String diabetic = edtDiabetic.getText().toString();

            if (firstName.isEmpty()) {
                Toast.makeText(this, "First Name is required", Toast.LENGTH_SHORT).show();
            } else if (lastName.isEmpty()) {
                Toast.makeText(this, "Last Name is required", Toast.LENGTH_SHORT).show();
            } else if (height.isEmpty()) {
                Toast.makeText(this, "Height is required", Toast.LENGTH_SHORT).show();
            } else if (weight.isEmpty()) {
                Toast.makeText(this, "Weight is required", Toast.LENGTH_SHORT).show();
            } else if (age.isEmpty()) {
                Toast.makeText(this, "Age is required", Toast.LENGTH_SHORT).show();
            } else if (smoke.isEmpty() || bpMedication.isEmpty() || diabetic.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show();
            } else {
                int heightInt = Integer.parseInt(height);
                int weightInt = Integer.parseInt(weight);
                int ageInt = Integer.parseInt(age);
                if (heightInt == 0) {
                    Toast.makeText(this, "Height can not be 0", Toast.LENGTH_SHORT).show();
                } else if (weightInt == 0) {
                    Toast.makeText(this, "Weight can not be 0", Toast.LENGTH_SHORT).show();
                } else if (ageInt == 0) {
                    Toast.makeText(this, "Age can not be 0", Toast.LENGTH_SHORT).show();
                } else {
                    /****
                     * do some start scan stuff here
                     */

                    Intent intent = new Intent(HealthCamBasicDetailsActivity.this, AccessPaymentActivity.class);
                    intent.putExtra("ACCESS_VALUE","FACIAL_SCAN");
                    startActivity(intent);
                }
            }
        });

    }

    private void openHeightPopUp() {
        PopupMenu popupMenu = new PopupMenu(this, edtSpinnerHeight);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_ft_inch_cms, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtSpinnerHeight.setText(menuItem.toString());
            return true;
        });

        popupMenu.show();
    }

    private void openWeightPopup() {
        PopupMenu popupMenu = new PopupMenu(this, edtSpinnerWeight);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_weight, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtSpinnerWeight.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }

    private void openGenderPopup() {
        PopupMenu popupMenu = new PopupMenu(this, edtGender);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_gender, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtGender.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }

    private void openSmokePopup() {
        PopupMenu popupMenu = new PopupMenu(this, edtSmoke);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_yesno, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtSmoke.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }

    private void openBPMedicationPopUp() {
        PopupMenu popupMenu = new PopupMenu(this, edtBPMedication);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_yesno, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtBPMedication.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }

    private void openDiabeticPopup() {
        PopupMenu popupMenu = new PopupMenu(this, edtDiabetic);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_diabetic, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtDiabetic.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }
}
