package com.example.aifitnessapp.notification

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.aifitnessapp.R

class DietNotificationWorker(
    appContext: Context,
    params: WorkerParameters
) : Worker(appContext, params) {

    override fun doWork(): Result {

        val mealName = inputData.getString("mealName") ?: return Result.failure()
        val items = inputData.getString("items") ?: ""

        val notification = NotificationCompat.Builder(applicationContext, "diet_channel")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("$mealName Reminder")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Eat:\n$items"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        NotificationManagerCompat.from(applicationContext)
            .notify(mealName.hashCode(), notification)

        return Result.success()
    }
}
