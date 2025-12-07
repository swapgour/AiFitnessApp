package com.example.aifitnessapp.domain.usecase

import com.example.aifitnessapp.data.local.dao.WeightHistoryDao
import com.example.aifitnessapp.domain.model.WeightHistory

class AddWeightEntryUseCase(
    private val dao: WeightHistoryDao
) {

    suspend operator fun invoke(profileId: Int, weight: Float) {
        val entry = WeightHistory(
            profileId = profileId,
            weight = weight,
            date = System.currentTimeMillis()
        )

        // Insert using correct DAO method
        dao.insert(entry)
    }
}
