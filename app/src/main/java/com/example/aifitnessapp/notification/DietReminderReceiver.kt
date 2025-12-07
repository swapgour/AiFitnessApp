package com.example.aifitnessapp.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.aifitnessapp.R

class DietReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val mealName = intent.getStringExtra("mealName") ?: "Meal Reminder"
        val items = intent.getStringExtra("items") ?: ""

        val notification = NotificationCompat.Builder(context, "diet_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(mealName)
            .setStyle(
                NotificationCompat.BigTextStyle().bigText(
                    "It's time to eat:\n$items"
                )
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(mealName.hashCode(), notification)
    }
}
