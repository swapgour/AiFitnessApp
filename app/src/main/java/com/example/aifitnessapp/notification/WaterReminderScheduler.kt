package com.example.aifitnessapp.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.Calendar

object WaterReminderScheduler {

    fun scheduleWaterReminders(context: Context, weightKg: Float) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Calculate daily water intake
        val totalMl = weightKg * 35f  // 35 ml per kg
        val remindersPerDay = 6  // Every ~2 hours
        val mlPerReminder = totalMl / remindersPerDay

        for (i in 0 until remindersPerDay) {

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 8 + (i * 2)) // 8 AM, 10 AM, 12 PM, 2 PM, 4 PM, 6 PM
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }

            val intent = Intent(context, WaterReminderReceiver::class.java).apply {
                putExtra("ml", mlPerReminder)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                2000 + i, // unique ID for each reminder
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}
