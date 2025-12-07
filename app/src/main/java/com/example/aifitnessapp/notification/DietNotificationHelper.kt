package com.example.aifitnessapp.notification

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object DietNotificationHelper {

    fun scheduleMealReminder(
        context: Context,
        mealName: String,
        items: String
    ) {
        val data = workDataOf(
            "mealName" to mealName,
            "items" to items
        )

        val work = OneTimeWorkRequestBuilder<DietNotificationWorker>()
            .setInitialDelay(10, TimeUnit.SECONDS)   // TEMP for testing
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(work)
    }
}
