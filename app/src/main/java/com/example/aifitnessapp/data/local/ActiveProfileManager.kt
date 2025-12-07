package com.example.aifitnessapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.activeProfileStore by preferencesDataStore("active_profile_prefs")

class ActiveProfileManager(private val context: Context) {

    companion object {
        val ACTIVE_PROFILE_ID = intPreferencesKey("active_profile_id")
    }

    // Save selected profile id
    suspend fun setActiveProfile(id: Int) {
        context.activeProfileStore.edit { prefs ->
            prefs[ACTIVE_PROFILE_ID] = id
        }
    }

    // Read selected profile id
    val activeProfileId: Flow<Int?> = context.activeProfileStore.data.map { prefs ->
        prefs[ACTIVE_PROFILE_ID]
    }
}
