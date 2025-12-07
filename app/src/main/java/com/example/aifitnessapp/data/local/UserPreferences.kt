package com.example.aifitnessapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val KEY_NAME = stringPreferencesKey("user_name")
        private val KEY_AGE = intPreferencesKey("user_age")
        private val KEY_HEIGHT = floatPreferencesKey("user_height_cm")
        private val KEY_WEIGHT = floatPreferencesKey("user_weight")
        private val KEY_TARGET = floatPreferencesKey("user_target_weight")
        private val KEY_ACTIVE_PROFILE = intPreferencesKey("active_profile_id")
    }

    // -------------------------------
    // 🔥 Expose flows to read values
    // -------------------------------
    val userNameFlow: Flow<String?> = context.dataStore.data.map { it[KEY_NAME] }
    val userAgeFlow: Flow<Int?> = context.dataStore.data.map { it[KEY_AGE] }
    val userHeightFlow: Flow<Float?> = context.dataStore.data.map { it[KEY_HEIGHT] }
    val userWeightFlow: Flow<Float?> = context.dataStore.data.map { it[KEY_WEIGHT] }
    val userTargetWeightFlow: Flow<Float?> = context.dataStore.data.map { it[KEY_TARGET] }

    val activeProfileFlow: Flow<Int?> = context.dataStore.data.map { it[KEY_ACTIVE_PROFILE] }

    // -------------------------------
    // 🔥 Save user details
    // -------------------------------
    suspend fun saveUserDetails(
        name: String,
        age: Int,
        heightCm: Float,
        weight: Float,
        targetWeight: Float
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_NAME] = name
            prefs[KEY_AGE] = age
            prefs[KEY_HEIGHT] = heightCm
            prefs[KEY_WEIGHT] = weight
            prefs[KEY_TARGET] = targetWeight
        }
    }

    // -------------------------------
    // 🔥 Save active profile id
    // -------------------------------
    suspend fun saveActiveProfile(id: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ACTIVE_PROFILE] = id
        }
    }
}
