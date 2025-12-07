package com.example.aifitnessapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.aifitnessapp.data.local.dao.ProfileDao
import com.example.aifitnessapp.data.local.dao.WeightHistoryDao
import com.example.aifitnessapp.data.local.dao.ChatDao
import com.example.aifitnessapp.data.local.dao.SavedDietDao
import com.example.aifitnessapp.domain.model.UserProfile
import com.example.aifitnessapp.domain.model.WeightHistory
import com.example.aifitnessapp.domain.model.ChatMessage
import com.example.aifitnessapp.domain.model.SavedDietPlan

@Database(
    entities = [
        UserProfile::class,
        WeightHistory::class,
        ChatMessage::class,
        SavedDietPlan::class
    ],
    version = 3,      // ⚠️ IMPORTANT: Increase this version when schema changes
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun profileDao(): ProfileDao
    abstract fun weightHistoryDao(): WeightHistoryDao
    abstract fun chatDao(): ChatDao

    // NEW DAO for saved diet plans
    abstract fun savedDietDao(): SavedDietDao
}
