package com.example.aifitnessapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weight_history")
data class WeightHistory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val profileId: Int,
    val weight: Float,
    val date: Long = System.currentTimeMillis()  // ⭐ FIX: add date
)
