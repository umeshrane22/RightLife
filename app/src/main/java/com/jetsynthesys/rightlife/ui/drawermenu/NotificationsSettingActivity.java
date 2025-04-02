package com.jetsynthesys.rightlife.ui.drawermenu;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.jetsynthesys.rightlife.R;

public class NotificationsSettingActivity extends AppCompatActivity {

    private SwitchCompat switchPushNotifications, switchReminderPushNotifications, switchMarketingPushNotifications,
            switchEmailNotifications, switchReminderEmailNotifications, switchMarketingEmailNotifications,
            switchSMSNotifications;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_settings);

        findViewById(R.id.ic_back_dialog).setOnClickListener(view -> finish());

        switchPushNotifications = findViewById(R.id.switch_push_notification);
        switchReminderPushNotifications = findViewById(R.id.switch_reminder_push_notification);
        switchMarketingPushNotifications = findViewById(R.id.switch_marketing_push_notification);
        switchEmailNotifications = findViewById(R.id.switch_email_notification);
        switchReminderEmailNotifications = findViewById(R.id.switch_reminder_email_notification);
        switchMarketingEmailNotifications = findViewById(R.id.switch_marketing_email_notification);
        switchSMSNotifications = findViewById(R.id.switch_sms_notification);

        switchPushNotifications.setOnCheckedChangeListener((compoundButton, b) -> {
            /****
             * Push Notification code here
             */
            switchReminderPushNotifications.setChecked(b);
            switchMarketingPushNotifications.setChecked(b);
        });

        switchEmailNotifications.setOnCheckedChangeListener((compoundButton, b) -> {
            /****
             * Email Notification code here
             */
            switchReminderEmailNotifications.setChecked(b);
            switchMarketingEmailNotifications.setChecked(b);
        });

        switchSMSNotifications.setOnCheckedChangeListener((compoundButton, b) -> {
            /****
             * SMS Notification code here
             */
        });

        switchReminderPushNotifications.setOnCheckedChangeListener((compoundButton, b) -> {
            /****
             * Reminder Push Notification code here
             */
            if (b && switchMarketingPushNotifications.isChecked()){
                switchPushNotifications.setChecked(true);
            } else {
                switchPushNotifications.setChecked(false);
            }
        });

        switchMarketingPushNotifications.setOnCheckedChangeListener((compoundButton, b) -> {
            /****
             * Marketing Push Notification code here
             */
            if (b && switchReminderPushNotifications.isChecked()){
                switchPushNotifications.setChecked(true);
            } else {
                switchPushNotifications.setChecked(false);
            }
        });


        switchReminderEmailNotifications.setOnCheckedChangeListener((compoundButton, b) -> {
            /****
             * Reminder Email Notification code here
             */
            if (b && switchMarketingEmailNotifications.isChecked()){
                switchEmailNotifications.setChecked(true);
            } else {
                switchEmailNotifications.setChecked(false);
            }
        });

        switchMarketingEmailNotifications.setOnCheckedChangeListener((compoundButton, b) -> {
            /****
             * Marketing Email Notification code here
             */
            if (b && switchReminderEmailNotifications.isChecked()){
                switchEmailNotifications.setChecked(true);
            } else {
                switchEmailNotifications.setChecked(false);
            }
        });

    }
}
