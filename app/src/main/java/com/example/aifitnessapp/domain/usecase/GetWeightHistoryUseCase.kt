package com.example.aifitnessapp.domain.usecase

import com.example.aifitnessapp.data.local.dao.WeightHistoryDao
import com.example.aifitnessapp.domain.model.WeightHistory

class GetWeightHistoryUseCase(private val dao: WeightHistoryDao) {

    suspend operator fun invoke(profileId: Int): List<WeightHistory> {
        return dao.getHistory(profileId)
    }
}
