package com.example.aifitnessapp.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.aifitnessapp.data.local.dao.SavedDietDao

class DietViewModelFactory(
    private val apiKey: String,
    private val savedDao: SavedDietDao,
    private val profileId: Int,
    private val appContext: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DietPlanViewModel::class.java)) {
            return DietPlanViewModel(
                apiKey = apiKey,
                savedDao = savedDao,
                profileId = profileId,
                appContext = appContext
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
