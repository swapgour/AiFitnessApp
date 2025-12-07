package com.example.aifitnessapp.domain.usecase

import android.content.Context
import com.example.aifitnessapp.data.local.ActiveProfileManager
import com.example.aifitnessapp.data.local.DatabaseModule
import com.example.aifitnessapp.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetActiveProfileUseCase(private val context: Context) {

    private val prefs = ActiveProfileManager(context)
    private val profileDao = DatabaseModule.getDatabase(context).profileDao()

    val activeProfile: Flow<UserProfile?> =
        prefs.activeProfileId.map { id ->
            if (id == null) {
                null
            } else {
                // DAO returns UserProfile? (not Flow)
                profileDao.getProfileById(id)
            }
        }
}
