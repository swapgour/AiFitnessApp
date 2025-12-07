package com.example.aifitnessapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_diet_plans")
data class SavedDietPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val profileId: Int,             // Which user this plan belongs to
    val timestamp: Long,            // When the plan was generated
    val json: String                // Full JSON returned by Gemini
)
