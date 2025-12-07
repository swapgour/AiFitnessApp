package com.example.aifitnessapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val age: Int,
    val heightCm: Float,
    val weight: Float,
    val targetWeight: Float
)
