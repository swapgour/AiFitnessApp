package com.example.aifitnessapp.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aifitnessapp.domain.model.UserProfile
import com.example.aifitnessapp.domain.model.WeightHistory
import com.example.aifitnessapp.domain.usecase.AddWeightEntryUseCase
import com.example.aifitnessapp.domain.usecase.GetWeightHistoryUseCase
import com.example.aifitnessapp.domain.usecase.GetProfileByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getProfileByIdUseCase: GetProfileByIdUseCase,
    private val getWeightHistoryUseCase: GetWeightHistoryUseCase,
    private val addWeightEntryUseCase: AddWeightEntryUseCase
) : ViewModel() {

    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile

    private val _weightHistory = MutableStateFlow<List<WeightHistory>>(emptyList())
    val weightHistory: StateFlow<List<WeightHistory>> = _weightHistory

    fun loadProfile(profileId: Int) {
        viewModelScope.launch {
            _profile.value = getProfileByIdUseCase(profileId)
        }
    }

    fun loadHistory(profileId: Int) {
        viewModelScope.launch {
            _weightHistory.value = getWeightHistoryUseCase(profileId)
        }
    }

    fun addWeight(profileId: Int, weight: Float) {
        viewModelScope.launch {
            addWeightEntryUseCase(profileId, weight)
            _weightHistory.value = getWeightHistoryUseCase(profileId)
        }
    }
}
