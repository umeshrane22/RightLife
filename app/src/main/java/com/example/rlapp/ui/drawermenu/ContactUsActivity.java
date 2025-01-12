package com.example.rlapp.ui.drawermenu;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rlapp.R;
import com.example.rlapp.ui.utility.Utils;

public class ContactUsActivity extends AppCompatActivity {
    private EditText edtName, edtEmail, edtPhoneNumber, edtMotive, edtMessage;
    private Button btnSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        edtName = findViewById(R.id.edt_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        edtMotive = findViewById(R.id.edt_motive_contact);
        edtMessage = findViewById(R.id.edt_your_message);
        btnSend = findViewById(R.id.btn_send);

        edtMotive.setOnClickListener(view -> {
            openPopup();
        });

        btnSend.setOnClickListener(view -> {
            sendData();
        });
        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());
    }

    private void sendData() {
        String name = edtName.getText().toString();
        String email = edtEmail.getText().toString();
        String phoneNumber = edtPhoneNumber.getText().toString();
        String motive = edtMotive.getText().toString();
        String message = edtMessage.getText().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Name is required", Toast.LENGTH_SHORT).show();
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
        } else if (!email.matches(Utils.emailPattern)) {
            Toast.makeText(this, "Invalid Email format", Toast.LENGTH_SHORT).show();
        } else if (phoneNumber.isEmpty()) {
            Toast.makeText(this, "Phone Number is required", Toast.LENGTH_SHORT).show();
        } else if (phoneNumber.length() != 10) {
            Toast.makeText(this, "Mobile Number should contain 10 digits", Toast.LENGTH_SHORT).show();
        } else if (motive.isEmpty()) {
            Toast.makeText(this, "Please select Motive To Contact", Toast.LENGTH_SHORT).show();
        } else if (message.isEmpty()) {
            Toast.makeText(this, "Message is required", Toast.LENGTH_SHORT).show();
        } else if (message.length() < 15) {
            Toast.makeText(this, "Message should be at least 15 char", Toast.LENGTH_SHORT).show();
        } else {
            /****
             * Send code here
             */

            showSuccessDialog();
        }
    }

    private void openPopup() {
        PopupMenu popupMenu = new PopupMenu(this, edtMotive);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_motive, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            edtMotive.setText(menuItem.getTitle());
            return true;
        });
        popupMenu.show();
    }

    private void showSuccessDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_success_contact_us);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);

        Button btnOK = dialog.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(view -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }
}
