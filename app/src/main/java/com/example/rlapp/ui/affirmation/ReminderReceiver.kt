package com.example.rlapp.ui.affirmation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Show notification or perform reminder action
        NotificationHelper.showNotification(
            context,
            "Reminder",
            "It's time for your scheduled event!"
        )
    }
}
