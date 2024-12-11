package com.example.rlapp.ui.drawermenu;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rlapp.R;

public class DeleteAccountActivity extends AppCompatActivity {

    private EditText edtDeleteMessage;
    private Button btnSend;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());

        edtDeleteMessage = findViewById(R.id.edt_delete_message);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(view -> {
            sendDeleteMessage();
        });

    }

    private void sendDeleteMessage() {
        String message = edtDeleteMessage.getText().toString();
        if (message.isEmpty()) {
            Toast.makeText(this, "Message is required", Toast.LENGTH_SHORT).show();
        } else if (message.length() < 15) {
            Toast.makeText(this, "Message should be at least 15 char", Toast.LENGTH_SHORT).show();
        } else {
            showSuccessDialog();
        }
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_success_contact_us);
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        // Set the dim amount
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.dimAmount = 0.7f; // Adjust the dim amount (0.0 - 1.0)
        window.setAttributes(layoutParams);
        TextView tvDialog = dialog.findViewById(R.id.tv_dialog_message);
        tvDialog.setText("Our Support Team will commence the process of deleting your account and you will be notified of the confirmation shortly");

        Button btnOK = dialog.findViewById(R.id.btn_ok);
        btnOK.setOnClickListener(view -> {
            dialog.dismiss();
            finish();
        });

        dialog.show();
    }
}
