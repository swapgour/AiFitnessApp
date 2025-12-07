package com.example.aifitnessapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aifitnessapp.data.local.DatabaseModule
import com.example.aifitnessapp.domain.model.WeightHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import android.content.Context

class WeightViewModel(context: Context) : ViewModel() {

    private val db = DatabaseModule.getDatabase(context)
    private val weightDao = db.weightHistoryDao()

    private val _history = MutableStateFlow<List<WeightHistory>>(emptyList())
    val history: StateFlow<List<WeightHistory>> = _history

    fun loadHistory(profileId: Int) {
        viewModelScope.launch {
            _history.value = weightDao.getHistory(profileId)
        }
    }

    fun addWeight(profileId: Int, weight: Float) {
        viewModelScope.launch {
            weightDao.insert(
                WeightHistory(
                    profileId = profileId,
                    weight = weight,
                    date = System.currentTimeMillis()
                )
            )
            loadHistory(profileId)
        }
    }
}
