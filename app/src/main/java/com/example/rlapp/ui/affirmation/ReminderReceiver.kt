package com.example.rlapp.ui.affirmation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Show notification or perform reminder action
        NotificationHelper.showNotification(
            context,
            "Right Life Reminder",
            "It's time for your scheduled affirmation practise!"
        )
    }
}
