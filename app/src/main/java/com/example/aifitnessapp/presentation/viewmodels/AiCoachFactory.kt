package com.example.aifitnessapp.presentation.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AiCoachFactory(
    private val apiKey: String,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AiCoachViewModel::class.java)) {
            return AiCoachViewModel(apiKey = apiKey, context = context) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
